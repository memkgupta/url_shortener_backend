package com.url_shortner.shortner.utils.specifications;


import com.url_shortner.shortner.annotations.SpecificationKey;
import com.url_shortner.shortner.enitities.QR;
import jakarta.persistence.criteria.Join;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Configuration
public class QRSpecification {

    @Component
    @SpecificationKey("org_name")
    public static class NameSpecification implements SpecificationStrategy<QR,String> {
        @Override
        public Specification<QR> getSpecification(String value) {
            return (root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + value.toLowerCase() + "%");
        }
    }


}
