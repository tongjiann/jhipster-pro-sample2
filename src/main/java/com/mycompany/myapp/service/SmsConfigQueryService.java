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
import com.mycompany.myapp.domain.SmsConfig;
import com.mycompany.myapp.repository.SmsConfigRepository;
import com.mycompany.myapp.service.criteria.SmsConfigCriteria;
import com.mycompany.myapp.service.dto.SmsConfigDTO;
import com.mycompany.myapp.service.mapper.SmsConfigMapper;
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
 * 用于对数据库中的{@link SmsConfig}实体执行复杂查询的Service。
 * 主要输入是一个{@link SmsConfigCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link SmsConfigDTO}列表{@link List} 或 {@link SmsConfigDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class SmsConfigQueryService implements QueryService<SmsConfig> {

    private final Logger log = LoggerFactory.getLogger(SmsConfigQueryService.class);

    private final DynamicJoinQueryWrapper<SmsConfig, SmsConfig> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        SmsConfig.class,
        null
    );

    private final SmsConfigRepository smsConfigRepository;

    private final SmsConfigMapper smsConfigMapper;

    public SmsConfigQueryService(SmsConfigRepository smsConfigRepository, SmsConfigMapper smsConfigMapper) {
        this.smsConfigRepository = smsConfigRepository;
        this.smsConfigMapper = smsConfigMapper;
    }

    /**
     * Return a {@link List} of {@link SmsConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SmsConfigDTO> findByCriteria(SmsConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SmsConfig> queryWrapper = createQueryWrapper(criteria);
        return smsConfigMapper.toDto(smsConfigRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link SmsConfigDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<SmsConfigDTO> findByQueryWrapper(QueryWrapper<SmsConfig> queryWrapper, Page<SmsConfig> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return smsConfigRepository.selectPage(page, queryWrapper).convert(smsConfigMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link SmsConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<SmsConfigDTO> findByCriteria(SmsConfigCriteria criteria, Page<SmsConfig> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<SmsConfig> queryWrapper = createQueryWrapper(criteria);
        return smsConfigRepository.selectPage(page, queryWrapper).convert(smsConfigMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SmsConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<SmsConfig> queryWrapper = createQueryWrapper(criteria);
        return smsConfigRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link SmsConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<SmsConfigDTO> getOneByCriteria(SmsConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SmsConfig> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(smsConfigMapper.toDto(smsConfigRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return smsConfigRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, SmsConfigCriteria criteria) {
        return smsConfigRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, SmsConfigCriteria criteria) {
        return (List<T>) smsConfigRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link SmsConfigCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<SmsConfig> createQueryWrapper(SmsConfigCriteria criteria) {
        QueryWrapper<SmsConfig> queryWrapper = new DynamicJoinQueryWrapper<>(SmsConfig.class, null);
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "sms_code")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "template_id")
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "region_id")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "sign_name")
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
                if (criteria.getSmsCode() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getSmsCode(), "sms_code"));
                }
                if (criteria.getTemplateId() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getTemplateId(), "template_id")
                        );
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
                if (criteria.getRegionId() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getRegionId(), "region_id")
                        );
                }
                if (criteria.getSignName() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getSignName(), "sign_name")
                        );
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
