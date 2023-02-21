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
import com.mycompany.myapp.domain.ViewPermission;
import com.mycompany.myapp.repository.ViewPermissionRepository;
import com.mycompany.myapp.service.criteria.ViewPermissionCriteria;
import com.mycompany.myapp.service.dto.ViewPermissionDTO;
import com.mycompany.myapp.service.mapper.ViewPermissionMapper;
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
 * 用于对数据库中的{@link ViewPermission}实体执行复杂查询的Service。
 * 主要输入是一个{@link ViewPermissionCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link ViewPermissionDTO}列表{@link List} 或 {@link ViewPermissionDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class ViewPermissionQueryService implements QueryService<ViewPermission> {

    private final Logger log = LoggerFactory.getLogger(ViewPermissionQueryService.class);

    private final DynamicJoinQueryWrapper<ViewPermission, ViewPermission> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        ViewPermission.class,
        null
    );

    private final ViewPermissionRepository viewPermissionRepository;

    private final ViewPermissionMapper viewPermissionMapper;

    public ViewPermissionQueryService(ViewPermissionRepository viewPermissionRepository, ViewPermissionMapper viewPermissionMapper) {
        this.viewPermissionRepository = viewPermissionRepository;
        this.viewPermissionMapper = viewPermissionMapper;
    }

    /**
     * Return a {@link List} of {@link ViewPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ViewPermissionDTO> findByCriteria(ViewPermissionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<ViewPermission> queryWrapper = createQueryWrapper(criteria);
        return viewPermissionMapper.toDto(viewPermissionRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link ViewPermissionDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<ViewPermissionDTO> findByQueryWrapper(QueryWrapper<ViewPermission> queryWrapper, Page<ViewPermission> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return viewPermissionRepository.selectPage(page, queryWrapper).convert(viewPermissionMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link ViewPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<ViewPermissionDTO> findByCriteria(ViewPermissionCriteria criteria, Page<ViewPermission> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<ViewPermission> queryWrapper = createQueryWrapper(criteria);
        return viewPermissionRepository.selectPage(page, queryWrapper).convert(viewPermissionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ViewPermissionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<ViewPermission> queryWrapper = createQueryWrapper(criteria);
        return viewPermissionRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link ViewPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<ViewPermissionDTO> getOneByCriteria(ViewPermissionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<ViewPermission> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(viewPermissionMapper.toDto(viewPermissionRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return viewPermissionRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, ViewPermissionCriteria criteria) {
        return viewPermissionRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, ViewPermissionCriteria criteria) {
        return (List<T>) viewPermissionRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link ViewPermissionCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<ViewPermission> createQueryWrapper(ViewPermissionCriteria criteria) {
        QueryWrapper<ViewPermission> queryWrapper = new DynamicJoinQueryWrapper<>(ViewPermission.class, null);
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "text")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "i18n")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "link")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "external_link")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "icon")
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
                            buildStringSpecification(
                                new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()),
                                "api_permission_codes"
                            )
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(
                                new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()),
                                "component_file"
                            )
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "redirect")
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getText() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getText(), "text"));
                }
                if (criteria.geti18n() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.geti18n(), "i18n"));
                }
                if (criteria.getGroup() != null) {
                    queryWrapper = CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getGroup(), "group"));
                }
                if (criteria.getLink() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getLink(), "link"));
                }
                if (criteria.getExternalLink() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getExternalLink(), "external_link")
                        );
                }
                if (criteria.getTarget() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getTarget(), "target"));
                }
                if (criteria.getIcon() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getIcon(), "icon"));
                }
                if (criteria.getDisabled() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getDisabled(), "disabled"));
                }
                if (criteria.getHide() != null) {
                    queryWrapper = CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getHide(), "hide"));
                }
                if (criteria.getHideInBreadcrumb() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildSpecification(criteria.getHideInBreadcrumb(), "hide_in_breadcrumb")
                        );
                }
                if (criteria.getShortcut() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getShortcut(), "shortcut"));
                }
                if (criteria.getShortcutRoot() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildSpecification(criteria.getShortcutRoot(), "shortcut_root")
                        );
                }
                if (criteria.getReuse() != null) {
                    queryWrapper = CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getReuse(), "reuse"));
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
                if (criteria.getOrder() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getOrder(), "order"));
                }
                if (criteria.getApiPermissionCodes() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getApiPermissionCodes(), "api_permission_codes")
                        );
                }
                if (criteria.getComponentFile() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getComponentFile(), "component_file")
                        );
                }
                if (criteria.getRedirect() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getRedirect(), "redirect"));
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
                if (criteria.getChildrenText() != null) {
                    // todo 未实现 one-to-many;[object Object];text
                }
                if (criteria.getParentId() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getParentId(), "parent_id"));
                }
                if (criteria.getParentText() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getParentText(), "view_permission_left_join.text")
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
