package com.cyanapp.api.ControllerTest;

import com.cyanapp.api.controller.DomainCheckRestController;
import com.cyanapp.api.enums.CategoryType;
import com.cyanapp.api.model.dto.DomainDto;
import com.cyanapp.api.model.request.CheckDomainRequest;
import com.cyanapp.api.model.response.GeneralStatistics;
import com.cyanapp.api.service.impl.DomainServiceImpl;
import com.cyanapp.api.util.URLUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class DomainCheckControllerTest {
    private final String DOMAIN_CHECKER_PATH = "/domain-checker";
    private final String DOMAIN_STATISTICS_PATH = "/domain-checker/statistics";
    @Mock
    private DomainServiceImpl domainService;

    @InjectMocks
    private DomainCheckRestController domainCheckRestController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(domainCheckRestController).build();
    }

    @Test
    public void check_domain() throws Exception {
        String url = "https://cdn.instagram.com/images/13761dac?utm=google.com";

        var domain = new DomainDto();
        domain.setDomain(URLUtil.getDomainFromUrl(url));
        domain.setCategory(CategoryType.SOCIAL_MEDIA);
        when(domainService.checkDomain(url)).thenReturn(domain);

        var request = new CheckDomainRequest();
        request.setUrl(url);
        var objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(DOMAIN_CHECKER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    public void get_domain_statistics() throws Exception {
        when(domainService.getApiStatistics()).thenReturn(new GeneralStatistics());

        mockMvc.perform(MockMvcRequestBuilders.get(DOMAIN_STATISTICS_PATH))
                .andExpect(status().isOk());
    }


}
