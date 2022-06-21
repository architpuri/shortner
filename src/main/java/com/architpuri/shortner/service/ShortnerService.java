package com.architpuri.shortner.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShortnerService {

    public String shorten(final String inputUrl){
        log.info("Something is working");
        return inputUrl+"Shortened";
    }
}
