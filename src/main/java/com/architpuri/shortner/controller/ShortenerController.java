package com.architpuri.shortner.controller;

import com.architpuri.shortner.util.CommonUtils;
import com.architpuri.shortner.service.ShortenerService;
import com.architpuri.shortner.util.MessageConstants;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class ShortenerController {

    @Autowired
    private ShortenerService shortenerService;

    @GetMapping(value = "{inputUrl}")
    @ApiOperation("To get the original url from the shortened.")
    public ResponseEntity<String> getShortenedApi(final @PathVariable("inputUrl") String inputUrl) {
        return shortenerService.fetchUrlEntry(inputUrl);
    }

    @PostMapping(value = "shorten")
    @ApiOperation("To generate a shortened url.")
    public ResponseEntity<String> shortenApi(final @RequestParam("inputUrl") String originalUrl,
                                             final @RequestParam(required = false) String desiredUrl,
                                             final @RequestParam(required = false) String redirect,
                                             final @RequestParam(value = "expiryInSeconds", required = false) Long expiryInSeconds) {
        //By default redirect is true
        Boolean isRedirect = true;
        if (StringUtils.isNotEmpty(redirect)) {
            if ("No".equalsIgnoreCase(redirect)) {
                isRedirect = false;
            }
        }
        if (CommonUtils.isValidUrl(originalUrl)) {
            return shortenerService.createUrlEntry(originalUrl, desiredUrl, isRedirect,getExpiryMillisecond(expiryInSeconds));
        } else {
            return new ResponseEntity<>(MessageConstants.INVALID_URL_INPUT, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    @ApiOperation("To clear the existing record for deletion.")
    public ResponseEntity<String> deleteApi(final @RequestParam("inputUrl") String originalUrl) {
        return shortenerService.deleteUrlEntry(originalUrl);
    }

    private Long getExpiryMillisecond(final Long expiryInputSeconds) {
        if(expiryInputSeconds == null){
            return CommonUtils.getDefaultExpiry();
        }
        return expiryInputSeconds*1000;
    }
}
