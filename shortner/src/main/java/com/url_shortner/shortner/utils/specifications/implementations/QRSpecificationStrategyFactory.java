package com.url_shortner.shortner.utils.specifications.implementations;

import com.url_shortner.shortner.enitities.QR;
import com.url_shortner.shortner.utils.specifications.SpecificationStrategy;
import com.url_shortner.shortner.utils.specifications.generics.SpecificationStrategyFactory;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class QRSpecificationStrategyFactory extends SpecificationStrategyFactory<QR> {
    public QRSpecificationStrategyFactory(List<SpecificationStrategy> specificationStrategies) {
        super(specificationStrategies);
    }
}
