package com.cyanapp.api.model.response;

import lombok.Data;

@Data
public class DomainStatistic {
    private String domain;
    private long hitCount;

    public DomainStatistic(String domain, long hitCount) {
        this.domain = domain;
        this.hitCount = hitCount;
    }
}
