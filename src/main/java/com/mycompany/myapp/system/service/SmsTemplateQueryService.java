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
import com.mycompany.myapp.system.domain.SmsTemplate;
import com.mycompany.myapp.system.repository.SmsTemplateRepository;
import com.mycompany.myapp.system.service.criteria.SmsTemplateCriteria;
import com.mycompany.myapp.system.service.dto.SmsTemplateDTO;
import com.mycompany.myapp.system.service.mapper.SmsTemplateMapper;
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
 * 用于对数据库中的{@link SmsTemplate}实体执行复杂查询的Service。
 * 主要输入是一个{@link SmsTemplateCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link SmsTemplateDTO}列表{@link List} 或 {@link SmsTemplateDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class SmsTemplateQueryService implements QueryService<SmsTemplate> {

    private final Logger log = LoggerFactory.getLogger(SmsTemplateQueryService.class);

    private final DynamicJoinQueryWrapper<SmsTemplate, SmsTemplate> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        SmsTemplate.class,
        null
    );

    private final SmsTemplateRepository smsTemplateRepository;

    private final SmsTemplateMapper smsTemplateMapper;

    public SmsTemplateQueryService(SmsTemplateRepository smsTemplateRepository, SmsTemplateMapper smsTemplateMapper) {
        this.smsTemplateRepository = smsTemplateRepository;
        this.smsTemplateMapper = smsTemplateMapper;
    }

    /**
     * Return a {@link List} of {@link SmsTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SmsTemplateDTO> findByCriteria(SmsTemplateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SmsTemplate> queryWrapper = createQueryWrapper(criteria);
        return smsTemplateMapper.toDto(smsTemplateRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link SmsTemplateDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<SmsTemplateDTO> findByQueryWrapper(QueryWrapper<SmsTemplate> queryWrapper, Page<SmsTemplate> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return smsTemplateRepository.selectPage(page, queryWrapper).convert(smsTemplateMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link SmsTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<SmsTemplateDTO> findByCriteria(SmsTemplateCriteria criteria, Page<SmsTemplate> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<SmsTemplate> queryWrapper = createQueryWrapper(criteria);
        return smsTemplateRepository.selectPage(page, queryWrapper).convert(smsTemplateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SmsTemplateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<SmsTemplate> queryWrapper = createQueryWrapper(criteria);
        return smsTemplateRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link SmsTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<SmsTemplateDTO> getOneByCriteria(SmsTemplateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SmsTemplate> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(smsTemplateMapper.toDto(smsTemplateRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return smsTemplateRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, SmsTemplateCriteria criteria) {
        return smsTemplateRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, SmsTemplateCriteria criteria) {
        return (List<T>) smsTemplateRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link SmsTemplateCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<SmsTemplate> createQueryWrapper(SmsTemplateCriteria criteria) {
        QueryWrapper<SmsTemplate> queryWrapper = new DynamicJoinQueryWrapper<>(SmsTemplate.class, null);
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "name")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "code")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "content")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "test_json")
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getName() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getName(), "name"));
                }
                if (criteria.getCode() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getCode(), "code"));
                }
                if (criteria.getType() != null) {
                    queryWrapper = CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getType(), "type"));
                }
                if (criteria.getContent() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getContent(), "content"));
                }
                if (criteria.getTestJson() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getTestJson(), "test_json")
                        );
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
