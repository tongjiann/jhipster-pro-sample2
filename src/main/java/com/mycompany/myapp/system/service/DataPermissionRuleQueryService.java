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
import com.mycompany.myapp.system.domain.DataPermissionRule;
import com.mycompany.myapp.system.repository.DataPermissionRuleRepository;
import com.mycompany.myapp.system.service.criteria.DataPermissionRuleCriteria;
import com.mycompany.myapp.system.service.dto.DataPermissionRuleDTO;
import com.mycompany.myapp.system.service.mapper.DataPermissionRuleMapper;
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
 * 用于对数据库中的{@link DataPermissionRule}实体执行复杂查询的Service。
 * 主要输入是一个{@link DataPermissionRuleCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link DataPermissionRuleDTO}列表{@link List} 或 {@link DataPermissionRuleDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class DataPermissionRuleQueryService implements QueryService<DataPermissionRule> {

    private final Logger log = LoggerFactory.getLogger(DataPermissionRuleQueryService.class);

    private final DynamicJoinQueryWrapper<DataPermissionRule, DataPermissionRule> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        DataPermissionRule.class,
        null
    );

    private final DataPermissionRuleRepository dataPermissionRuleRepository;

    private final DataPermissionRuleMapper dataPermissionRuleMapper;

    public DataPermissionRuleQueryService(
        DataPermissionRuleRepository dataPermissionRuleRepository,
        DataPermissionRuleMapper dataPermissionRuleMapper
    ) {
        this.dataPermissionRuleRepository = dataPermissionRuleRepository;
        this.dataPermissionRuleMapper = dataPermissionRuleMapper;
    }

    /**
     * Return a {@link List} of {@link DataPermissionRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DataPermissionRuleDTO> findByCriteria(DataPermissionRuleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<DataPermissionRule> queryWrapper = createQueryWrapper(criteria);
        return dataPermissionRuleMapper.toDto(dataPermissionRuleRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link DataPermissionRuleDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<DataPermissionRuleDTO> findByQueryWrapper(QueryWrapper<DataPermissionRule> queryWrapper, Page<DataPermissionRule> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return dataPermissionRuleRepository.selectPage(page, queryWrapper).convert(dataPermissionRuleMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link DataPermissionRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<DataPermissionRuleDTO> findByCriteria(DataPermissionRuleCriteria criteria, Page<DataPermissionRule> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<DataPermissionRule> queryWrapper = createQueryWrapper(criteria);
        return dataPermissionRuleRepository.selectPage(page, queryWrapper).convert(dataPermissionRuleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DataPermissionRuleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<DataPermissionRule> queryWrapper = createQueryWrapper(criteria);
        return dataPermissionRuleRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link DataPermissionRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<DataPermissionRuleDTO> getOneByCriteria(DataPermissionRuleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<DataPermissionRule> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(dataPermissionRuleMapper.toDto(dataPermissionRuleRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return dataPermissionRuleRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, DataPermissionRuleCriteria criteria) {
        return dataPermissionRuleRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, DataPermissionRuleCriteria criteria) {
        return (List<T>) dataPermissionRuleRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link DataPermissionRuleCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<DataPermissionRule> createQueryWrapper(DataPermissionRuleCriteria criteria) {
        QueryWrapper<DataPermissionRule> queryWrapper = new DynamicJoinQueryWrapper<>(DataPermissionRule.class, null);
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "permission_id")
                        );
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "column")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "conditions")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "value")
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getPermissionId() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getPermissionId(), "permission_id")
                        );
                }
                if (criteria.getName() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getName(), "name"));
                }
                if (criteria.getColumn() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getColumn(), "column"));
                }
                if (criteria.getConditions() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getConditions(), "conditions")
                        );
                }
                if (criteria.getValue() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getValue(), "value"));
                }
                if (criteria.getDisabled() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getDisabled(), "disabled"));
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
