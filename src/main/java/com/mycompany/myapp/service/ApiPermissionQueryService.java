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
import com.mycompany.myapp.domain.ApiPermission;
import com.mycompany.myapp.repository.ApiPermissionRepository;
import com.mycompany.myapp.service.criteria.ApiPermissionCriteria;
import com.mycompany.myapp.service.dto.ApiPermissionDTO;
import com.mycompany.myapp.service.mapper.ApiPermissionMapper;
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

/**
 * 用于对数据库中的{@link ApiPermission}实体执行复杂查询的Service。
 * 主要输入是一个{@link ApiPermissionCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link ApiPermissionDTO}列表{@link List} 或 {@link ApiPermissionDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class ApiPermissionQueryService implements QueryService<ApiPermission> {

    private final Logger log = LoggerFactory.getLogger(ApiPermissionQueryService.class);

    private final DynamicJoinQueryWrapper<ApiPermission, ApiPermission> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        ApiPermission.class,
        null
    );

    private final ApiPermissionRepository apiPermissionRepository;

    private final ApiPermissionMapper apiPermissionMapper;

    public ApiPermissionQueryService(ApiPermissionRepository apiPermissionRepository, ApiPermissionMapper apiPermissionMapper) {
        this.apiPermissionRepository = apiPermissionRepository;
        this.apiPermissionMapper = apiPermissionMapper;
    }

    /**
     * Return a {@link List} of {@link ApiPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ApiPermissionDTO> findByCriteria(ApiPermissionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<ApiPermission> queryWrapper = createQueryWrapper(criteria);
        return apiPermissionMapper.toDto(apiPermissionRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link ApiPermissionDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<ApiPermissionDTO> findByQueryWrapper(QueryWrapper<ApiPermission> queryWrapper, Page<ApiPermission> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return apiPermissionRepository.selectPage(page, queryWrapper).convert(apiPermissionMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link ApiPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<ApiPermissionDTO> findByCriteria(ApiPermissionCriteria criteria, Page<ApiPermission> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<ApiPermission> queryWrapper = createQueryWrapper(criteria);
        return apiPermissionRepository.selectPage(page, queryWrapper).convert(apiPermissionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ApiPermissionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<ApiPermission> queryWrapper = createQueryWrapper(criteria);
        return apiPermissionRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link ApiPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<ApiPermissionDTO> getOneByCriteria(ApiPermissionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<ApiPermission> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(apiPermissionMapper.toDto(apiPermissionRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return apiPermissionRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, ApiPermissionCriteria criteria) {
        return apiPermissionRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, ApiPermissionCriteria criteria) {
        return (List<T>) apiPermissionRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link ApiPermissionCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<ApiPermission> createQueryWrapper(ApiPermissionCriteria criteria) {
        QueryWrapper<ApiPermission> queryWrapper = new DynamicJoinQueryWrapper<>(ApiPermission.class, null);
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
                } else {
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "service_name")
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "code")
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "method")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "url")
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getServiceName() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getServiceName(), "service_name")
                        );
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
                if (criteria.getType() != null) {
                    queryWrapper = CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getType(), "type"));
                }
                if (criteria.getMethod() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getMethod(), "method"));
                }
                if (criteria.getUrl() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getUrl(), "url"));
                }
                if (criteria.getStatus() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getStatus(), "status"));
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
                            buildStringSpecification(criteria.getParentName(), "api_permission_left_join.name")
                        );
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
            }
        }
        return queryWrapper;
    }
}
