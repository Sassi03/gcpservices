package com.gcpservice.model;

import java.util.List;

public class DocumentPage {
    private Integer pageNumber;

    private Integer totalNumberOfTables;

    private Integer totalFormFields;
    private List<DocumentPageTable> tables;

    private List<DocumentPageFormFields> formFields;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getTotalNumberOfTables() {
        return totalNumberOfTables;
    }

    public Integer getTotalFormFields() {
        return totalFormFields;
    }

    public void setTotalFormFields(Integer totalFormFields) {
        this.totalFormFields = totalFormFields;
    }

    public void setTotalNumberOfTables(Integer totalNumberOfTables) {
        this.totalNumberOfTables = totalNumberOfTables;
    }

    public List<DocumentPageTable> getTables() {
        return tables;
    }

    public void setTables(List<DocumentPageTable> tables) {
        this.tables = tables;
    }

    public List<DocumentPageFormFields> getFormFields() {
        return formFields;
    }

    public void setFormFields(List<DocumentPageFormFields> formFields) {
        this.formFields = formFields;
    }
}
