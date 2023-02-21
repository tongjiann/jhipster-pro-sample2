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
import com.mycompany.myapp.domain.UploadFile;
import com.mycompany.myapp.repository.UploadFileRepository;
import com.mycompany.myapp.service.criteria.UploadFileCriteria;
import com.mycompany.myapp.service.dto.UploadFileDTO;
import com.mycompany.myapp.service.mapper.UploadFileMapper;
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
 * 用于对数据库中的{@link UploadFile}实体执行复杂查询的Service。
 * 主要输入是一个{@link UploadFileCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link UploadFileDTO}列表{@link List} 或 {@link UploadFileDTO} 的分页列表 {@link Page}。
 */
@Service
@Transactional(readOnly = true)
public class UploadFileQueryService implements QueryService<UploadFile> {

    private final Logger log = LoggerFactory.getLogger(UploadFileQueryService.class);

    private final DynamicJoinQueryWrapper<UploadFile, UploadFile> dynamicJoinQueryWrapper = new DynamicJoinQueryWrapper<>(
        UploadFile.class,
        null
    );

    private final UploadFileRepository uploadFileRepository;

    private final UploadFileMapper uploadFileMapper;

    public UploadFileQueryService(UploadFileRepository uploadFileRepository, UploadFileMapper uploadFileMapper) {
        this.uploadFileRepository = uploadFileRepository;
        this.uploadFileMapper = uploadFileMapper;
    }

    /**
     * Return a {@link List} of {@link UploadFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UploadFileDTO> findByCriteria(UploadFileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<UploadFile> queryWrapper = createQueryWrapper(criteria);
        return uploadFileMapper.toDto(uploadFileRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link UploadFileDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<UploadFileDTO> findByQueryWrapper(QueryWrapper<UploadFile> queryWrapper, Page<UploadFile> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return uploadFileRepository.selectPage(page, queryWrapper).convert(uploadFileMapper::toDto);
    }

    /**
     * Return a {@link IPage} of {@link UploadFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public IPage<UploadFileDTO> findByCriteria(UploadFileCriteria criteria, Page<UploadFile> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<UploadFile> queryWrapper = createQueryWrapper(criteria);
        return uploadFileRepository.selectPage(page, queryWrapper).convert(uploadFileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UploadFileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<UploadFile> queryWrapper = createQueryWrapper(criteria);
        return uploadFileRepository.selectCount(queryWrapper);
    }

    /**
     * Return a {@link UploadFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entity.
     */
    @Transactional(readOnly = true)
    public Optional<UploadFileDTO> getOneByCriteria(UploadFileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<UploadFile> queryWrapper = createQueryWrapper(criteria);
        return Optional.of(uploadFileMapper.toDto(uploadFileRepository.selectOne(queryWrapper)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByQueryWrapper(QueryWrapper queryWrapper) {
        log.debug("count by queryWrapper : {}", queryWrapper);
        return uploadFileRepository.selectCount(queryWrapper);
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, UploadFileCriteria criteria) {
        return uploadFileRepository.selectCount(createQueryWrapper(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, UploadFileCriteria criteria) {
        return (List<T>) uploadFileRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    /**
     * Function to convert {@link UploadFileCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    protected QueryWrapper<UploadFile> createQueryWrapper(UploadFileCriteria criteria) {
        QueryWrapper<UploadFile> queryWrapper = new DynamicJoinQueryWrapper<>(UploadFile.class, null);
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
                                "file_size"
                            )
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildRangeSpecification(
                                (LongFilter) new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())),
                                "reference_count"
                            )
                        );
                } else {
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "full_name")
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
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "ext")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "type")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "url")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "path")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "folder")
                        );
                    queryWrapper =
                        CriteriaUtil.build(
                            true,
                            queryWrapper,
                            buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "entity_name")
                        );
                }
            } else {
                if (criteria.getId() != null) {
                    queryWrapper = CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getId(), "id"));
                }
                if (criteria.getFullName() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getFullName(), "full_name")
                        );
                }
                if (criteria.getName() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getName(), "name"));
                }
                if (criteria.getExt() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getExt(), "ext"));
                }
                if (criteria.getType() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getType(), "type"));
                }
                if (criteria.getUrl() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getUrl(), "url"));
                }
                if (criteria.getPath() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getPath(), "path"));
                }
                if (criteria.getFolder() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildStringSpecification(criteria.getFolder(), "folder"));
                }
                if (criteria.getEntityName() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getEntityName(), "entity_name")
                        );
                }
                if (criteria.getCreateAt() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getCreateAt(), "create_at"));
                }
                if (criteria.getFileSize() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getFileSize(), "file_size"));
                }
                if (criteria.getReferenceCount() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getReferenceCount(), "reference_count")
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
                if (criteria.getUserId() != null) {
                    queryWrapper =
                        CriteriaUtil.build(criteria.getUseOr(), queryWrapper, buildRangeSpecification(criteria.getUserId(), "user_id"));
                }
                if (criteria.getUserLogin() != null) {
                    // todo 未实现 many-to-one;;login
                }
                if (criteria.getCategoryId() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildRangeSpecification(criteria.getCategoryId(), "category_id")
                        );
                }
                if (criteria.getCategoryTitle() != null) {
                    queryWrapper =
                        CriteriaUtil.build(
                            criteria.getUseOr(),
                            queryWrapper,
                            buildStringSpecification(criteria.getCategoryTitle(), "resource_category_left_join.title")
                        );
                }
            }
        }
        return queryWrapper;
    }
}
