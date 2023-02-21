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
import com.mycompany.myapp.domain.Position;
import com.mycompany.myapp.repository.PositionRepository;
import com.mycompany.myapp.service.criteria.PositionCriteria;
import com.mycompany.myapp.service.dto.PositionDTO;
import com.mycompany.myapp.service.mapper.PositionMapper;
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
 * 用于对数据库中的{@link Position}实体执行复杂查询的Service。
 * 主要输入是一个{@link PositionCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link PositionDTO}列表{@link List} 或 {@link PositionDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class PositionQueryService implements QueryService<Position> {

    private final Logger log = LoggerFactory.getLogger(PositionQueryService.class);

    private final DynamicJoinQueryWrapper<Position, Position> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(Position.class, null);

    private final PositionRepository positionRepository;

    private final PositionMapper positionMapper;

    public PositionQueryService(PositionRepository positionRepository, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
    }

    /**
     * Return a {@link List} of {@link PositionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PositionDTO> findByCriteria(PositionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<Position> queryWrapper = createQueryWrapper(criteria);
        return positionMapper.toDto(positionRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link PositionDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<PositionDTO> findByQueryWrapper(QueryWrapper<Position> queryWrapper, Page<Position> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return positionRepository.selectPage(page, queryWrapper).convert(positionMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link PositionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<PositionDTO> findByCriteria(PositionCriteria criteria, Page<Position> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<Position> queryWrapper = createQueryWrapper(criteria);
        return positionRepository.selectPage(page, queryWrapper).convert(positionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PositionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<Position> queryWrapper = createQueryWrapper(criteria);
        return positionRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link PositionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<PositionDTO> getOneByCriteria(PositionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<Position> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(positionMapper.toDto(positionRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return positionRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, PositionCriteria criteria) {
        return positionRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, PositionCriteria criteria) {
        return (List<T>) positionRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link PositionCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<Position> createQueryWrapper(PositionCriteria criteria) {
        QueryWrapper<Position> queryWrapper = new DynamicJoinQueryWrapper<>(Position.class, null);
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
                                "sort_no"
                            )
                        );
                } else {
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "name")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "description")
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getCode() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getCode(), "code"));
                }
                if (criteria.getName() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getName(), "name"));
                }
                if (criteria.getSortNo() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getSortNo(), "sort_no"));
                }
                if (criteria.getDescription() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getDescription(), "description")
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
                if (criteria.getUsersId() != null) {
                    // todo 未实现
                }
                if (criteria.getUsersFirstName() != null) {
                    // todo 未实现 one-to-many;;firstName
                }
            }
        }
        return queryWrapper;
    }
}
