package com.mycompany.myapp.util.mybatis.filter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.function.Consumer;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.RangeFilter;
import tech.jhipster.service.filter.StringFilter;

public interface QueryService<ENTITY> {
    default <X> Consumer<QueryWrapper<ENTITY>> buildSpecification(Filter<X> filter, String field) {
        return (
            queryWrapper -> {
                if (filter.getEquals() != null) {
                    queryWrapper.eq(field, filter.getEquals());
                } else if (filter.getIn() != null) {
                    queryWrapper.in(field, filter.getIn());
                } else if (filter.getNotEquals() != null) {
                    queryWrapper.ne(field, filter.getNotEquals());
                } else if (filter.getSpecified() != null) {
                    if (filter.getSpecified()) {
                        queryWrapper.isNotNull(field);
                    } else {
                        queryWrapper.isNull(field);
                    }
                }
            }
        );
    }

    default Consumer<QueryWrapper<ENTITY>> buildStringSpecification(StringFilter filter, String field) {
        return buildSpecification(filter, field);
    }

    default Consumer<QueryWrapper<ENTITY>> buildSpecification(StringFilter filter, String field) {
        return (
            queryWrapper -> {
                if (filter.getEquals() != null) {
                    queryWrapper.eq(field, filter.getEquals());
                } else if (filter.getIn() != null) {
                    queryWrapper.in(field, filter.getIn());
                } else if (filter.getContains() != null) {
                    queryWrapper.like(field, filter.getContains());
                } else if (filter.getDoesNotContain() != null) {
                    queryWrapper.notLike(field, filter.getDoesNotContain());
                } else if (filter.getNotEquals() != null) {
                    queryWrapper.ne(field, filter.getNotEquals());
                } else if (filter.getSpecified() != null) {
                    if (filter.getSpecified()) {
                        queryWrapper.isNotNull(field);
                    } else {
                        queryWrapper.isNull(field);
                    }
                }
            }
        );
    }

    default <X extends Comparable<? super X>> Consumer<QueryWrapper<ENTITY>> buildRangeSpecification(RangeFilter<X> filter, String field) {
        return buildSpecification(filter, field);
    }

    default <X extends Comparable<? super X>> Consumer<QueryWrapper<ENTITY>> buildSpecification(RangeFilter<X> filter, String field) {
        return (
            queryWrapper -> {
                if (filter.getEquals() != null) {
                    queryWrapper.eq(field, filter.getEquals());
                } else if (filter.getIn() != null) {
                    queryWrapper.in(field, filter.getIn());
                }
                if (filter.getSpecified() != null) {
                    if (filter.getSpecified()) {
                        queryWrapper.isNotNull(field);
                    } else {
                        queryWrapper.isNull(field);
                    }
                }
                if (filter.getNotEquals() != null) {
                    queryWrapper.ne(field, filter.getNotEquals());
                }
                if (filter.getGreaterThan() != null) {
                    queryWrapper.gt(field, filter.getGreaterThan());
                }
                if (filter.getGreaterThanOrEqual() != null) {
                    queryWrapper.ge(field, filter.getGreaterThanOrEqual());
                }
                if (filter.getLessThan() != null) {
                    queryWrapper.lt(field, filter.getLessThan());
                }
                if (filter.getLessThanOrEqual() != null) {
                    queryWrapper.le(field, filter.getLessThanOrEqual());
                }
            }
        );
    }
}
