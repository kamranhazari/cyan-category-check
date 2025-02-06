package com.cyanapp.api.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(includeFieldNames = true)
public class CheckDomainRequest {
    @NotNull
    private String url;
}
