package com.cyanapp.api.model.dto;

import com.cyanapp.api.enums.CategoryType;
import lombok.Data;

@Data
public class DomainDto {
    private String domain;
    private CategoryType category;
    private boolean blocked;
}
