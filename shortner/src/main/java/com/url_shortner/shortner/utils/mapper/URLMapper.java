package com.url_shortner.shortner.utils.mapper;

import com.url_shortner.shortner.dtos.UrlDTO;
import com.url_shortner.shortner.enitities.URL;

public class URLMapper implements EntityToDTOMapper<URL, UrlDTO> {
    @Override
    public UrlDTO toDTO(URL entity) {
        return UrlDTO.builder()
                .shortURL("http://localhost:8001/s/" + entity.getShortCode())
                .originalURL(entity.getUrl())
                .id(entity.getId())
                .shortCode(entity.getShortCode())
                .clicks(entity.getClicks())
                .createdAt(entity.getCreated_at())
                .build();
    }
}
