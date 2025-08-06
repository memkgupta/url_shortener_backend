package com.url_shortner.shortner.utils.specifications.implementations;


import com.url_shortner.shortner.utils.specifications.generics.SpecificationBuilder;
import com.url_shortner.shortner.utils.specifications.generics.SpecificationStrategyFactory;
import com.url_shortner.shortner.enitities.URL;
import org.springframework.stereotype.Component;

@Component
public class URLSpecificationBuilder extends SpecificationBuilder<URL> {
    public URLSpecificationBuilder(SpecificationStrategyFactory<URL> specificationStrategyFactory) {
        super(specificationStrategyFactory);
    }
}
