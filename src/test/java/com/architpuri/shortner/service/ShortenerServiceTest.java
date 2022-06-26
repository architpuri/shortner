package com.architpuri.shortner.service;

import com.architpuri.shortner.helper.ShorteningHelper;
import com.architpuri.shortner.helper.UrlDetailsHelper;
import com.architpuri.shortner.model.UrlDetails;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ShortenerService.class)
public class ShortenerServiceTest {

    @Autowired
    private ShortenerService shortenerService;

    @MockBean
    private ShorteningHelper shorteningHelper;

    @MockBean
    private UrlDetailsHelper urlDetailsHelper;

    @Test
    public void fetchUrlEntry_NoRedirect_Success() {
        UrlDetails dummyDetails = new UrlDetails(1L, "test-url", "http://test.com", false);
        when(urlDetailsHelper.getUrlDetailsByAlias(anyString())).thenReturn(dummyDetails);
        ResponseEntity<String> result = shortenerService.fetchUrlEntry("test-url");
        Assert.assertEquals(HttpStatus.FOUND, result.getStatusCode());
    }

    @Test
    public void fetchUrlEntry_Redirect_Success() {
        UrlDetails dummyDetails = new UrlDetails(1L, "test-url", "http://test.com", true);
        when(urlDetailsHelper.getUrlDetailsByAlias(anyString())).thenReturn(dummyDetails);
        ResponseEntity<String> result = shortenerService.fetchUrlEntry("test-url");
        Assert.assertEquals(HttpStatus.FOUND, result.getStatusCode());
    }

    @Test
    public void createUrlEntry_AlreadyExists() {
        UrlDetails dummyDetails = new UrlDetails(1L, "test-url", "http://test.com", true);
        when(urlDetailsHelper.getUrlDetailsByAlias(anyString())).thenReturn(dummyDetails);
        ResponseEntity<String> result = shortenerService.createUrlEntry("http://test.com", "test-url", true);
        Assert.assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }
}