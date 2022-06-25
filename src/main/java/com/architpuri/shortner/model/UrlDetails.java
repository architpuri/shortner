package com.architpuri.shortner.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class UrlDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String aliasUrl;
    private String originalUrl;
    private Boolean redirect;

    public UrlDetails(final String aliasUrl, final String originalUrl, final Boolean redirect) {
        this.aliasUrl = aliasUrl;
        this.originalUrl = originalUrl;
        this.redirect = redirect;
    }
}
