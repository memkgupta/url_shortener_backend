package com.url_shortner.shortner.utils;

import com.url_shortner.shortner.dtos.PaginatedResponse;
import com.url_shortner.shortner.utils.mapper.EntityToDTOMapper;
import org.springframework.data.domain.Page;

public class PaginationUtil {

    public static <T,D> PaginatedResponse<D> toPaginatedResponse(Page<T> page, EntityToDTOMapper<T,D> entityToDTOMapper) {
        PaginatedResponse<D> response = new PaginatedResponse<>();
        response.setTotal((int) page.getTotalElements());
        response.setData(
                page.getContent().stream().map(entityToDTOMapper::toDTO).toList()
        );

        int currentPage = page.getNumber();
        int totalPages = page.getTotalPages();

        response.setPrevious(currentPage > 0 ? currentPage - 1 : -1);
        response.setNext(currentPage + 1 < totalPages ? currentPage + 1 : -1);

        return response;
    }
}