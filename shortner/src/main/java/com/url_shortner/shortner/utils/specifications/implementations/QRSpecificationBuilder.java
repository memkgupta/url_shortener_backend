package com.url_shortner.shortner.utils.specifications.implementations;

import com.url_shortner.shortner.enitities.QR;
import com.url_shortner.shortner.utils.specifications.generics.SpecificationBuilder;
import com.url_shortner.shortner.utils.specifications.generics.SpecificationStrategyFactory;

public class QRSpecificationBuilder extends SpecificationBuilder<QR> {
    public QRSpecificationBuilder(SpecificationStrategyFactory<QR> specificationStrategyFactory) {
        super(specificationStrategyFactory);
    }
}
