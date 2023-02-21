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
import com.mycompany.myapp.domain.DepartmentAuthority;
import com.mycompany.myapp.repository.DepartmentAuthorityRepository;
import com.mycompany.myapp.service.criteria.DepartmentAuthorityCriteria;
import com.mycompany.myapp.service.dto.DepartmentAuthorityDTO;
import com.mycompany.myapp.service.mapper.DepartmentAuthorityMapper;
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
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * 用于对数据库中的{@link DepartmentAuthority}实体执行复杂查询的Service。
 * 主要输入是一个{@link DepartmentAuthorityCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link DepartmentAuthorityDTO}列表{@link List} 或 {@link DepartmentAuthorityDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class DepartmentAuthorityQueryService implements QueryService<DepartmentAuthority> {

    private final Logger log = LoggerFactory.getLogger(DepartmentAuthorityQueryService.class);

    private final DynamicJoinQueryWrapper<DepartmentAuthority, DepartmentAuthority> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        DepartmentAuthority.class,
        null
    );

    private final DepartmentAuthorityRepository departmentAuthorityRepository;

    private final DepartmentAuthorityMapper departmentAuthorityMapper;

    public DepartmentAuthorityQueryService(
        DepartmentAuthorityRepository departmentAuthorityRepository,
        DepartmentAuthorityMapper departmentAuthorityMapper
    ) {
        this.departmentAuthorityRepository = departmentAuthorityRepository;
        this.departmentAuthorityMapper = departmentAuthorityMapper;
    }

    /**
     * Return a {@link List} of {@link DepartmentAuthorityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepartmentAuthorityDTO> findByCriteria(DepartmentAuthorityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<DepartmentAuthority> queryWrapper = createQueryWrapper(criteria);
        return departmentAuthorityMapper.toDto(departmentAuthorityRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link DepartmentAuthorityDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<DepartmentAuthorityDTO> findByQueryWrapper(
        QueryWrapper<DepartmentAuthority> queryWrapper,
        Page<DepartmentAuthority> page
    ) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return departmentAuthorityRepository.selectPage(page, queryWrapper).convert(departmentAuthorityMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link DepartmentAuthorityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<DepartmentAuthorityDTO> findByCriteria(DepartmentAuthorityCriteria criteria, Page<DepartmentAuthority> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<DepartmentAuthority> queryWrapper = createQueryWrapper(criteria);
        return departmentAuthorityRepository.selectPage(page, queryWrapper).convert(departmentAuthorityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepartmentAuthorityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<DepartmentAuthority> queryWrapper = createQueryWrapper(criteria);
        return departmentAuthorityRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link DepartmentAuthorityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<DepartmentAuthorityDTO> getOneByCriteria(DepartmentAuthorityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<DepartmentAuthority> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(departmentAuthorityMapper.toDto(departmentAuthorityRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return departmentAuthorityRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, DepartmentAuthorityCriteria criteria) {
        return departmentAuthorityRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, DepartmentAuthorityCriteria criteria) {
        return (List<T>) departmentAuthorityRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link DepartmentAuthorityCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<DepartmentAuthority> createQueryWrapper(DepartmentAuthorityCriteria criteria) {
        QueryWrapper<DepartmentAuthority> queryWrapper = new DynamicJoinQueryWrapper<>(DepartmentAuthority.class, null);
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
                                "create_user_id"
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "description")
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
                if (criteria.getDescription() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getDescription(), "description")
                        );
                }
                if (criteria.getCreateUserId() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getCreateUserId(), "create_user_id")
                        );
                }
                if (criteria.getCreateTime() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getCreateTime(), "create_time")
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
                if (criteria.getApiPermissionsId() != null) {
                    // todo 未实现
                }
                if (criteria.getApiPermissionsName() != null) {
                    // todo 未实现 one-to-many;[object Object];name
                }
                if (criteria.getViewPermissionsId() != null) {
                    // todo 未实现
                }
                if (criteria.getViewPermissionsText() != null) {
                    // todo 未实现 one-to-many;[object Object];text
                }
                if (criteria.getDepartmentId() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getDepartmentId(), "department_id")
                        );
                }
                if (criteria.getDepartmentName() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getDepartmentName(), "department_left_join.name")
                        );
                }
            }
        }
        return queryWrapper;
    }
}
