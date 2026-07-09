package com.automationanywhere.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;

public class LearningInstanceRequest {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("description")
    private String description;

    @JsonProperty("domainId")
    private String domainId;

    @JsonProperty("domainLanguageId")
    private String domainLanguageId;

    @JsonProperty("domainLanguageProviderId")
    private String domainLanguageProviderId;

    @JsonProperty("fields")
    private Object[] fields = new Object[0];

    @JsonProperty("genaiFeature")
    private Map<String, Object> genaiFeature = new HashMap<>();

    @JsonProperty("genaiProvider")
    private String genaiProvider = "Open_AI";

    @JsonProperty("isCloudExtraction")
    private boolean isCloudExtraction = false;

    @JsonProperty("isDefault")
    private boolean isDefault = true;

    @JsonProperty("isGenAIEnabled")
    private boolean isGenAIEnabled = true;

    @JsonProperty("isHeuristicFeedbackEnabled")
    private boolean isHeuristicFeedbackEnabled = true;

    @JsonProperty("locale")
    private String locale = "en-US";

    @JsonProperty("modelConnectionId")
    private String modelConnectionId = "";

    @JsonProperty("modelConnectionName")
    private String modelConnectionName = "";

    @JsonProperty("rules")
    private Object[] rules = new Object[0];

    @JsonProperty("selectedGenaiProvider")
    private String selectedGenaiProvider = "Open_AI";

    @JsonProperty("tables")
    private Object[] tables = new Object[0];

    @JsonProperty("useGenai")
    private boolean useGenai = true;

    public LearningInstanceRequest() {
        this.genaiFeature.put("tableFieldsSupported", true);
    }

    public LearningInstanceRequest(String name, String description, String domainId, String domainLanguageId, String domainLanguageProviderId) {
        this();
        this.name = name;
        this.description = description;
        this.domainId = domainId;
        this.domainLanguageId = domainLanguageId;
        this.domainLanguageProviderId = domainLanguageProviderId;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDomainId() { return domainId; }
    public void setDomainId(String domainId) { this.domainId = domainId; }

    public String getDomainLanguageId() { return domainLanguageId; }
    public void setDomainLanguageId(String domainLanguageId) { this.domainLanguageId = domainLanguageId; }

    public String getDomainLanguageProviderId() { return domainLanguageProviderId; }
    public void setDomainLanguageProviderId(String domainLanguageProviderId) { this.domainLanguageProviderId = domainLanguageProviderId; }

    // Other getters and setters omitted for brevity since RestAssured serialize using Jackson fields directly
}
