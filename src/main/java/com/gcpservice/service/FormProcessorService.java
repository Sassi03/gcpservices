package com.gcpservice.service;


import com.gcpservice.model.DocumentPage;
import com.gcpservice.model.DocumentPageFormFields;
import com.gcpservice.model.DocumentPageTable;
import com.gcpservice.model.DocumentResponse;
import com.google.cloud.documentai.v1beta3.Document;
import com.google.cloud.documentai.v1beta3.DocumentProcessorServiceClient;
import com.google.cloud.documentai.v1beta3.DocumentProcessorServiceSettings;
import com.google.cloud.documentai.v1beta3.ProcessRequest;
import com.google.cloud.documentai.v1beta3.ProcessResponse;
import com.google.cloud.documentai.v1beta3.RawDocument;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class FormProcessorService {

    public DocumentResponse getData(MultipartFile file) throws IOException {
        String projectId = "";
        String location = "us"; // Format is "us" or "eu".
        String processorId = "";
        byte[] fileBytes = file.getBytes();
        ByteString content = ByteString.copyFrom(fileBytes);
        String mimeType;
        switch (file.getContentType()) {
            case "application/pdf":
                mimeType = "application/pdf";
                break;
            case "image/tiff":
                mimeType = "image/tiff";
                break;
            default:
                throw new IllegalArgumentException("unsupported file type:" + file.getContentType());
        }
        String endpoint = String.format("%s-documentai.googleapis.com:443", location);

        DocumentProcessorServiceSettings settings =
                DocumentProcessorServiceSettings.newBuilder()
                        .setEndpoint(endpoint)
                        .build();
        DocumentResponse formParserDocumentResponse = new DocumentResponse();
        try (DocumentProcessorServiceClient client = DocumentProcessorServiceClient.create(settings)) {
            // The full resource name of the processor, e.g.:
            // projects/project-id/locations/location/processor/processor-id
            // You must create new processors in the Cloud Console first
            String name =
                    String.format("projects/%s/locations/%s/processors/%s", projectId, location, processorId);

            RawDocument document =
                    RawDocument.newBuilder().setContent(content).setMimeType(mimeType).build();

            // Configure the process request.
            ProcessRequest request =
                    ProcessRequest.newBuilder().setName(name).setRawDocument(document).build();

            // Recognizes text entities in the PDF document
            ProcessResponse result = client.processDocument(request);
            Document documentResponse = result.getDocument();

            System.out.println("Document processing complete.");

            // Read the text recognition output from the processor
            // For a full list of Document object attributes,
            // please reference this page:
            // https://googleapis.dev/java/google-cloud-document-ai/latest/index.html

            // Get all of the document text as one big string
            String text = documentResponse.getText();
            if (text != null) {
                formParserDocumentResponse.setText(removeNewlines(text));
            }
            // Read the text recognition output from the processor
            List<Document.Page> pages = documentResponse.getPagesList();
            formParserDocumentResponse.setTotalNumberOfPages(pages.size());
            System.out.printf("There are %s page(s) in this document.\n", pages.size());

            List<DocumentPage> pagesList = new ArrayList<>();
            for (Document.Page page : pages) {
                DocumentPage documentPage = new DocumentPage();
                documentPage.setPageNumber(page.getPageNumber());

                List<Document.Page.Table> tables = page.getTablesList();
                documentPage.setTotalNumberOfTables(tables.size());

                List<DocumentPageTable> DocumentPageList = new ArrayList<>();
                for (Document.Page.Table table : tables) {
                    DocumentPageTable documentPageTable = new DocumentPageTable();
                    Map<String, List<String>> tableData = getTableContent(table, text);
                    documentPageTable.setTable(tableData);
                    DocumentPageList.add(documentPageTable);
                }
                documentPage.setTables(DocumentPageList);

                List<Document.Page.FormField> formFields = page.getFormFieldsList();
                documentPage.setTotalFormFields(formFields.size());
                List<DocumentPageFormFields> formFieldsList = new ArrayList<>();

                for (Document.Page.FormField formField : formFields) {
                    DocumentPageFormFields documentPageFormField = new DocumentPageFormFields();
                    String fieldName = getLayoutText(formField.getFieldName().getTextAnchor(), text);
                    String fieldValue = getLayoutText(formField.getFieldValue().getTextAnchor(), text);
                    documentPageFormField.setFieldName(removeNewlines(fieldName));
                    documentPageFormField.setFieldValue(removeNewlines(fieldValue));
                    formFieldsList.add(documentPageFormField);
                }
                documentPage.setFormFields(formFieldsList);

                pagesList.add(documentPage);
            }
            formParserDocumentResponse.setPages(pagesList);
        }
        return formParserDocumentResponse;
    }

    private Map<String, List<String>> getTableContent(Document.Page.Table table, String text) {
        LinkedHashMap<String, List<String>> tableData = new LinkedHashMap<>();

        // Extract header information
        Document.Page.Table.TableRow headerRow = table.getHeaderRows(0);
        for (Document.Page.Table.TableCell cell : headerRow.getCellsList()) {
            String columnName = getLayoutText(cell.getLayout().getTextAnchor(), text);
            tableData.put((columnName), new ArrayList<>());
        }

        // Extract body information
        for (Document.Page.Table.TableRow bodyRow : table.getBodyRowsList()) {
            int columnIndex = 0;
            for (Document.Page.Table.TableCell cell : bodyRow.getCellsList()) {
                String columnName = getLayoutText(headerRow.getCells(columnIndex).getLayout().getTextAnchor(), text);
                String cellText = getLayoutText(cell.getLayout().getTextAnchor(), text);

                // Append cellText to the corresponding column in the map
                tableData.computeIfAbsent(columnName, k -> new ArrayList<>()).add(removeNewlines(cellText));

                columnIndex++;
            }
        }
        return tableData;
    }

    // Extract shards from the text field
    private static String getLayoutText(Document.TextAnchor textAnchor, String text) {
        if (textAnchor.getTextSegmentsList().size() > 0) {
            int startIdx = (int) textAnchor.getTextSegments(0).getStartIndex();
            int endIdx = (int) textAnchor.getTextSegments(0).getEndIndex();
            return text.substring(startIdx, endIdx);
        }
        return "[NO TEXT]";
    }

    private static String removeNewlines(String s) {
        return s.replace("\n", "").replace("\r", "");
    }
}

