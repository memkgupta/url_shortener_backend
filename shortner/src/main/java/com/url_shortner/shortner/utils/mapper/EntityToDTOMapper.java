package com.url_shortner.shortner.utils.mapper;

@FunctionalInterface
public interface EntityToDTOMapper<T,D> {
    public D toDTO(T entity);
}
