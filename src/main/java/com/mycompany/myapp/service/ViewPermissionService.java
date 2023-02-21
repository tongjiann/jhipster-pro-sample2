package com.mycompany.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import com.mycompany.myapp.domain.ViewPermission;
import com.mycompany.myapp.repository.ViewPermissionRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.dto.ViewPermissionDTO;
import com.mycompany.myapp.service.mapper.ViewPermissionMapper;
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
 * Service Implementation for managing {@link ViewPermission}.
 */
@Service
public class ViewPermissionService extends BaseServiceImpl<ViewPermissionRepository, ViewPermission> {

    private final Logger log = LoggerFactory.getLogger(ViewPermissionService.class);
    private final List<String> relationCacheNames = Arrays.asList(
        com.mycompany.myapp.domain.ViewPermission.class.getName() + ".parent",
        com.mycompany.myapp.domain.ViewPermission.class.getName() + ".children",
        com.mycompany.myapp.domain.Authority.class.getName() + ".viewPermissions"
    );

    private final ViewPermissionRepository viewPermissionRepository;

    private final CacheManager cacheManager;

    private final ViewPermissionMapper viewPermissionMapper;

    public ViewPermissionService(
        ViewPermissionRepository viewPermissionRepository,
        CacheManager cacheManager,
        ViewPermissionMapper viewPermissionMapper
    ) {
        this.viewPermissionRepository = viewPermissionRepository;
        this.cacheManager = cacheManager;
        this.viewPermissionMapper = viewPermissionMapper;
    }

    /**
     * Save a viewPermission.
     *
     * @param viewPermissionDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public ViewPermissionDTO save(ViewPermissionDTO viewPermissionDTO) {
        log.debug("Request to save ViewPermission : {}", viewPermissionDTO);
        ViewPermission viewPermission = viewPermissionMapper.toEntity(viewPermissionDTO);
        clearChildrenCache();
        this.saveOrUpdate(viewPermission);
        // 更新缓存
        if (viewPermission.getParent() != null) {
            viewPermission.getParent().addChildren(viewPermission);
        }
        return viewPermissionMapper.toDto(viewPermission);
    }

    /**
     * Partially update a viewPermission.
     *
     * @param viewPermissionDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<ViewPermissionDTO> partialUpdate(ViewPermissionDTO viewPermissionDTO) {
        log.debug("Request to partially update ViewPermission : {}", viewPermissionDTO);

        return viewPermissionRepository
            .findById(viewPermissionDTO.getId())
            .map(
                existingViewPermission -> {
                    viewPermissionMapper.partialUpdate(existingViewPermission, viewPermissionDTO);

                    return existingViewPermission;
                }
            )
            .map(
                tempViewPermission -> {
                    viewPermissionRepository.save(tempViewPermission);
                    return viewPermissionMapper.toDto(viewPermissionRepository.selectById(tempViewPermission.getId()));
                }
            );
    }

    /**
     * Get all the viewPermissions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<ViewPermissionDTO> findAll(Page<ViewPermission> pageable) {
        log.debug("Request to get all ViewPermissions");
        return this.page(pageable).convert(viewPermissionMapper::toDto);
    }

    /**
     * get all viewPermission by currentUserLogin
     *
     * @return List<ViewPermissionDTO>
     */
    public List<ViewPermissionDTO> getAllByLogin() {
        List<ViewPermission> resultList = new ArrayList<>();
        // 根据login获得用户的角色
        SecurityUtils
            .getCurrentUserId()
            .ifPresent(
                userId -> {
                    List<ViewPermission> viewPermissionsByUserId = this.viewPermissionRepository.findAllViewPermissionsByUserId(userId);
                    List<ViewPermission> addViewPermissions = new ArrayList<>();
                    viewPermissionsByUserId.forEach(
                        viewPermission -> {
                            Binder.bindRelations(viewPermission, new String[] { "parent", "authorities" });
                            while (viewPermission != null && viewPermission.getParent() != null) {
                                Long parentId = viewPermission.getParent().getId();
                                if (
                                    viewPermissionsByUserId
                                        .stream()
                                        .noneMatch(viewPermissionDTO1 -> viewPermissionDTO1.getId().equals(parentId))
                                ) {
                                    Optional<ViewPermission> oneNoChildren = viewPermissionRepository.findById(parentId);
                                    if (oneNoChildren.isPresent()) {
                                        addViewPermissions.add(oneNoChildren.get());
                                        viewPermission = oneNoChildren.get();
                                        Binder.bindRelations(viewPermission, new String[] { "parent", "authorities" });
                                    } else {
                                        viewPermission = null;
                                    }
                                } else {
                                    viewPermission = null;
                                }
                            }
                        }
                    );
                    viewPermissionsByUserId.addAll(addViewPermissions);
                    // 已经找到所有的当前User相关的ViewPermissions及上级，接下来转换为树形结构。
                    viewPermissionsByUserId.forEach(
                        userViewPermission -> {
                            if (userViewPermission.getParent() == null) {
                                Long finalId = userViewPermission.getId();
                                if (
                                    resultList
                                        .stream()
                                        .noneMatch(resultViewPermissionDTO -> resultViewPermissionDTO.getId().equals(finalId))
                                ) {
                                    resultList.add(userViewPermission);
                                }
                            } else {
                                ViewPermission topParent = null;
                                while (userViewPermission != null && userViewPermission.getParent() != null) {
                                    Long tempId = userViewPermission.getParent().getId();
                                    Optional<ViewPermission> tempViewPermission = viewPermissionsByUserId
                                        .stream()
                                        .filter(viewPermission -> viewPermission.getId().equals(tempId))
                                        .findAny();
                                    if (tempViewPermission.isPresent()) {
                                        if (tempViewPermission.get().getParent() == null) {
                                            topParent = tempViewPermission.get();
                                            tempViewPermission.get().getChildren().add(userViewPermission);
                                            userViewPermission = null;
                                        } else {
                                            tempViewPermission.get().getChildren().add(userViewPermission);
                                            userViewPermission = tempViewPermission.get();
                                        }
                                    } else {
                                        userViewPermission = null;
                                    }
                                }
                                if (topParent != null) {
                                    ViewPermission finalTopParent = topParent;
                                    Optional<ViewPermission> any = resultList
                                        .stream()
                                        .filter(resultViewPermission -> resultViewPermission.getId().equals(finalTopParent.getId()))
                                        .findAny();
                                    if (any.isPresent()) {
                                        // 处理子列表
                                        List<ViewPermission> resultChildren = any.get().getChildren();
                                        List<ViewPermission> unCheckChildren = topParent.getChildren();
                                        addToResult(resultChildren, unCheckChildren);
                                    } else {
                                        resultList.add(topParent);
                                    }
                                }
                            }
                        }
                    );
                }
            );
        return viewPermissionMapper.toDto(resultList);
    }

    private void addToResult(List<ViewPermission> resultChildren, List<ViewPermission> unCheckChildren) {
        if (unCheckChildren.size() > 0) {
            unCheckChildren.forEach(
                child -> {
                    Long childId = child.getId();
                    Optional<ViewPermission> any = resultChildren
                        .stream()
                        .filter(viewPermission -> viewPermission.getId().equals(childId))
                        .findAny();
                    if (any.isPresent()) {
                        addToResult(any.get().getChildren(), child.getChildren());
                    } else {
                        resultChildren.add(child);
                    }
                }
            );
        }
    }

    /**
     * Get all the viewPermissions for parent is null.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public IPage<ViewPermissionDTO> findAllTop(Page<ViewPermission> pageable) {
        log.debug("Request to get all ViewPermissions for parent is null");
        return viewPermissionRepository
            .findAllByParentIsNull(pageable)
            .convert(
                viewPermission -> {
                    Binder.bindRelations(viewPermission, new String[] { "departmentAuthority", "parent", "authorities" });
                    return viewPermissionMapper.toDto(viewPermission);
                }
            );
    }

    /**
     * Get all the viewPermissions for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<ViewPermissionDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all ViewPermissions for parent is parentId");
        return viewPermissionRepository
            .selectList(new LambdaUpdateWrapper<ViewPermission>().eq(ViewPermission::getParentId, parentId))
            .stream()
            .map(
                viewPermission -> {
                    Binder.bindRelations(viewPermission, new String[] { "departmentAuthority", "parent", "authorities" });
                    return viewPermissionMapper.toDto(viewPermission);
                }
            )
            .collect(Collectors.toList());
    }

    /**
     * Get one viewPermission by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ViewPermissionDTO> findOne(Long id) {
        log.debug("Request to get ViewPermission : {}", id);
        return Optional.ofNullable(viewPermissionRepository.selectById(id)).map(viewPermissionMapper::toDto);
    }

    /**
     * Delete the viewPermission by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete ViewPermission : {}", id);
        ViewPermission viewPermission = viewPermissionRepository.selectById(id);
        if (viewPermission.getParent() != null) {
            viewPermission.getParent().removeChildren(viewPermission);
        }
        if (viewPermission.getChildren() != null) {
            viewPermission
                .getChildren()
                .forEach(
                    subViewPermission -> {
                        subViewPermission.setParent(null);
                    }
                );
        }
        viewPermissionRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by viewPermission
     */
    @Transactional
    public ViewPermissionDTO updateByIgnoreSpecifiedFields(ViewPermissionDTO changeViewPermissionDTO, Set<String> unchangedFields) {
        ViewPermissionDTO viewPermissionDTO = findOne(changeViewPermissionDTO.getId()).get();
        BeanUtil.copyProperties(changeViewPermissionDTO, viewPermissionDTO, unchangedFields.toArray(new String[0]));
        viewPermissionDTO = save(viewPermissionDTO);
        return viewPermissionDTO;
    }

    /**
     * Update specified fields by viewPermission
     */
    @Transactional
    public ViewPermissionDTO updateBySpecifiedFields(ViewPermissionDTO changeViewPermissionDTO, Set<String> fieldNames) {
        UpdateWrapper<ViewPermission> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeViewPermissionDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                    BeanUtil.getFieldValue(changeViewPermissionDTO, fieldName)
                );
            }
        );
        this.update(updateWrapper);
        return findOne(changeViewPermissionDTO.getId()).get();
    }

    /**
     * Update specified field by viewPermission
     */
    @Transactional
    public ViewPermissionDTO updateBySpecifiedField(ViewPermissionDTO changeViewPermissionDTO, String fieldName) {
        ViewPermissionDTO update = new ViewPermissionDTO();
        BeanUtil.setFieldValue(update, "id", changeViewPermissionDTO.getId());
        BeanUtil.setFieldValue(update, fieldName, BeanUtil.getFieldValue(changeViewPermissionDTO, fieldName));
        this.updateEntity(viewPermissionMapper.toEntity(update));
        return findOne(changeViewPermissionDTO.getId()).get();
    }

    // 清除children缓存
    private void clearChildrenCache() {
        Objects.requireNonNull(cacheManager.getCache(com.mycompany.myapp.domain.ViewPermission.class.getName() + ".children")).clear();
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
