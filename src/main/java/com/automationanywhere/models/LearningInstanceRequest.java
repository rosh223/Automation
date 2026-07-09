package com.automationanywhere.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LearningInstanceRequest {
    
    @JsonProperty("name")
    private String name;
    
    public LearningInstanceRequest() {}

    @JsonProperty("description")
    private String description;

    public LearningInstanceRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
