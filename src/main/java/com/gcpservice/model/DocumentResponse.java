package com.gcpservice.model;


import java.util.List;

public class DocumentResponse {
    private String text;

    private Integer totalNumberOfPages;
    private List<DocumentPage> pages;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getTotalNumberOfPages() {
        return totalNumberOfPages;
    }

    public void setTotalNumberOfPages(Integer totalNumberOfPages) {
        this.totalNumberOfPages = totalNumberOfPages;
    }

    public List<DocumentPage> getPages() {
        return pages;
    }

    public void setPages(List<DocumentPage> pages) {
        this.pages = pages;
    }
}
