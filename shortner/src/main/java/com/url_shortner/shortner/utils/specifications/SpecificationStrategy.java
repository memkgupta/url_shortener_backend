package com.url_shortner.shortner.utils.specifications;

import org.springframework.data.jpa.domain.Specification;

@FunctionalInterface
public interface SpecificationStrategy<T,D> {
    Specification<T> getSpecification(D value);
}
