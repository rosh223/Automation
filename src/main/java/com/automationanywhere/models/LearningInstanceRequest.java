package com.automationanywhere.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LearningInstanceRequest {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("domainId")
    private String documentType;
    
    @JsonProperty("description")
    private String description;
    @JsonProperty("languageId")
    private String languageId;
    
    @JsonProperty("locale")
    private String locale;
    
    @JsonProperty("domainLanguageProviderId")
    private String providerId;

    public LearningInstanceRequest() {}

    public LearningInstanceRequest(String name, String domainId, String description, String languageId, String locale, String providerId) {
        this.name = name;
        this.documentType = domainId;
        this.description = description;
        this.languageId = languageId;
        this.locale = locale;
        this.providerId = providerId;
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

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
