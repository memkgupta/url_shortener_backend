package com.url_shortner.shortner.utils.specifications.generics;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public abstract class SpecificationBuilder<T>{
    private final SpecificationStrategyFactory<T> specificationStrategyFactory;

    public SpecificationBuilder(SpecificationStrategyFactory<T> specificationStrategyFactory) {
        this.specificationStrategyFactory = specificationStrategyFactory;
    }
    public Specification<T> build(Map<String,String> params)
    {
        Specification<T> specification = null;
        for(Map.Entry<String,String> entry : params.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();
            Specification<T> specificationStrategy = specificationStrategyFactory.getSpecification(key,value);
            specification = specification == null ? specificationStrategy : specificationStrategy.and(specificationStrategy);
        }
        return specification;
    }
}
