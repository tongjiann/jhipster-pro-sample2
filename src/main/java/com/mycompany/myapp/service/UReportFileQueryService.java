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
import com.mycompany.myapp.domain.UReportFile;
import com.mycompany.myapp.repository.UReportFileRepository;
import com.mycompany.myapp.service.criteria.UReportFileCriteria;
import com.mycompany.myapp.service.dto.UReportFileDTO;
import com.mycompany.myapp.service.mapper.UReportFileMapper;
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
 * 用于对数据库中的{@link UReportFile}实体执行复杂查询的Service。
 * 主要输入是一个{@link UReportFileCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link UReportFileDTO}列表{@link List} 或 {@link UReportFileDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class UReportFileQueryService implements QueryService<UReportFile> {

    private final Logger log = LoggerFactory.getLogger(UReportFileQueryService.class);

    private final DynamicJoinQueryWrapper<UReportFile, UReportFile> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        UReportFile.class,
        null
    );

    private final UReportFileRepository uReportFileRepository;

    private final UReportFileMapper uReportFileMapper;

    public UReportFileQueryService(UReportFileRepository uReportFileRepository, UReportFileMapper uReportFileMapper) {
        this.uReportFileRepository = uReportFileRepository;
        this.uReportFileMapper = uReportFileMapper;
    }

    /**
     * Return a {@link List} of {@link UReportFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UReportFileDTO> findByCriteria(UReportFileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<UReportFile> queryWrapper = createQueryWrapper(criteria);
        return uReportFileMapper.toDto(uReportFileRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link UReportFileDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<UReportFileDTO> findByQueryWrapper(QueryWrapper<UReportFile> queryWrapper, Page<UReportFile> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return uReportFileRepository.selectPage(page, queryWrapper).convert(uReportFileMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link UReportFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<UReportFileDTO> findByCriteria(UReportFileCriteria criteria, Page<UReportFile> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<UReportFile> queryWrapper = createQueryWrapper(criteria);
        return uReportFileRepository.selectPage(page, queryWrapper).convert(uReportFileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UReportFileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<UReportFile> queryWrapper = createQueryWrapper(criteria);
        return uReportFileRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link UReportFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<UReportFileDTO> getOneByCriteria(UReportFileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<UReportFile> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(uReportFileMapper.toDto(uReportFileRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return uReportFileRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, UReportFileCriteria criteria) {
        return uReportFileRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, UReportFileCriteria criteria) {
        return (List<T>) uReportFileRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link UReportFileCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<UReportFile> createQueryWrapper(UReportFileCriteria criteria) {
        QueryWrapper<UReportFile> queryWrapper = new DynamicJoinQueryWrapper<>(UReportFile.class, null);
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "name")
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
                if (criteria.getCreateAt() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getCreateAt(), "create_at"));
                }
                if (criteria.getUpdateAt() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getUpdateAt(), "update_at"));
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
