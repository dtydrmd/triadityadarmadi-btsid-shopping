package com.example.shop.controller;

import com.example.shop.support.SupportArticle;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/support/errors")
public class SupportController {

    private final Map<String, SupportArticle> articles;

    public SupportController() {
        Map<String, SupportArticle> seed = new LinkedHashMap<>();
        SupportArticle faaPost124 = SupportArticle.forFaaPost124();
        seed.put(faaPost124.getCode(), faaPost124);
        this.articles = Collections.unmodifiableMap(seed);
    }

    @GetMapping
    public Collection<SupportArticle> listErrors() {
        return articles.values();
    }

    @GetMapping("/{code}")
    public SupportArticle getError(@PathVariable String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error code is required");
        }
        SupportArticle article = articles.get(code.trim().toUpperCase(Locale.ROOT));
        if (article == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown error code");
        }
        return article;
    }
}
