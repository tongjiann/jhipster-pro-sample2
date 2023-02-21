package com.mycompany.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import com.diboot.core.vo.Pagination;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.DataDictionary;
import com.mycompany.myapp.repository.DataDictionaryRepository;
import com.mycompany.myapp.service.criteria.DataDictionaryCriteria;
import com.mycompany.myapp.service.dto.DataDictionaryDTO;
import com.mycompany.myapp.service.mapper.DataDictionaryMapper;
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
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * 用于对数据库中的{@link DataDictionary}实体执行复杂查询的Service。
 * 主要输入是一个{@link DataDictionaryCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link DataDictionaryDTO}列表{@link List} 或 {@link DataDictionaryDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class DataDictionaryQueryService implements QueryService<DataDictionary> {

    private final Logger log = LoggerFactory.getLogger(DataDictionaryQueryService.class);

    private final DynamicJoinQueryWrapper<DataDictionary, DataDictionary> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        DataDictionary.class,
        null
    );

    private final DataDictionaryRepository dataDictionaryRepository;

    private final DataDictionaryMapper dataDictionaryMapper;

    public DataDictionaryQueryService(DataDictionaryRepository dataDictionaryRepository, DataDictionaryMapper dataDictionaryMapper) {
        this.dataDictionaryRepository = dataDictionaryRepository;
        this.dataDictionaryMapper = dataDictionaryMapper;
    }

    /**
     * Return a {@link List} of {@link DataDictionaryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DataDictionaryDTO> findByCriteria(DataDictionaryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<DataDictionary> queryWrapper = createQueryWrapper(criteria);
        return dataDictionaryMapper.toDto(dataDictionaryRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link DataDictionaryDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<DataDictionaryDTO> findByQueryWrapper(QueryWrapper<DataDictionary> queryWrapper, Page<DataDictionary> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return dataDictionaryRepository.selectPage(page, queryWrapper).convert(dataDictionaryMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link DataDictionaryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<DataDictionaryDTO> findByCriteria(DataDictionaryCriteria criteria, Page<DataDictionary> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<DataDictionary> queryWrapper = createQueryWrapper(criteria);
        return dataDictionaryRepository.selectPage(page, queryWrapper).convert(dataDictionaryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DataDictionaryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<DataDictionary> queryWrapper = createQueryWrapper(criteria);
        return dataDictionaryRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link DataDictionaryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<DataDictionaryDTO> getOneByCriteria(DataDictionaryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<DataDictionary> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(dataDictionaryMapper.toDto(dataDictionaryRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return dataDictionaryRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, DataDictionaryCriteria criteria) {
        return dataDictionaryRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, DataDictionaryCriteria criteria) {
        return (List<T>) dataDictionaryRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link DataDictionaryCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<DataDictionary> createQueryWrapper(DataDictionaryCriteria criteria) {
        QueryWrapper<DataDictionary> queryWrapper = new DynamicJoinQueryWrapper<>(DataDictionary.class, null);
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
                                (IntegerFilter) new IntegerFilter().setEquals(Integer.valueOf(criteria.getJhiCommonSearchKeywords())),
                                "sort_order"
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "title")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "value")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "description")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "font_color")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(
                                new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()),
                                "background_color"
                            )
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
                if (criteria.getTitle() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getTitle(), "title"));
                }
                if (criteria.getValue() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getValue(), "value"));
                }
                if (criteria.getDescription() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getDescription(), "description")
                        );
                }
                if (criteria.getSortOrder() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getSortOrder(), "sort_order")
                        );
                }
                if (criteria.getDisabled() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getDisabled(), "disabled"));
                }
                if (criteria.getFontColor() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getFontColor(), "font_color")
                        );
                }
                if (criteria.getValueType() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getValueType(), "value_type"));
                }
                if (criteria.getBackgroundColor() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getBackgroundColor(), "background_color")
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
                if (criteria.getChildrenId() != null) {
                    // todo 未实现
                }
                if (criteria.getChildrenName() != null) {
                    // todo 未实现 one-to-many;[object Object];name
                }
                if (criteria.getParentId() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getParentId(), "parent_id"));
                }
                if (criteria.getParentName() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getParentName(), "data_dictionary_left_join.name")
                        );
                }
            }
        }
        return queryWrapper;
    }
}
