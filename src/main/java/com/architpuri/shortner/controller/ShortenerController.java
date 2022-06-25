package com.architpuri.shortner.controller;

import com.architpuri.shortner.util.CommonUtils;
import com.architpuri.shortner.service.ShortenerService;
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
    public ResponseEntity<String> shortenApi(final @RequestParam("inputUrl") String inputUrl,
                                             final @RequestParam(required = false) String desiredUrl,
                                             final @RequestParam(required = false) String redirect) {
        Boolean isRedirect = true;
        if (StringUtils.isNotEmpty(redirect)) {
            if ("No".equalsIgnoreCase(redirect)) {
                isRedirect = true;
            }
        }
        if (CommonUtils.isValidUrl(inputUrl)) {
            return shortenerService.createUrlEntry(inputUrl, desiredUrl,isRedirect);
        } else {
            return new ResponseEntity<>("Invalid Input Url Attribute", HttpStatus.BAD_REQUEST);
        }
    }
}
