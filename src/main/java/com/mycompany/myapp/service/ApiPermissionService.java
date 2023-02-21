package com.mycompany.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import com.mycompany.myapp.domain.ApiPermission;
import com.mycompany.myapp.domain.enumeration.ApiPermissionType;
import com.mycompany.myapp.repository.ApiPermissionRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.security.annotation.PermissionDefine;
import com.mycompany.myapp.service.dto.ApiPermissionDTO;
import com.mycompany.myapp.service.mapper.ApiPermissionMapper;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link ApiPermission}.
 */
@Service
public class ApiPermissionService extends BaseServiceImpl<ApiPermissionRepository, ApiPermission> {

    private final Logger log = LoggerFactory.getLogger(ApiPermissionService.class);
    private final List<String> relationCacheNames = Arrays.asList(
        com.mycompany.myapp.domain.ApiPermission.class.getName() + ".parent",
        com.mycompany.myapp.domain.ApiPermission.class.getName() + ".children",
        com.mycompany.myapp.domain.Authority.class.getName() + ".apiPermissions"
    );

    private final ApiPermissionRepository apiPermissionRepository;

    private final CacheManager cacheManager;

    private final ApiPermissionMapper apiPermissionMapper;

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public ApiPermissionService(
        ApiPermissionRepository apiPermissionRepository,
        CacheManager cacheManager,
        ApiPermissionMapper apiPermissionMapper,
        RequestMappingHandlerMapping requestMappingHandlerMapping
    ) {
        this.apiPermissionRepository = apiPermissionRepository;
        this.cacheManager = cacheManager;
        this.apiPermissionMapper = apiPermissionMapper;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    /**
     * Save a apiPermission.
     *
     * @param apiPermissionDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public ApiPermissionDTO save(ApiPermissionDTO apiPermissionDTO) {
        log.debug("Request to save ApiPermission : {}", apiPermissionDTO);
        ApiPermission apiPermission = apiPermissionMapper.toEntity(apiPermissionDTO);
        clearChildrenCache();
        this.saveOrUpdate(apiPermission);
        // 更新缓存
        if (apiPermission.getParent() != null) {
            apiPermission.getParent().addChildren(apiPermission);
        }
        return apiPermissionMapper.toDto(apiPermission);
    }

    /**
     * Partially update a apiPermission.
     *
     * @param apiPermissionDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<ApiPermissionDTO> partialUpdate(ApiPermissionDTO apiPermissionDTO) {
        log.debug("Request to partially update ApiPermission : {}", apiPermissionDTO);

        return apiPermissionRepository
            .findById(apiPermissionDTO.getId())
            .map(
                existingApiPermission -> {
                    apiPermissionMapper.partialUpdate(existingApiPermission, apiPermissionDTO);

                    return existingApiPermission;
                }
            )
            .map(
                tempApiPermission -> {
                    apiPermissionRepository.save(tempApiPermission);
                    return apiPermissionMapper.toDto(apiPermissionRepository.selectById(tempApiPermission.getId()));
                }
            );
    }

    /**
     * Get all the apiPermissions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<ApiPermissionDTO> findAll(Page<ApiPermission> pageable) {
        log.debug("Request to get all ApiPermissions");
        return this.page(pageable).convert(apiPermissionMapper::toDto);
    }

    /**
     * Get all the apiPermissions.
     *
     * @param type the ApiPermissionType.
     * @return the list of entities.
     */
    public List<ApiPermissionDTO> findAllByType(ApiPermissionType type) {
        log.debug("Request to get all ApiPermissions by type");
        return apiPermissionMapper.toDto(apiPermissionRepository.findAllByType(type));
    }

    /**
     * regenerate ApiPermissions from Annotation
     */
    @Transactional
    public void regenerateApiPermissions() {
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            HandlerMethod method = m.getValue();
            PermissionDefine permissionDefineAnnotation = method.getMethodAnnotation(PermissionDefine.class);
            if (permissionDefineAnnotation != null) {
                // 处理group
                ApiPermission apiPermissionGroup = apiPermissionRepository
                    .findOneByCode(permissionDefineAnnotation.groupCode())
                    .orElseGet(
                        () -> {
                            ApiPermission permission = new ApiPermission()
                                .code(permissionDefineAnnotation.groupCode())
                                .name(permissionDefineAnnotation.groupName())
                                .type(ApiPermissionType.BUSINESS);
                            apiPermissionRepository.insert(permission);
                            return this.getById(permission.getId());
                        }
                    );
                // 处理实体
                ApiPermission apiPermissionEntity = apiPermissionRepository
                    .findOneByCode(permissionDefineAnnotation.groupCode() + "_" + permissionDefineAnnotation.entityCode())
                    .orElseGet(
                        () -> {
                            ApiPermission permission = new ApiPermission()
                                .code(permissionDefineAnnotation.groupCode() + "_" + permissionDefineAnnotation.entityCode())
                                .name(permissionDefineAnnotation.entityName())
                                .type(ApiPermissionType.ENTITY);
                            apiPermissionRepository.insert(permission);
                            return this.getById(permission.getId());
                        }
                    );
                this.saveOrUpdate(apiPermissionEntity.parent(apiPermissionGroup));
                // 处理permission
                // 获得相关的methodType

                ApiPermission apiPermission = apiPermissionRepository
                    .findOneByCode(
                        permissionDefineAnnotation.groupCode() +
                        "_" +
                        permissionDefineAnnotation.entityCode() +
                        "_" +
                        permissionDefineAnnotation.permissionCode()
                    )
                    .orElseGet(
                        () -> {
                            ApiPermission permission = new ApiPermission()
                                .code(
                                    permissionDefineAnnotation.groupCode() +
                                    "_" +
                                    permissionDefineAnnotation.entityCode() +
                                    "_" +
                                    permissionDefineAnnotation.permissionCode()
                                )
                                .name(permissionDefineAnnotation.permissionName())
                                .parent(apiPermissionEntity)
                                .type(ApiPermissionType.API)
                                .status(permissionDefineAnnotation.state());
                            apiPermissionRepository.insert(permission);
                            return this.getById(permission.getId());
                        }
                    );

                GetMapping getMappingAnnotation = method.getMethodAnnotation(GetMapping.class);
                PostMapping postMappingAnnotation = method.getMethodAnnotation(PostMapping.class);
                DeleteMapping deleteMappingAnnotation = method.getMethodAnnotation(DeleteMapping.class);
                PutMapping putMappingAnnotation = method.getMethodAnnotation(PutMapping.class);
                RequestMapping requestMappingAnnotation = method.getMethodAnnotation(RequestMapping.class);
                StringBuilder methodType = new StringBuilder();
                if (getMappingAnnotation != null) {
                    methodType = new StringBuilder("GET");
                }
                if (postMappingAnnotation != null) {
                    methodType = new StringBuilder("POST");
                }
                if (deleteMappingAnnotation != null) {
                    methodType = new StringBuilder("DELETE");
                }
                if (putMappingAnnotation != null) {
                    methodType = new StringBuilder("PUT");
                }
                if (requestMappingAnnotation != null) {
                    if (requestMappingAnnotation.method().length > 0) {
                        RequestMethod[] methods = requestMappingAnnotation.method();
                        for (RequestMethod r : methods) {
                            if (methodType.indexOf(r.name()) == -1) {
                                methodType.append(",").append(r.name());
                            }
                        }
                        if (methodType.charAt(0) == ',') {
                            methodType.deleteCharAt(0);
                        }
                    }
                }
                // url
                PatternsRequestCondition patternsCondition = m.getKey().getPatternsCondition();
                String url = patternsCondition.toString();
                apiPermission.method(methodType.toString()).url(url);
                apiPermission.status(permissionDefineAnnotation.state());
                this.saveOrUpdate(apiPermission.parent(apiPermissionEntity));
            }
        }
    }

    public List<ApiPermissionDTO> findAllApiPermissionsByCurrentUser() {
        if (SecurityUtils.isAuthenticated()) {
            return apiPermissionMapper.toDto(
                apiPermissionRepository.findAllApiPermissionsByCurrentUser(SecurityUtils.getCurrentUserId().get())
            );
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Get all the apiPermissions for parent is null.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public IPage<ApiPermissionDTO> findAllTop(Page<ApiPermission> pageable) {
        log.debug("Request to get all ApiPermissions for parent is null");
        return apiPermissionRepository
            .findAllByParentIsNull(pageable)
            .convert(
                apiPermission -> {
                    Binder.bindRelations(apiPermission, new String[] { "departmentAuthority", "parent", "authorities" });
                    return apiPermissionMapper.toDto(apiPermission);
                }
            );
    }

    /**
     * Get all the apiPermissions for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<ApiPermissionDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all ApiPermissions for parent is parentId");
        return apiPermissionRepository
            .selectList(new LambdaUpdateWrapper<ApiPermission>().eq(ApiPermission::getParentId, parentId))
            .stream()
            .map(
                apiPermission -> {
                    Binder.bindRelations(apiPermission, new String[] { "departmentAuthority", "parent", "authorities" });
                    return apiPermissionMapper.toDto(apiPermission);
                }
            )
            .collect(Collectors.toList());
    }

    /**
     * Get one apiPermission by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ApiPermissionDTO> findOne(Long id) {
        log.debug("Request to get ApiPermission : {}", id);
        return Optional.ofNullable(apiPermissionRepository.selectById(id)).map(apiPermissionMapper::toDto);
    }

    /**
     * Delete the apiPermission by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete ApiPermission : {}", id);
        ApiPermission apiPermission = apiPermissionRepository.selectById(id);
        if (apiPermission.getParent() != null) {
            apiPermission.getParent().removeChildren(apiPermission);
        }
        if (apiPermission.getChildren() != null) {
            apiPermission
                .getChildren()
                .forEach(
                    subApiPermission -> {
                        subApiPermission.setParent(null);
                    }
                );
        }
        apiPermissionRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by apiPermission
     */
    @Transactional
    public ApiPermissionDTO updateByIgnoreSpecifiedFields(ApiPermissionDTO changeApiPermissionDTO, Set<String> unchangedFields) {
        ApiPermissionDTO apiPermissionDTO = findOne(changeApiPermissionDTO.getId()).get();
        BeanUtil.copyProperties(changeApiPermissionDTO, apiPermissionDTO, unchangedFields.toArray(new String[0]));
        apiPermissionDTO = save(apiPermissionDTO);
        return apiPermissionDTO;
    }

    /**
     * Update specified fields by apiPermission
     */
    @Transactional
    public ApiPermissionDTO updateBySpecifiedFields(ApiPermissionDTO changeApiPermissionDTO, Set<String> fieldNames) {
        UpdateWrapper<ApiPermission> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeApiPermissionDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                    BeanUtil.getFieldValue(changeApiPermissionDTO, fieldName)
                );
            }
        );
        this.update(updateWrapper);
        return findOne(changeApiPermissionDTO.getId()).get();
    }

    /**
     * Update specified field by apiPermission
     */
    @Transactional
    public ApiPermissionDTO updateBySpecifiedField(ApiPermissionDTO changeApiPermissionDTO, String fieldName) {
        ApiPermissionDTO update = new ApiPermissionDTO();
        BeanUtil.setFieldValue(update, "id", changeApiPermissionDTO.getId());
        BeanUtil.setFieldValue(update, fieldName, BeanUtil.getFieldValue(changeApiPermissionDTO, fieldName));
        this.updateEntity(apiPermissionMapper.toEntity(update));
        return findOne(changeApiPermissionDTO.getId()).get();
    }

    // 清除children缓存
    private void clearChildrenCache() {
        Objects.requireNonNull(cacheManager.getCache(com.mycompany.myapp.domain.ApiPermission.class.getName() + ".children")).clear();
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
