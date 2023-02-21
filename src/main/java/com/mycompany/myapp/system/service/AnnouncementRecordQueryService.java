package com.mycompany.myapp.system.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import com.diboot.core.vo.Pagination;
import com.mycompany.myapp.system.domain.*; // for static metamodels
import com.mycompany.myapp.system.domain.AnnouncementRecord;
import com.mycompany.myapp.system.repository.AnnouncementRecordRepository;
import com.mycompany.myapp.system.service.criteria.AnnouncementRecordCriteria;
import com.mycompany.myapp.system.service.dto.AnnouncementRecordDTO;
import com.mycompany.myapp.system.service.mapper.AnnouncementRecordMapper;
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
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * 用于对数据库中的{@link AnnouncementRecord}实体执行复杂查询的Service。
 * 主要输入是一个{@link AnnouncementRecordCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link AnnouncementRecordDTO}列表{@link List} 或 {@link AnnouncementRecordDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class AnnouncementRecordQueryService implements QueryService<AnnouncementRecord> {

    private final Logger log = LoggerFactory.getLogger(AnnouncementRecordQueryService.class);

    private final DynamicJoinQueryWrapper<AnnouncementRecord, AnnouncementRecord> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        AnnouncementRecord.class,
        null
    );

    private final AnnouncementRecordRepository announcementRecordRepository;

    private final AnnouncementRecordMapper announcementRecordMapper;

    public AnnouncementRecordQueryService(
        AnnouncementRecordRepository announcementRecordRepository,
        AnnouncementRecordMapper announcementRecordMapper
    ) {
        this.announcementRecordRepository = announcementRecordRepository;
        this.announcementRecordMapper = announcementRecordMapper;
    }

    /**
     * Return a {@link List} of {@link AnnouncementRecordDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AnnouncementRecordDTO> findByCriteria(AnnouncementRecordCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<AnnouncementRecord> queryWrapper = createQueryWrapper(criteria);
        return announcementRecordMapper.toDto(announcementRecordRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link AnnouncementRecordDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<AnnouncementRecordDTO> findByQueryWrapper(QueryWrapper<AnnouncementRecord> queryWrapper, Page<AnnouncementRecord> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return announcementRecordRepository.selectPage(page, queryWrapper).convert(announcementRecordMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link AnnouncementRecordDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<AnnouncementRecordDTO> findByCriteria(AnnouncementRecordCriteria criteria, Page<AnnouncementRecord> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<AnnouncementRecord> queryWrapper = createQueryWrapper(criteria);
        return announcementRecordRepository.selectPage(page, queryWrapper).convert(announcementRecordMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnnouncementRecordCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<AnnouncementRecord> queryWrapper = createQueryWrapper(criteria);
        return announcementRecordRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link AnnouncementRecordDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<AnnouncementRecordDTO> getOneByCriteria(AnnouncementRecordCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<AnnouncementRecord> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(announcementRecordMapper.toDto(announcementRecordRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return announcementRecordRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, AnnouncementRecordCriteria criteria) {
        return announcementRecordRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, AnnouncementRecordCriteria criteria) {
        return (List<T>) announcementRecordRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link AnnouncementRecordCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<AnnouncementRecord> createQueryWrapper(AnnouncementRecordCriteria criteria) {
        QueryWrapper<AnnouncementRecord> queryWrapper = new DynamicJoinQueryWrapper<>(AnnouncementRecord.class, null);
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
                                "annt_id"
                            )
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildRangeSpecification(
                                (LongFilter) new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())),
                                "user_id"
                            )
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildRangeSpecification(
                                (LongFilter) new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())),
                                "created_by"
                            )
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildRangeSpecification(
                                (LongFilter) new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())),
                                "last_modified_by"
                            )
                        );
                } else {}
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getAnntId() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getAnntId(), "annt_id"));
                }
                if (criteria.getUserId() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getUserId(), "user_id"));
                }
                if (criteria.getHasRead() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getHasRead(), "has_read"));
                }
                if (criteria.getReadTime() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getReadTime(), "read_time"));
                }
                if (criteria.getRemovedAt() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getRemovedAt(), "removed_at")
                        );
                }
                if (criteria.getCreatedBy() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getCreatedBy(), "created_by")
                        );
                }
                if (criteria.getCreatedDate() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getCreatedDate(), "created_date")
                        );
                }
                if (criteria.getLastModifiedBy() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getLastModifiedBy(), "last_modified_by")
                        );
                }
                if (criteria.getLastModifiedDate() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getLastModifiedDate(), "last_modified_date")
                        );
                }
            }
        }
        return queryWrapper;
    }
}
