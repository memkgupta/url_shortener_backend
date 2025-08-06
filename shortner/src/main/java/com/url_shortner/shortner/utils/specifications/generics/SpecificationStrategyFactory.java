package com.url_shortner.shortner.utils.specifications.generics;

import com.url_shortner.shortner.annotations.SpecificationKey;
import com.url_shortner.shortner.utils.specifications.SpecificationStrategy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
@Component
public abstract class SpecificationStrategyFactory<T> {
    protected final Map<String, SpecificationStrategy> strategies;

    public SpecificationStrategyFactory(List<SpecificationStrategy> strategyList) {
        this.strategies = strategyList.stream().collect(Collectors.toMap(
                strategy -> {
                    SpecificationKey key = strategy.getClass().getAnnotation(SpecificationKey.class);
                    if (key == null) throw new IllegalArgumentException("Missing @SpecificationKey");
                    return key.value();
                },
                Function.identity()
        ));
    }

    public Specification<T> getSpecification(String key, Object value) {
        SpecificationStrategy strategy = strategies.get(key);
        return strategy != null ? strategy.getSpecification(value) : null;
    }
}
