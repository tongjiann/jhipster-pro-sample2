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
import com.mycompany.myapp.domain.Department;
import com.mycompany.myapp.repository.DepartmentRepository;
import com.mycompany.myapp.service.criteria.DepartmentCriteria;
import com.mycompany.myapp.service.dto.DepartmentDTO;
import com.mycompany.myapp.service.mapper.DepartmentMapper;
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
 * 用于对数据库中的{@link Department}实体执行复杂查询的Service。
 * 主要输入是一个{@link DepartmentCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link DepartmentDTO}列表{@link List} 或 {@link DepartmentDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class DepartmentQueryService implements QueryService<Department> {

    private final Logger log = LoggerFactory.getLogger(DepartmentQueryService.class);

    private final DynamicJoinQueryWrapper<Department, Department> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        Department.class,
        null
    );

    private final DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper;

    public DepartmentQueryService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    /**
     * Return a {@link List} of {@link DepartmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepartmentDTO> findByCriteria(DepartmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<Department> queryWrapper = createQueryWrapper(criteria);
        return departmentMapper.toDto(departmentRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link DepartmentDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<DepartmentDTO> findByQueryWrapper(QueryWrapper<Department> queryWrapper, Page<Department> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return departmentRepository.selectPage(page, queryWrapper).convert(departmentMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link DepartmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<DepartmentDTO> findByCriteria(DepartmentCriteria criteria, Page<Department> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<Department> queryWrapper = createQueryWrapper(criteria);
        return departmentRepository.selectPage(page, queryWrapper).convert(departmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepartmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<Department> queryWrapper = createQueryWrapper(criteria);
        return departmentRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link DepartmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<DepartmentDTO> getOneByCriteria(DepartmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<Department> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(departmentMapper.toDto(departmentRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return departmentRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, DepartmentCriteria criteria) {
        return departmentRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, DepartmentCriteria criteria) {
        return (List<T>) departmentRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link DepartmentCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<Department> createQueryWrapper(DepartmentCriteria criteria) {
        QueryWrapper<Department> queryWrapper = new DynamicJoinQueryWrapper<>(Department.class, null);
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "address")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "phone_num")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "logo")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "contact")
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
                if (criteria.getAddress() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getAddress(), "address"));
                }
                if (criteria.getPhoneNum() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getPhoneNum(), "phone_num")
                        );
                }
                if (criteria.getLogo() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getLogo(), "logo"));
                }
                if (criteria.getContact() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getContact(), "contact"));
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
                if (criteria.getChildrenId() != null) {
                    // todo 未实现
                }
                if (criteria.getChildrenName() != null) {
                    // todo 未实现 one-to-many;[object Object];name
                }
                if (criteria.getAuthoritiesId() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getAuthoritiesId(), "jhi_authority_left_join.id")
                        );
                }
                if (criteria.getAuthoritiesName() != null) {
                    // todo 未实现 many-to-many;;name
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
                            buildStringSpecification(criteria.getParentName(), "department_left_join.name")
                        );
                }
                if (criteria.getUsersId() != null) {
                    // todo 未实现
                }
                if (criteria.getUsersFirstName() != null) {
                    // todo 未实现 one-to-many;;firstName
                }
                if (criteria.getDepartmentAuthoritiesId() != null) {
                    // todo 未实现
                }
                if (criteria.getDepartmentAuthoritiesName() != null) {
                    // todo 未实现 one-to-many;[object Object];name
                }
            }
        }
        return queryWrapper;
    }
}
