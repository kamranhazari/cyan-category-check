package com.cyanapp.api.service.impl;

import com.cyanapp.api.enums.CategoryType;
import com.cyanapp.api.model.dto.DomainDto;
import com.cyanapp.api.model.response.DomainStatistic;
import com.cyanapp.api.model.response.GeneralStatistics;
import com.cyanapp.api.persist.repository.DomainRepository;
import com.cyanapp.api.service.DomainService;
import com.cyanapp.api.util.URLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class DomainServiceImpl implements DomainService {
    HashMap<String, AtomicLong> blockedDomainHitCounter = new HashMap<>();
    @Autowired
    DomainRepository domainRepository;

    @Autowired
    public DomainServiceImpl(DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    /**
     * checks existence of the domain in database and returns its status
     *
     * @param url to check FQDN
     * @return {@code DomainDto} with given domain
     */
    @Override
    public DomainDto checkDomain(String url) {
        DomainDto result = new DomainDto();
        String fqdn = URLUtil.getDomainFromUrl(url);
        if (fqdn != null && !fqdn.isEmpty()) {
            result.setDomain(fqdn);
            var domain = domainRepository.findByDomain(fqdn);
            if (domain.isPresent()) {
                result.setCategory(domain.get().getCategory());
                result.setBlocked(domain.get().getBlocked());
                if (domain.get().getBlocked()) {
                    increaseDomainHitCount(fqdn);
                }
            } else {
                result.setCategory(CategoryType.UNKNOWN);
                result.setBlocked(false);
            }
            return result;
        } else {
            throw new IllegalArgumentException("Invalid URL format!");
        }
    }

    /**
     * return number of domains for given category type and status
     *
     * @param category the category type to filter
     * @param blocked  status of domains to check
     * @return number of domains
     */
    public long countDomainsByCategoryAndBlocked(CategoryType category, boolean blocked) {
        return domainRepository.countByCategoryAndBlocked(category, blocked);
    }

    /**
     * return number of domains filtering given status except the given category type
     *
     * @param excludedCategory category type to exclude
     * @param blocked          status of domains to check
     * @return number of domains
     */
    public long countDomainsBlockedExcludingCategory(CategoryType excludedCategory, boolean blocked) {
        return domainRepository.countExcludedCategoryAndBlocked(excludedCategory, blocked);
    }

    /**
     * return number of domains for given status
     *
     * @param blocked status of domains to check and count
     * @return number of domains
     */
    public long countBlockedDomains(boolean blocked) {
        return domainRepository.countByBlocked(blocked);
    }

    /**
     * get Api Statistics for domains inquired by other parties and current stats of system
     *
     * @return {@code GeneralStatistics} containing statistics of MALWARE_AND_PISHING category
     * and current inquiry stats of domains
     */
    public GeneralStatistics getApiStatistics() {
        var stats = new GeneralStatistics();
        stats.setMalwarePhishingCount(domainRepository.countByCategoryAndBlocked(CategoryType.MALWARE_AND_PHISHING, true));
        stats.setNonMalwarePhishingCount(domainRepository.countExcludedCategoryAndBlocked(CategoryType.MALWARE_AND_PHISHING, true));

        var blockedHitCount = blockedDomainHitCounter.entrySet().stream()
                .map(entry -> new DomainStatistic(entry.getKey(), entry.getValue().get()))
                .collect(Collectors.toList());
        stats.setDomainStatistics(blockedHitCount);
        return stats;
    }

    private void increaseDomainHitCount(String domain) {
        if (!blockedDomainHitCounter.containsKey(domain)) {
            blockedDomainHitCounter.put(domain, new AtomicLong(1L));
        } else {
            blockedDomainHitCounter.get(domain).incrementAndGet();
        }
    }

}
