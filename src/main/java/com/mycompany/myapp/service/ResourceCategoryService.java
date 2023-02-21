package com.mycompany.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import com.mycompany.myapp.domain.ResourceCategory;
import com.mycompany.myapp.repository.ResourceCategoryRepository;
import com.mycompany.myapp.service.dto.ResourceCategoryDTO;
import com.mycompany.myapp.service.mapper.ResourceCategoryMapper;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link ResourceCategory}.
 */
@Service
public class ResourceCategoryService extends BaseServiceImpl<ResourceCategoryRepository, ResourceCategory> {

    private final Logger log = LoggerFactory.getLogger(ResourceCategoryService.class);
    private final List<String> relationCacheNames = Arrays.asList(
        com.mycompany.myapp.domain.UploadFile.class.getName() + ".category",
        com.mycompany.myapp.domain.ResourceCategory.class.getName() + ".parent",
        com.mycompany.myapp.domain.UploadImage.class.getName() + ".category",
        com.mycompany.myapp.domain.ResourceCategory.class.getName() + ".children"
    );

    private final ResourceCategoryRepository resourceCategoryRepository;

    private final CacheManager cacheManager;

    private final ResourceCategoryMapper resourceCategoryMapper;

    public ResourceCategoryService(
        ResourceCategoryRepository resourceCategoryRepository,
        CacheManager cacheManager,
        ResourceCategoryMapper resourceCategoryMapper
    ) {
        this.resourceCategoryRepository = resourceCategoryRepository;
        this.cacheManager = cacheManager;
        this.resourceCategoryMapper = resourceCategoryMapper;
    }

    /**
     * Save a resourceCategory.
     *
     * @param resourceCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public ResourceCategoryDTO save(ResourceCategoryDTO resourceCategoryDTO) {
        log.debug("Request to save ResourceCategory : {}", resourceCategoryDTO);
        ResourceCategory resourceCategory = resourceCategoryMapper.toEntity(resourceCategoryDTO);
        clearChildrenCache();
        this.saveOrUpdate(resourceCategory);
        // 更新缓存
        if (resourceCategory.getParent() != null) {
            resourceCategory.getParent().addChildren(resourceCategory);
        }
        return resourceCategoryMapper.toDto(resourceCategory);
    }

    /**
     * Partially update a resourceCategory.
     *
     * @param resourceCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<ResourceCategoryDTO> partialUpdate(ResourceCategoryDTO resourceCategoryDTO) {
        log.debug("Request to partially update ResourceCategory : {}", resourceCategoryDTO);

        return resourceCategoryRepository
            .findById(resourceCategoryDTO.getId())
            .map(
                existingResourceCategory -> {
                    resourceCategoryMapper.partialUpdate(existingResourceCategory, resourceCategoryDTO);

                    return existingResourceCategory;
                }
            )
            .map(
                tempResourceCategory -> {
                    resourceCategoryRepository.save(tempResourceCategory);
                    return resourceCategoryMapper.toDto(resourceCategoryRepository.selectById(tempResourceCategory.getId()));
                }
            );
    }

    /**
     * Get all the resourceCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<ResourceCategoryDTO> findAll(Page<ResourceCategory> pageable) {
        log.debug("Request to get all ResourceCategories");
        return this.page(pageable).convert(resourceCategoryMapper::toDto);
    }

    /**
     * Get all the resourceCategories for parent is null.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public IPage<ResourceCategoryDTO> findAllTop(Page<ResourceCategory> pageable) {
        log.debug("Request to get all ResourceCategories for parent is null");
        return resourceCategoryRepository
            .findAllByParentIsNull(pageable)
            .convert(
                resourceCategory -> {
                    Binder.bindRelations(resourceCategory, new String[] { "files", "images", "parent" });
                    return resourceCategoryMapper.toDto(resourceCategory);
                }
            );
    }

    /**
     * Get all the resourceCategories for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<ResourceCategoryDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all ResourceCategories for parent is parentId");
        return resourceCategoryRepository
            .selectList(new LambdaUpdateWrapper<ResourceCategory>().eq(ResourceCategory::getParentId, parentId))
            .stream()
            .map(
                resourceCategory -> {
                    Binder.bindRelations(resourceCategory, new String[] { "files", "images", "parent" });
                    return resourceCategoryMapper.toDto(resourceCategory);
                }
            )
            .collect(Collectors.toList());
    }

    /**
     * Get one resourceCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ResourceCategoryDTO> findOne(Long id) {
        log.debug("Request to get ResourceCategory : {}", id);
        return Optional.ofNullable(resourceCategoryRepository.selectById(id)).map(resourceCategoryMapper::toDto);
    }

    /**
     * Delete the resourceCategory by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete ResourceCategory : {}", id);
        ResourceCategory resourceCategory = resourceCategoryRepository.selectById(id);
        if (resourceCategory.getParent() != null) {
            resourceCategory.getParent().removeChildren(resourceCategory);
        }
        if (resourceCategory.getChildren() != null) {
            resourceCategory
                .getChildren()
                .forEach(
                    subResourceCategory -> {
                        subResourceCategory.setParent(null);
                    }
                );
        }
        resourceCategoryRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by resourceCategory
     */
    @Transactional
    public ResourceCategoryDTO updateByIgnoreSpecifiedFields(ResourceCategoryDTO changeResourceCategoryDTO, Set<String> unchangedFields) {
        ResourceCategoryDTO resourceCategoryDTO = findOne(changeResourceCategoryDTO.getId()).get();
        BeanUtil.copyProperties(changeResourceCategoryDTO, resourceCategoryDTO, unchangedFields.toArray(new String[0]));
        resourceCategoryDTO = save(resourceCategoryDTO);
        return resourceCategoryDTO;
    }

    /**
     * Update specified fields by resourceCategory
     */
    @Transactional
    public ResourceCategoryDTO updateBySpecifiedFields(ResourceCategoryDTO changeResourceCategoryDTO, Set<String> fieldNames) {
        UpdateWrapper<ResourceCategory> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeResourceCategoryDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                    BeanUtil.getFieldValue(changeResourceCategoryDTO, fieldName)
                );
            }
        );
        this.update(updateWrapper);
        return findOne(changeResourceCategoryDTO.getId()).get();
    }

    /**
     * Update specified field by resourceCategory
     */
    @Transactional
    public ResourceCategoryDTO updateBySpecifiedField(ResourceCategoryDTO changeResourceCategoryDTO, String fieldName) {
        ResourceCategoryDTO update = new ResourceCategoryDTO();
        BeanUtil.setFieldValue(update, "id", changeResourceCategoryDTO.getId());
        BeanUtil.setFieldValue(update, fieldName, BeanUtil.getFieldValue(changeResourceCategoryDTO, fieldName));
        this.updateEntity(resourceCategoryMapper.toEntity(update));
        return findOne(changeResourceCategoryDTO.getId()).get();
    }

    // 清除children缓存
    private void clearChildrenCache() {
        Objects.requireNonNull(cacheManager.getCache(com.mycompany.myapp.domain.ResourceCategory.class.getName() + ".children")).clear();
    }

    private void clearRelationsCache() {
        this.relationCacheNames.forEach(
                cacheName -> {
                    if (cacheManager.getCache(cacheName) != null) {
                        cacheManager.getCache(cacheName).clear();
                    }
                }
            );
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
