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
import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.repository.AuthorityRepository;
import com.mycompany.myapp.service.criteria.AuthorityCriteria;
import com.mycompany.myapp.service.dto.AuthorityDTO;
import com.mycompany.myapp.service.mapper.AuthorityMapper;
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
 * 用于对数据库中的{@link Authority}实体执行复杂查询的Service。
 * 主要输入是一个{@link AuthorityCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link AuthorityDTO}列表{@link List} 或 {@link AuthorityDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class AuthorityQueryService implements QueryService<Authority> {

    private final Logger log = LoggerFactory.getLogger(AuthorityQueryService.class);

    private final DynamicJoinQueryWrapper<Authority, Authority> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        Authority.class,
        null
    );

    private final AuthorityRepository authorityRepository;

    private final AuthorityMapper authorityMapper;

    public AuthorityQueryService(AuthorityRepository authorityRepository, AuthorityMapper authorityMapper) {
        this.authorityRepository = authorityRepository;
        this.authorityMapper = authorityMapper;
    }

    /**
     * Return a {@link List} of {@link AuthorityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AuthorityDTO> findByCriteria(AuthorityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<Authority> queryWrapper = createQueryWrapper(criteria);
        return authorityMapper.toDto(authorityRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link AuthorityDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<AuthorityDTO> findByQueryWrapper(QueryWrapper<Authority> queryWrapper, Page<Authority> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return authorityRepository.selectPage(page, queryWrapper).convert(authorityMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link AuthorityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<AuthorityDTO> findByCriteria(AuthorityCriteria criteria, Page<Authority> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<Authority> queryWrapper = createQueryWrapper(criteria);
        return authorityRepository.selectPage(page, queryWrapper).convert(authorityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AuthorityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<Authority> queryWrapper = createQueryWrapper(criteria);
        return authorityRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link AuthorityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<AuthorityDTO> getOneByCriteria(AuthorityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<Authority> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(authorityMapper.toDto(authorityRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return authorityRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, AuthorityCriteria criteria) {
        return authorityRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, AuthorityCriteria criteria) {
        return (List<T>) authorityRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link AuthorityCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<Authority> createQueryWrapper(AuthorityCriteria criteria) {
        QueryWrapper<Authority> queryWrapper = new DynamicJoinQueryWrapper<>(Authority.class, null);
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
                                "order"
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "info")
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
                if (criteria.getInfo() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getInfo(), "info"));
                }
                if (criteria.getOrder() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getOrder(), "order"));
                }
                if (criteria.getDisplay() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getDisplay(), "display"));
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
                    // todo 未实现 one-to-many;;name
                }
                if (criteria.getParentId() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getParentId(), "parent_id"));
                }
                if (criteria.getParentName() != null) {
                    // todo 未实现 many-to-one;;name
                }
                if (criteria.getApiPermissionsId() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getApiPermissionsId(), "api_permission_left_join.id")
                        );
                }
                if (criteria.getApiPermissionsName() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getApiPermissionsName(), "api_permission_left_join.name")
                        );
                }
                if (criteria.getViewPermissionsId() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getViewPermissionsId(), "view_permission_left_join.id")
                        );
                }
                if (criteria.getViewPermissionsText() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getViewPermissionsText(), "view_permission_left_join.text")
                        );
                }
            }
        }
        return queryWrapper;
    }
}
