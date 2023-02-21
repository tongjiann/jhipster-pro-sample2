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
import com.mycompany.myapp.system.domain.Announcement;
import com.mycompany.myapp.system.repository.AnnouncementRepository;
import com.mycompany.myapp.system.service.criteria.AnnouncementCriteria;
import com.mycompany.myapp.system.service.dto.AnnouncementDTO;
import com.mycompany.myapp.system.service.mapper.AnnouncementMapper;
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
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * 用于对数据库中的{@link Announcement}实体执行复杂查询的Service。
 * 主要输入是一个{@link AnnouncementCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link AnnouncementDTO}列表{@link List} 或 {@link AnnouncementDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class AnnouncementQueryService implements QueryService<Announcement> {

    private final Logger log = LoggerFactory.getLogger(AnnouncementQueryService.class);

    private final DynamicJoinQueryWrapper<Announcement, Announcement> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        Announcement.class,
        null
    );

    private final AnnouncementRepository announcementRepository;

    private final AnnouncementMapper announcementMapper;

    public AnnouncementQueryService(AnnouncementRepository announcementRepository, AnnouncementMapper announcementMapper) {
        this.announcementRepository = announcementRepository;
        this.announcementMapper = announcementMapper;
    }

    /**
     * Return a {@link List} of {@link AnnouncementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AnnouncementDTO> findByCriteria(AnnouncementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<Announcement> queryWrapper = createQueryWrapper(criteria);
        return announcementMapper.toDto(announcementRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link AnnouncementDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<AnnouncementDTO> findByQueryWrapper(QueryWrapper<Announcement> queryWrapper, Page<Announcement> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return announcementRepository.selectPage(page, queryWrapper).convert(announcementMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link AnnouncementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<AnnouncementDTO> findByCriteria(AnnouncementCriteria criteria, Page<Announcement> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<Announcement> queryWrapper = createQueryWrapper(criteria);
        return announcementRepository.selectPage(page, queryWrapper).convert(announcementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnnouncementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<Announcement> queryWrapper = createQueryWrapper(criteria);
        return announcementRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link AnnouncementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<AnnouncementDTO> getOneByCriteria(AnnouncementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<Announcement> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(announcementMapper.toDto(announcementRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return announcementRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, AnnouncementCriteria criteria) {
        return announcementRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, AnnouncementCriteria criteria) {
        return (List<T>) announcementRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link AnnouncementCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<Announcement> createQueryWrapper(AnnouncementCriteria criteria) {
        QueryWrapper<Announcement> queryWrapper = new DynamicJoinQueryWrapper<>(Announcement.class, null);
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
                                "sender_id"
                            )
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildRangeSpecification(
                                (LongFilter) new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())),
                                "business_id"
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
                } else {
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "titile")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "open_page")
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getTitile() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getTitile(), "titile"));
                }
                if (criteria.getStartTime() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getStartTime(), "start_time")
                        );
                }
                if (criteria.getEndTime() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getEndTime(), "end_time"));
                }
                if (criteria.getSenderId() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getSenderId(), "sender_id"));
                }
                if (criteria.getPriority() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getPriority(), "priority"));
                }
                if (criteria.getCategory() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getCategory(), "category"));
                }
                if (criteria.getReceiverType() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildSpecification(criteria.getReceiverType(), "receiver_type")
                        );
                }
                if (criteria.getSendStatus() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getSendStatus(), "send_status"));
                }
                if (criteria.getSendTime() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getSendTime(), "send_time"));
                }
                if (criteria.getCancelTime() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getCancelTime(), "cancel_time")
                        );
                }
                if (criteria.getBusinessType() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildSpecification(criteria.getBusinessType(), "business_type")
                        );
                }
                if (criteria.getBusinessId() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getBusinessId(), "business_id")
                        );
                }
                if (criteria.getOpenType() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getOpenType(), "open_type"));
                }
                if (criteria.getOpenPage() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getOpenPage(), "open_page")
                        );
                }
                if (criteria.getReceiverIds() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildSpecification(criteria.getReceiverIds(), "receiver_ids")
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
