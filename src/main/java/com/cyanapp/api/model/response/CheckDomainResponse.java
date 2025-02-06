package com.cyanapp.api.model.response;

import com.cyanapp.api.enums.CategoryType;
import lombok.Data;

@Data
public class CheckDomainResponse {
    private String domain;
    private CategoryType category;
    private boolean blocked;

    public CheckDomainResponse(String domain, CategoryType category, boolean blocked) {
        this.domain = domain;
        this.category = category;
        this.blocked = blocked;
    }

    public CheckDomainResponse() {
    }
}
