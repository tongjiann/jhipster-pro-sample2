package com.mycompany.myapp.system.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import com.diboot.core.vo.Pagination;
import com.mycompany.myapp.system.domain.*; // for static metamodels
import com.mycompany.myapp.system.domain.SiteConfig;
import com.mycompany.myapp.system.repository.SiteConfigRepository;
import com.mycompany.myapp.system.service.criteria.SiteConfigCriteria;
import com.mycompany.myapp.system.service.dto.SiteConfigDTO;
import com.mycompany.myapp.system.service.mapper.SiteConfigMapper;
import com.mycompany.myapp.util.CriteriaUtil;
import com.mycompany.myapp.util.mybatis.filter.QueryService;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * 用于对数据库中的{@link SiteConfig}实体执行复杂查询的Service。
 * 主要输入是一个{@link SiteConfigCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link SiteConfigDTO}列表{@link List} 或 {@link SiteConfigDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class SiteConfigQueryService implements QueryService<SiteConfig> {

    private final Logger log = LoggerFactory.getLogger(SiteConfigQueryService.class);

    private final DynamicJoinQueryWrapper<SiteConfig, SiteConfig> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        SiteConfig.class,
        null
    );

    private final SiteConfigRepository siteConfigRepository;

    private final SiteConfigMapper siteConfigMapper;

    public SiteConfigQueryService(SiteConfigRepository siteConfigRepository, SiteConfigMapper siteConfigMapper) {
        this.siteConfigRepository = siteConfigRepository;
        this.siteConfigMapper = siteConfigMapper;
    }

    /**
     * Return a {@link List} of {@link SiteConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SiteConfigDTO> findByCriteria(SiteConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SiteConfig> queryWrapper = createQueryWrapper(criteria);
        return siteConfigMapper.toDto(siteConfigRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link SiteConfigDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<SiteConfigDTO> findByQueryWrapper(QueryWrapper<SiteConfig> queryWrapper, Page<SiteConfig> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return siteConfigRepository.selectPage(page, queryWrapper).convert(siteConfigMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link SiteConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<SiteConfigDTO> findByCriteria(SiteConfigCriteria criteria, Page<SiteConfig> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<SiteConfig> queryWrapper = createQueryWrapper(criteria);
        return siteConfigRepository.selectPage(page, queryWrapper).convert(siteConfigMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SiteConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<SiteConfig> queryWrapper = createQueryWrapper(criteria);
        return siteConfigRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link SiteConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<SiteConfigDTO> getOneByCriteria(SiteConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SiteConfig> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(siteConfigMapper.toDto(siteConfigRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return siteConfigRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, SiteConfigCriteria criteria) {
        return siteConfigRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, SiteConfigCriteria criteria) {
        return (List<T>) siteConfigRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link SiteConfigCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<SiteConfig> createQueryWrapper(SiteConfigCriteria criteria) {
        QueryWrapper<SiteConfig> queryWrapper = new DynamicJoinQueryWrapper<>(SiteConfig.class, null);
        if (criteria != null) {
            if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
                if (StringUtils.isNumeric(criteria.getJhiCommonSearchKeywords())) {
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildSpecification(new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())), "id")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildRangeSpecification(
                                (LongFilter) new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())),
                                "id"
                            )
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildRangeSpecification(
                                (LongFilter) new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())),
                                "created_by"
                            )
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildRangeSpecification(
                                (LongFilter) new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())),
                                "last_modified_by"
                            )
                        );
                } else {
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "title")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "remark")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "field_name")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "field_value")
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getTitle() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getTitle(), "title"));
                }
                if (criteria.getRemark() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getRemark(), "remark"));
                }
                if (criteria.getFieldName() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getFieldName(), "field_name")
                        );
                }
                if (criteria.getFieldValue() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getFieldValue(), "field_value")
                        );
                }
                if (criteria.getFieldType() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getFieldType(), "field_type"));
                }
                if (criteria.getRemovedAt() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getRemovedAt(), "removed_at")
                        );
                }
                if (criteria.getCreatedBy() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getCreatedBy(), "created_by")
                        );
                }
                if (criteria.getCreatedDate() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getCreatedDate(), "created_date")
                        );
                }
                if (criteria.getLastModifiedBy() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getLastModifiedBy(), "last_modified_by")
                        );
                }
                if (criteria.getLastModifiedDate() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getLastModifiedDate(), "last_modified_date")
                        );
                }
            }
        }
        return queryWrapper;
    }
}
