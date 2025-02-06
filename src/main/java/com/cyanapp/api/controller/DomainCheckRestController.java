package com.cyanapp.api.controller;

import com.cyanapp.api.model.request.CheckDomainRequest;
import com.cyanapp.api.model.response.CheckDomainResponse;
import com.cyanapp.api.model.response.GeneralStatistics;
import com.cyanapp.api.service.impl.DomainServiceImpl;
import com.cyanapp.api.util.URLConstants;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(URLConstants.DOMAIN_CHECKER_PATH)
public class DomainCheckRestController {
    private static final Logger logger = LogManager.getLogger(DomainCheckRestController.class);
    @Autowired
    DomainServiceImpl domainService;

    @Autowired
    public DomainCheckRestController(DomainServiceImpl domainService) {
        this.domainService = domainService;
    }

    @PostMapping
    public ResponseEntity<CheckDomainResponse> checkDomain(@Valid @RequestBody CheckDomainRequest request) {
        logger.debug("inside checkDomain() ...");

        var domain = domainService.checkDomain(request.getUrl());
        var response = new CheckDomainResponse(domain.getDomain(), domain.getCategory(), domain.isBlocked());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(URLConstants.DOMAIN_STATISTICS_PATH)
    public ResponseEntity<GeneralStatistics> getDomainStatistics() {

        var stats = domainService.getApiStatistics();

        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
