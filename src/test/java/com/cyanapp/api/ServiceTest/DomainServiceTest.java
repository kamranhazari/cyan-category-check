package com.cyanapp.api.ServiceTest;


import com.cyanapp.api.enums.CategoryType;
import com.cyanapp.api.model.response.DomainStatistic;
import com.cyanapp.api.persist.entity.DomainEntity;
import com.cyanapp.api.persist.repository.DomainRepository;
import com.cyanapp.api.service.DomainService;
import com.cyanapp.api.service.impl.DomainServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DomainServiceTest {
    @Mock
    private DomainRepository domainRepository;

    private DomainService domainService;//we will do DI manually here 8-]

    @BeforeEach
    public void setup() {
        //Manually Inject dependencies
        domainService = new DomainServiceImpl(domainRepository);
    }

    @Test
    public void check_non_submitted_domain() {
        String url = "https://cdn.instagram.com/images/13761dac?utm=google.com";

        when(domainRepository.findByDomain("cdn.instagram.com")).thenReturn(Optional.empty());

        var result = domainService.checkDomain(url);

        Assertions.assertEquals(result.getCategory(), CategoryType.UNKNOWN, "non submitted domain has unKnown category");

        verify(domainRepository).findByDomain("cdn.instagram.com");
    }

    @Test
    public void check_submitted_domain() {
        String fqdn = "phishing.badinternetdomain.com";
        String url = String.format("https://%s/images/13761dac?utm=google.com", fqdn);

        var domain = new DomainEntity();
        domain.setDomain(fqdn);
        domain.setCategory(CategoryType.MALWARE_AND_PHISHING);
        domain.setBlocked(true);
        when(domainRepository.findByDomain(fqdn)).thenReturn(Optional.of(domain));

        var result = domainService.checkDomain(url);

        Assertions.assertEquals(result.getCategory(), CategoryType.MALWARE_AND_PHISHING, "submitted domain is MALWARE_AND_PHISHING");

        verify(domainRepository).findByDomain(fqdn);
    }

    @Test
    public void check_invalid_domain() {
        String fqdn = "phishing.badinternetdomain.com";
        String url = String.format("%s/images", fqdn);

        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> domainService.checkDomain(url), "inavlid url sent");

    }

    @Test
    public void get_domain_stats() {
        var category = CategoryType.MALWARE_AND_PHISHING;
        when(domainRepository.countByCategoryAndBlocked(category, true)).thenReturn(2L);
        when(domainRepository.countExcludedCategoryAndBlocked(category, true)).thenReturn(6L);

        var result = domainService.getApiStatistics();

        Assertions.assertEquals(result.getMalwarePhishingCount(), 2L);
        Assertions.assertEquals(result.getNonMalwarePhishingCount(), 6L);

    }

    @Test
    public void get_domain_stats_and_check_hitCount() {
        String fqdn = "phishing.badinternetdomain.com";
        String url = String.format("https://%s/images/13761dac?utm=google.com", fqdn);

        var category = CategoryType.MALWARE_AND_PHISHING;
        var domain = new DomainEntity();
        domain.setDomain(fqdn);
        domain.setCategory(category);
        domain.setBlocked(true);

        when(domainRepository.findByDomain(fqdn)).thenReturn(Optional.of(domain));
        when(domainRepository.countByCategoryAndBlocked(category, true)).thenReturn(2L);
        when(domainRepository.countExcludedCategoryAndBlocked(category, true)).thenReturn(6L);

        var checkResult = domainService.checkDomain(url);

        Assertions.assertEquals(checkResult.getCategory(), CategoryType.MALWARE_AND_PHISHING, "submitted domain is MALWARE_AND_PHISHING");

        var result = domainService.getApiStatistics();

        Assertions.assertEquals(result.getMalwarePhishingCount(), 2L);
        Assertions.assertEquals(result.getNonMalwarePhishingCount(), 6L);
        Optional<DomainStatistic> domainInfo = result.getDomainStatistics().stream().
                filter(stat -> stat.getDomain().equals(fqdn)).findFirst();

        Assertions.assertEquals(domainInfo.get().getHitCount(), 1L);

        checkResult = domainService.checkDomain(url);
        result = domainService.getApiStatistics();

        domainInfo = result.getDomainStatistics().stream().
                filter(stat -> stat.getDomain().equals(fqdn)).findFirst();

        Assertions.assertEquals(domainInfo.get().getHitCount(), 2L);
    }

    @Test
    public void get_domain_stats_and_check_hitCount_without_increment() {
        String fqdn = "phishing.badinternetdomain.com";
        String url = String.format("https://%s/images/13761dac?utm=google.com", fqdn);
        String fqdn2 = "cdn.instagram.com";
        String url2 = String.format("https://%s/books/76482468?utm=microsoft.com", fqdn2);

        var category = CategoryType.MALWARE_AND_PHISHING;
        var domain = new DomainEntity();
        domain.setDomain(fqdn);
        domain.setCategory(category);
        domain.setBlocked(true);
        when(domainRepository.findByDomain(fqdn2)).thenReturn(Optional.empty());
        when(domainRepository.findByDomain(fqdn)).thenReturn(Optional.of(domain));
        when(domainRepository.countByCategoryAndBlocked(category, true)).thenReturn(2L);
        when(domainRepository.countExcludedCategoryAndBlocked(category, true)).thenReturn(6L);

        var checkResult = domainService.checkDomain(url);

        Assertions.assertEquals(checkResult.getCategory(), CategoryType.MALWARE_AND_PHISHING, "submitted domain is MALWARE_AND_PHISHING");

        var result = domainService.getApiStatistics();

        Assertions.assertEquals(result.getMalwarePhishingCount(), 2L);
        Assertions.assertEquals(result.getNonMalwarePhishingCount(), 6L);
        Optional<DomainStatistic> domainInfo = result.getDomainStatistics().stream().
                filter(stat -> stat.getDomain().equals(fqdn)).findFirst();

        Assertions.assertEquals(domainInfo.get().getHitCount(), 1L);

        checkResult = domainService.checkDomain(url2);
        result = domainService.getApiStatistics();

        domainInfo = result.getDomainStatistics().stream().
                filter(stat -> stat.getDomain().equals(fqdn)).findFirst();

        Assertions.assertEquals(domainInfo.get().getHitCount(), 1L);
    }
}
