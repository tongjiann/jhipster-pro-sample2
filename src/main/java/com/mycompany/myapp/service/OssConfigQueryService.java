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
import com.mycompany.myapp.domain.OssConfig;
import com.mycompany.myapp.repository.OssConfigRepository;
import com.mycompany.myapp.service.criteria.OssConfigCriteria;
import com.mycompany.myapp.service.dto.OssConfigDTO;
import com.mycompany.myapp.service.mapper.OssConfigMapper;
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
 * 用于对数据库中的{@link OssConfig}实体执行复杂查询的Service。
 * 主要输入是一个{@link OssConfigCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link OssConfigDTO}列表{@link List} 或 {@link OssConfigDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class OssConfigQueryService implements QueryService<OssConfig> {

    private final Logger log = LoggerFactory.getLogger(OssConfigQueryService.class);

    private final DynamicJoinQueryWrapper<OssConfig, OssConfig> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        OssConfig.class,
        null
    );

    private final OssConfigRepository ossConfigRepository;

    private final OssConfigMapper ossConfigMapper;

    public OssConfigQueryService(OssConfigRepository ossConfigRepository, OssConfigMapper ossConfigMapper) {
        this.ossConfigRepository = ossConfigRepository;
        this.ossConfigMapper = ossConfigMapper;
    }

    /**
     * Return a {@link List} of {@link OssConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OssConfigDTO> findByCriteria(OssConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<OssConfig> queryWrapper = createQueryWrapper(criteria);
        return ossConfigMapper.toDto(ossConfigRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link OssConfigDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<OssConfigDTO> findByQueryWrapper(QueryWrapper<OssConfig> queryWrapper, Page<OssConfig> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return ossConfigRepository.selectPage(page, queryWrapper).convert(ossConfigMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link OssConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<OssConfigDTO> findByCriteria(OssConfigCriteria criteria, Page<OssConfig> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<OssConfig> queryWrapper = createQueryWrapper(criteria);
        return ossConfigRepository.selectPage(page, queryWrapper).convert(ossConfigMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OssConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<OssConfig> queryWrapper = createQueryWrapper(criteria);
        return ossConfigRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link OssConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<OssConfigDTO> getOneByCriteria(OssConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<OssConfig> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(ossConfigMapper.toDto(ossConfigRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return ossConfigRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, OssConfigCriteria criteria) {
        return ossConfigRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, OssConfigCriteria criteria) {
        return (List<T>) ossConfigRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link OssConfigCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<OssConfig> createQueryWrapper(OssConfigCriteria criteria) {
        QueryWrapper<OssConfig> queryWrapper = new DynamicJoinQueryWrapper<>(OssConfig.class, null);
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "oss_code")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "endpoint")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "access_key")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "secret_key")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "bucket_name")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "app_id")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "region")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "remark")
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getProvider() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getProvider(), "provider"));
                }
                if (criteria.getOssCode() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getOssCode(), "oss_code"));
                }
                if (criteria.getEndpoint() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getEndpoint(), "endpoint"));
                }
                if (criteria.getAccessKey() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getAccessKey(), "access_key")
                        );
                }
                if (criteria.getSecretKey() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getSecretKey(), "secret_key")
                        );
                }
                if (criteria.getBucketName() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getBucketName(), "bucket_name")
                        );
                }
                if (criteria.getAppId() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getAppId(), "app_id"));
                }
                if (criteria.getRegion() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getRegion(), "region"));
                }
                if (criteria.getRemark() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getRemark(), "remark"));
                }
                if (criteria.getEnabled() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getEnabled(), "enabled"));
                }
                if (criteria.getRemovedAt() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getRemovedAt(), "removed_at")
                        );
                }
            }
        }
        return queryWrapper;
    }
}
