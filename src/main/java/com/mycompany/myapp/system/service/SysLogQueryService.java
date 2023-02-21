package com.mycompany.myapp.system.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import com.diboot.core.vo.Pagination;
import com.mycompany.myapp.domain.enumeration.LogType;
import com.mycompany.myapp.system.domain.*; // for static metamodels
import com.mycompany.myapp.system.domain.SysLog;
import com.mycompany.myapp.system.repository.SysLogRepository;
import com.mycompany.myapp.system.service.criteria.SysLogCriteria;
import com.mycompany.myapp.system.service.dto.SysLogDTO;
import com.mycompany.myapp.system.service.mapper.SysLogMapper;
import com.mycompany.myapp.util.CriteriaUtil;
import com.mycompany.myapp.util.mybatis.filter.QueryService;
import java.time.Instant;
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
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * 用于对数据库中的{@link SysLog}实体执行复杂查询的Service。
 * 主要输入是一个{@link SysLogCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link SysLogDTO}列表{@link List} 或 {@link SysLogDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class SysLogQueryService implements QueryService<SysLog> {

    private final Logger log = LoggerFactory.getLogger(SysLogQueryService.class);

    private final DynamicJoinQueryWrapper<SysLog, SysLog> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(SysLog.class, null);

    private final SysLogRepository sysLogRepository;

    private final SysLogMapper sysLogMapper;

    public SysLogQueryService(SysLogRepository sysLogRepository, SysLogMapper sysLogMapper) {
        this.sysLogRepository = sysLogRepository;
        this.sysLogMapper = sysLogMapper;
    }

    /**
     * Return a {@link List} of {@link SysLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SysLogDTO> findByCriteria(SysLogCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SysLog> queryWrapper = createQueryWrapper(criteria);
        return sysLogMapper.toDto(sysLogRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link SysLogDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<SysLogDTO> findByQueryWrapper(QueryWrapper<SysLog> queryWrapper, Page<SysLog> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return sysLogRepository.selectPage(page, queryWrapper).convert(sysLogMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link SysLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<SysLogDTO> findByCriteria(SysLogCriteria criteria, Page<SysLog> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<SysLog> queryWrapper = createQueryWrapper(criteria);
        return sysLogRepository.selectPage(page, queryWrapper).convert(sysLogMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SysLogCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<SysLog> queryWrapper = createQueryWrapper(criteria);
        return sysLogRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link SysLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<SysLogDTO> getOneByCriteria(SysLogCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SysLog> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(sysLogMapper.toDto(sysLogRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return sysLogRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, SysLogCriteria criteria) {
        return sysLogRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, SysLogCriteria criteria) {
        return (List<T>) sysLogRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    public Map<String, Object> logInfo() {
        Map<String, Object> result = new HashMap<>();
        //update-begin--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数
        // 获取一天的开始和结束时间
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date dayStart = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date dayEnd = calendar.getTime();
        // 获取系统访问记录
        SysLogCriteria sysLogCriteria = new SysLogCriteria();
        sysLogCriteria.logType().setEquals(LogType.LOGIN);
        Long totalVisitCount = countByCriteria(sysLogCriteria);
        result.put("totalVisitCount", totalVisitCount);
        sysLogCriteria.createdDate().setGreaterThanOrEqual(dayStart.toInstant()).setLessThan(dayEnd.toInstant());
        Long todayVisitCount = countByCriteria(sysLogCriteria);
        result.put("todayVisitCount", todayVisitCount);
        Long todayIp = countByFieldNameAndCriteria("ip", true, sysLogCriteria);
        result.put("todayIp", todayIp);
        return result;
    }

    public List<Map<String, Object>> countVisit() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date dayEnd = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date dayStart = calendar.getTime();
        String[] selections = new String[4];
        selections[0] = "count(*) as visit";
        selections[1] = "count(distinct(ip)) as ip";
        selections[2] = "DATE_FORMAT(created_date, '%Y-%m-%d') as day";
        selections[3] = "DATE_FORMAT(created_date, '%m-%d') as type";
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(selections);
        queryWrapper.ge("created_date", dayStart);
        queryWrapper.lt("created_date", dayEnd);
        queryWrapper.eq("log_type", LogType.LOGIN);
        queryWrapper.groupBy("day", "type");
        queryWrapper.orderBy(true, true, "day");
        return sysLogRepository.selectMaps(queryWrapper);
    }

    /**
     * Function to convert {@link SysLogCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<SysLog> createQueryWrapper(SysLogCriteria criteria) {
        QueryWrapper<SysLog> queryWrapper = new DynamicJoinQueryWrapper<>(SysLog.class, null);
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
                                "operate_type"
                            )
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildRangeSpecification(
                                (LongFilter) new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())),
                                "cost_time"
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "log_content")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "userid")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "username")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "ip")
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "request_url")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "request_type")
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getLogType() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildSpecification(criteria.getLogType(), "log_type"));
                }
                if (criteria.getLogContent() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getLogContent(), "log_content")
                        );
                }
                if (criteria.getOperateType() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getOperateType(), "operate_type")
                        );
                }
                if (criteria.getUserid() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getUserid(), "userid"));
                }
                if (criteria.getUsername() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getUsername(), "username"));
                }
                if (criteria.getIp() != null) {
                    queryWrapper = CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getIp(), "ip"));
                }
                if (criteria.getMethod() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getMethod(), "method"));
                }
                if (criteria.getRequestUrl() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getRequestUrl(), "request_url")
                        );
                }
                if (criteria.getRequestType() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getRequestType(), "request_type")
                        );
                }
                if (criteria.getCostTime() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getCostTime(), "cost_time"));
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
