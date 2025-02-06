package com.cyanapp.api.model.response;

import lombok.Data;

import java.util.List;

@Data
public class GeneralStatistics {
    private long malwarePhishingCount;
    private long nonMalwarePhishingCount;
    private List<DomainStatistic> domainStatistics;
}
