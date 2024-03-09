package com.gcpservice.controller;

import com.gcpservice.model.DocumentResponse;
import com.gcpservice.service.FormProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class DocumentExtractionController {

    @Autowired
    FormProcessorService formProcessorService;

    @PostMapping("/extract")
    public DocumentResponse getDocumentData(@RequestParam("file") MultipartFile file) throws IOException {
        DocumentResponse data = formProcessorService.getData(file);
        return data;
    }
}
