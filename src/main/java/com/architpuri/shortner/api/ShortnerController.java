package com.architpuri.shortner.api;

import com.architpuri.shortner.service.ShortnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("shorten")
public class ShortnerController {

    @Autowired
    private ShortnerService shortenerService;

    @GetMapping(value = "")
    public String getShortenedApi(final @RequestParam("inputUrl") String inputUrl) {
        return shortenerService.shorten(inputUrl);
    }
}
