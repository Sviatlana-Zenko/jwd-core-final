package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.BaseEntity;

/**
 * Should be a builder for {@link BaseEntity} fields
 */
public abstract class Criteria<T extends BaseEntity> {

    private Long id;
    private String name;

    public Criteria(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static CriteriaBuilder criteriaBuilder() {
        return new CriteriaBuilder();
    }

    public static class CriteriaBuilder {

        Long id;
        String name;

        public CriteriaBuilder() {
            this.configure();
        }

        protected void configure() {}

        public CriteriaBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CriteriaBuilder name(String name) {
            this.name = name;
            return this;
        }
    }
}
