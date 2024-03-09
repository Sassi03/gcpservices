package com.gcpservice.model;

import java.util.List;
import java.util.Map;

public class DocumentPageTable {
    private Map<String, List<String>> table;

    public Map<String, List<String>> getTable() {
        return table;
    }

    public void setTable(Map<String, List<String>> table) {
        this.table = table;
    }
}
