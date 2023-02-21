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
import com.mycompany.myapp.domain.RegionCode;
import com.mycompany.myapp.repository.RegionCodeRepository;
import com.mycompany.myapp.service.criteria.RegionCodeCriteria;
import com.mycompany.myapp.service.dto.RegionCodeDTO;
import com.mycompany.myapp.service.mapper.RegionCodeMapper;
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
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * 用于对数据库中的{@link RegionCode}实体执行复杂查询的Service。
 * 主要输入是一个{@link RegionCodeCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link RegionCodeDTO}列表{@link List} 或 {@link RegionCodeDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class RegionCodeQueryService implements QueryService<RegionCode> {

    private final Logger log = LoggerFactory.getLogger(RegionCodeQueryService.class);

    private final DynamicJoinQueryWrapper<RegionCode, RegionCode> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        RegionCode.class,
        null
    );

    private final RegionCodeRepository regionCodeRepository;

    private final RegionCodeMapper regionCodeMapper;

    public RegionCodeQueryService(RegionCodeRepository regionCodeRepository, RegionCodeMapper regionCodeMapper) {
        this.regionCodeRepository = regionCodeRepository;
        this.regionCodeMapper = regionCodeMapper;
    }

    /**
     * Return a {@link List} of {@link RegionCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RegionCodeDTO> findByCriteria(RegionCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<RegionCode> queryWrapper = createQueryWrapper(criteria);
        return regionCodeMapper.toDto(regionCodeRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link RegionCodeDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<RegionCodeDTO> findByQueryWrapper(QueryWrapper<RegionCode> queryWrapper, Page<RegionCode> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return regionCodeRepository.selectPage(page, queryWrapper).convert(regionCodeMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link RegionCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<RegionCodeDTO> findByCriteria(RegionCodeCriteria criteria, Page<RegionCode> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<RegionCode> queryWrapper = createQueryWrapper(criteria);
        return regionCodeRepository.selectPage(page, queryWrapper).convert(regionCodeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RegionCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<RegionCode> queryWrapper = createQueryWrapper(criteria);
        return regionCodeRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link RegionCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<RegionCodeDTO> getOneByCriteria(RegionCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<RegionCode> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(regionCodeMapper.toDto(regionCodeRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return regionCodeRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, RegionCodeCriteria criteria) {
        return regionCodeRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, RegionCodeCriteria criteria) {
        return (List<T>) regionCodeRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link RegionCodeCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<RegionCode> createQueryWrapper(RegionCodeCriteria criteria) {
        QueryWrapper<RegionCode> queryWrapper = new DynamicJoinQueryWrapper<>(RegionCode.class, null);
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
                                (DoubleFilter) new DoubleFilter().setEquals(Double.valueOf(criteria.getJhiCommonSearchKeywords())),
                                "lng"
                            )
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildRangeSpecification(
                                (DoubleFilter) new DoubleFilter().setEquals(Double.valueOf(criteria.getJhiCommonSearchKeywords())),
                                "lat"
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "area_code")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "city_code")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "merger_name")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "short_name")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "zip_code")
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
                if (criteria.getAreaCode() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getAreaCode(), "area_code")
                        );
                }
                if (criteria.getCityCode() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getCityCode(), "city_code")
                        );
                }
                if (criteria.getMergerName() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getMergerName(), "merger_name")
                        );
                }
                if (criteria.getShortName() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getShortName(), "short_name")
                        );
                }
                if (criteria.getZipCode() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getZipCode(), "zip_code"));
                }
                if (criteria.getLevel() != null) {
                    queryWrapper = CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getLevel(), "level"));
                }
                if (criteria.getLng() != null) {
                    queryWrapper = CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getLng(), "lng"));
                }
                if (criteria.getLat() != null) {
                    queryWrapper = CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getLat(), "lat"));
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
                            buildStringSpecification(criteria.getParentName(), "region_code_left_join.name")
                        );
                }
            }
        }
        return queryWrapper;
    }
}
