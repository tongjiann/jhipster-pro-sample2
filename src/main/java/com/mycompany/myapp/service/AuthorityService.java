package com.mycompany.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.repository.AuthorityRepository;
import com.mycompany.myapp.service.dto.AuthorityDTO;
import com.mycompany.myapp.service.mapper.AuthorityMapper;
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
 * Service Implementation for managing {@link Authority}.
 */
@Service
public class AuthorityService extends BaseServiceImpl<AuthorityRepository, Authority> {

    private final Logger log = LoggerFactory.getLogger(AuthorityService.class);
    private final List<String> relationCacheNames = Arrays.asList(
        com.mycompany.myapp.domain.Authority.class.getName() + ".parent",
        com.mycompany.myapp.domain.Authority.class.getName() + ".children",
        com.mycompany.myapp.domain.ApiPermission.class.getName() + ".authorities",
        com.mycompany.myapp.domain.ViewPermission.class.getName() + ".authorities"
    );

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    private final AuthorityMapper authorityMapper;

    public AuthorityService(AuthorityRepository authorityRepository, CacheManager cacheManager, AuthorityMapper authorityMapper) {
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.authorityMapper = authorityMapper;
    }

    /**
     * Save a authority.
     *
     * @param authorityDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public AuthorityDTO save(AuthorityDTO authorityDTO) {
        log.debug("Request to save Authority : {}", authorityDTO);
        Authority authority = authorityMapper.toEntity(authorityDTO);
        clearChildrenCache();
        this.saveOrUpdate(authority);
        // 更新缓存
        if (authority.getParent() != null) {
            authority.getParent().addChildren(authority);
        }
        return authorityMapper.toDto(authority);
    }

    /**
     * Partially update a authority.
     *
     * @param authorityDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<AuthorityDTO> partialUpdate(AuthorityDTO authorityDTO) {
        log.debug("Request to partially update Authority : {}", authorityDTO);

        return authorityRepository
            .findById(authorityDTO.getId())
            .map(
                existingAuthority -> {
                    authorityMapper.partialUpdate(existingAuthority, authorityDTO);

                    return existingAuthority;
                }
            )
            .map(
                tempAuthority -> {
                    authorityRepository.save(tempAuthority);
                    return authorityMapper.toDto(authorityRepository.selectById(tempAuthority.getId()));
                }
            );
    }

    /**
     * Get all the authorities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<AuthorityDTO> findAll(Page<Authority> pageable) {
        log.debug("Request to get all Authorities");
        return this.page(pageable).convert(authorityMapper::toDto);
    }

    /**
     * Get one authority by code.
     *
     * @param code the id of the entity.
     * @return the entity.
     */
    public Optional<AuthorityDTO> findFirstByCode(String code) {
        log.debug("Request to get Authority : {}", code);
        return authorityRepository.findFirstByCode(code).map(authorityMapper::toDto);
    }

    /**
     * Get all the authorities for parent is null.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public IPage<AuthorityDTO> findAllTop(Page<Authority> pageable) {
        log.debug("Request to get all Authorities for parent is null");
        return authorityRepository
            .findAllByParentIsNull(pageable)
            .convert(
                authority -> {
                    Binder.bindRelations(authority, new String[] { "parent", "departments", "apiPermissions", "viewPermissions" });
                    return authorityMapper.toDto(authority);
                }
            );
    }

    /**
     * Get all the authorities for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<AuthorityDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all Authorities for parent is parentId");
        return authorityRepository
            .selectList(new LambdaUpdateWrapper<Authority>().eq(Authority::getParentId, parentId))
            .stream()
            .map(
                authority -> {
                    Binder.bindRelations(authority, new String[] { "parent", "departments", "apiPermissions", "viewPermissions" });
                    return authorityMapper.toDto(authority);
                }
            )
            .collect(Collectors.toList());
    }

    /**
     * Get all the authorities with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public IPage<AuthorityDTO> findAllWithEagerRelationships(Page<Authority> pageable) {
        IPage<Authority> result = authorityRepository.selectPage(pageable, null);
        Binder.bindRelations(result.getRecords(), new String[] { "children", "parent" });
        return result.convert(authorityMapper::toDto);
    }

    /**
     * Get one authority by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<AuthorityDTO> findOne(Long id) {
        log.debug("Request to get Authority : {}", id);
        return Optional
            .ofNullable(authorityRepository.selectById(id))
            .map(
                authority -> {
                    Binder.bindRelations(authority);
                    return authority;
                }
            )
            .map(authorityMapper::toDto);
    }

    /**
     * Delete the authority by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Authority : {}", id);
        Authority authority = authorityRepository.selectById(id);
        if (authority.getParent() != null) {
            authority.getParent().removeChildren(authority);
        }
        if (authority.getChildren() != null) {
            authority
                .getChildren()
                .forEach(
                    subAuthority -> {
                        subAuthority.setParent(null);
                    }
                );
        }
        authorityRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by authority
     */
    @Transactional
    public AuthorityDTO updateByIgnoreSpecifiedFields(AuthorityDTO changeAuthorityDTO, Set<String> unchangedFields) {
        AuthorityDTO authorityDTO = findOne(changeAuthorityDTO.getId()).get();
        BeanUtil.copyProperties(changeAuthorityDTO, authorityDTO, unchangedFields.toArray(new String[0]));
        authorityDTO = save(authorityDTO);
        return authorityDTO;
    }

    /**
     * Update specified fields by authority
     */
    @Transactional
    public AuthorityDTO updateBySpecifiedFields(AuthorityDTO changeAuthorityDTO, Set<String> fieldNames) {
        UpdateWrapper<Authority> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeAuthorityDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                    BeanUtil.getFieldValue(changeAuthorityDTO, fieldName)
                );
            }
        );
        this.update(updateWrapper);
        return findOne(changeAuthorityDTO.getId()).get();
    }

    /**
     * Update specified field by authority
     */
    @Transactional
    public AuthorityDTO updateBySpecifiedField(AuthorityDTO changeAuthorityDTO, String fieldName) {
        AuthorityDTO update = new AuthorityDTO();
        BeanUtil.setFieldValue(update, "id", changeAuthorityDTO.getId());
        BeanUtil.setFieldValue(update, fieldName, BeanUtil.getFieldValue(changeAuthorityDTO, fieldName));
        this.updateEntity(authorityMapper.toEntity(update));
        return findOne(changeAuthorityDTO.getId()).get();
    }

    // 清除children缓存
    private void clearChildrenCache() {
        Objects.requireNonNull(cacheManager.getCache(com.mycompany.myapp.domain.Authority.class.getName() + ".children")).clear();
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
