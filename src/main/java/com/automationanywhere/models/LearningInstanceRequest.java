package com.automationanywhere.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LearningInstanceRequest {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("documentType")
    private String documentType;
    
    @JsonProperty("description")
    private String description;

    public LearningInstanceRequest() {}

    public LearningInstanceRequest(String name, String documentType, String description) {
        this.name = name;
        this.documentType = documentType;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
