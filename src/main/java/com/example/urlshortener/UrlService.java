package com.example.urlshortener;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    private String generateShortCode() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public Url shortenUrl(String originalUrl) {
        String code = generateShortCode();
        while (urlRepository.findByShortCode(code).isPresent()) {
            code = generateShortCode();
        }
        Url url = new Url();
        url.setShortCode(code);
        url.setOriginalUrl(originalUrl);
        return urlRepository.save(url);
    }

    public String getOriginalUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .map(Url::getOriginalUrl)
                .orElseThrow(() -> new RuntimeException("Short URL not found: " + shortCode));
    }
}