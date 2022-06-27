package com.architpuri.shortner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UrlDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String aliasUrl;
    private String originalUrl;
    private Boolean redirect;
    private Long createdOn;
    private Long expiry;

    public UrlDetails(final String aliasUrl, final String originalUrl, final Boolean redirect, final Long createdOn, final Long expiry) {
        this.aliasUrl = aliasUrl;
        this.originalUrl = originalUrl;
        this.redirect = redirect;
    }
}
