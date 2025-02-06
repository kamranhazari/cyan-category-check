package com.cyanapp.api.service;

import com.cyanapp.api.enums.CategoryType;
import com.cyanapp.api.model.dto.DomainDto;
import com.cyanapp.api.model.response.GeneralStatistics;

public interface DomainService {
    /**
     * checks existence of the domain in database and returns its status
     *
     * @param url to check FQDN
     * @return {@code DomainDto} with given domain
     */
    DomainDto checkDomain(String url);

    /**
     * get Api Statistics for domains inquired by other parties and current stats of system
     *
     * @return {@code GeneralStatistics} containing statistics of MALWARE_AND_PISHING category
     * and current inquiry stats of domains
     */
    GeneralStatistics getApiStatistics();

    /**
     * return number of domains for given category type and status
     *
     * @param category the category type to filter
     * @param blocked  status of domains to check
     * @return number of domains
     */
    long countDomainsByCategoryAndBlocked(CategoryType category, boolean blocked);

    /**
     * return number of domains filtering given status except the given category type
     *
     * @param excludedCategory category type to exclude
     * @param blocked          status of domains to check
     * @return number of domains
     */
    long countDomainsBlockedExcludingCategory(CategoryType excludedCategory, boolean blocked);

    /**
     * return number of domains for given status
     *
     * @param blocked status of domains to check and count
     * @return number of domains
     */
    long countBlockedDomains(boolean blocked);
}
