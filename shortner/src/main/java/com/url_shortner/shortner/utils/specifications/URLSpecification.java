package com.url_shortner.shortner.utils.specifications;

import com.url_shortner.shortner.annotations.SpecificationKey;
import com.url_shortner.shortner.enitities.Tag;
import com.url_shortner.shortner.enitities.URL;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Configuration
public class URLSpecification {

    @Component
    @SpecificationKey("url_user_id")
    public static class UserUrlSpecification implements SpecificationStrategy<URL,String> {
        @Override
        public Specification<URL> getSpecification(String value) {
            return (root, query, cb) -> cb.equal(root.get("user_id"), value );
        }
    }
    @Component
    @SpecificationKey("original_url")
    public static class OriginalUrlSpecification implements SpecificationStrategy<URL,String> {
        @Override
        public Specification<URL> getSpecification(String value) {
            return (root, query, cb) -> cb.like(cb.lower(root.get("url")), "%" + value.toLowerCase() + "%");
        }
    }
    @Component
    @SpecificationKey("short_code")
    public static class ShortCodeSpecification implements SpecificationStrategy<URL,String> {

        @Override
        public Specification<URL> getSpecification(String value) {
            return (root,query,cb)->cb.equal(root.get("short_code"), value);
        }
    }

    @Component
    @SpecificationKey("created_at_start")
    public static class CreatedAtStartSpecification implements SpecificationStrategy<URL, Timestamp> {
        @Override
        public Specification<URL> getSpecification(Timestamp value) {
            return (root,query,cb)->cb.greaterThanOrEqualTo(root.get("created_at"), value);
        }
    }
    @Component
    @SpecificationKey("created_at_end")
    public static class CreatedAtEndSpecification implements SpecificationStrategy<URL,Timestamp> {
        @Override
        public Specification<URL> getSpecification(Timestamp value) {
            return (root,query,cb)->cb.lessThanOrEqualTo(root.get("created_at"), value);
        }
    }
    @Component
    @SpecificationKey("url_with_tags")
    public static class TagsSpecification implements SpecificationStrategy<URL, Set<String>> {

        @Override
        public Specification<URL> getSpecification(Set<String> value) {

            return (root,query,cb)->{
                Join<URL, Tag> tagJoin = root.join("tags", JoinType.INNER);
                query.groupBy(root.get("id"));
                query.having(cb.equal(
                        cb.countDistinct(tagJoin.get("id")),
                        value.size()
                ));
                return tagJoin.get("name").in(value);

            };
        }
    }

}
