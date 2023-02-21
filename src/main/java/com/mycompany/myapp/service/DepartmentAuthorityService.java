package com.mycompany.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import com.mycompany.myapp.domain.DepartmentAuthority;
import com.mycompany.myapp.repository.DepartmentAuthorityRepository;
import com.mycompany.myapp.service.dto.DepartmentAuthorityDTO;
import com.mycompany.myapp.service.mapper.DepartmentAuthorityMapper;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link DepartmentAuthority}.
 */
@Service
public class DepartmentAuthorityService extends BaseServiceImpl<DepartmentAuthorityRepository, DepartmentAuthority> {

    private final Logger log = LoggerFactory.getLogger(DepartmentAuthorityService.class);
    private final List<String> relationCacheNames = Arrays.asList(
        com.mycompany.myapp.domain.User.class.getName() + ".departmentAuthority",
        com.mycompany.myapp.domain.ApiPermission.class.getName() + ".departmentAuthority",
        com.mycompany.myapp.domain.ViewPermission.class.getName() + ".departmentAuthority",
        com.mycompany.myapp.domain.Department.class.getName() + ".departmentAuthorities"
    );

    private final DepartmentAuthorityRepository departmentAuthorityRepository;

    private final CacheManager cacheManager;

    private final DepartmentAuthorityMapper departmentAuthorityMapper;

    public DepartmentAuthorityService(
        DepartmentAuthorityRepository departmentAuthorityRepository,
        CacheManager cacheManager,
        DepartmentAuthorityMapper departmentAuthorityMapper
    ) {
        this.departmentAuthorityRepository = departmentAuthorityRepository;
        this.cacheManager = cacheManager;
        this.departmentAuthorityMapper = departmentAuthorityMapper;
    }

    /**
     * Save a departmentAuthority.
     *
     * @param departmentAuthorityDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public DepartmentAuthorityDTO save(DepartmentAuthorityDTO departmentAuthorityDTO) {
        log.debug("Request to save DepartmentAuthority : {}", departmentAuthorityDTO);
        DepartmentAuthority departmentAuthority = departmentAuthorityMapper.toEntity(departmentAuthorityDTO);
        this.saveOrUpdate(departmentAuthority);
        return departmentAuthorityMapper.toDto(departmentAuthority);
    }

    /**
     * Partially update a departmentAuthority.
     *
     * @param departmentAuthorityDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<DepartmentAuthorityDTO> partialUpdate(DepartmentAuthorityDTO departmentAuthorityDTO) {
        log.debug("Request to partially update DepartmentAuthority : {}", departmentAuthorityDTO);

        return departmentAuthorityRepository
            .findById(departmentAuthorityDTO.getId())
            .map(
                existingDepartmentAuthority -> {
                    departmentAuthorityMapper.partialUpdate(existingDepartmentAuthority, departmentAuthorityDTO);

                    return existingDepartmentAuthority;
                }
            )
            .map(
                tempDepartmentAuthority -> {
                    departmentAuthorityRepository.save(tempDepartmentAuthority);
                    return departmentAuthorityMapper.toDto(departmentAuthorityRepository.selectById(tempDepartmentAuthority.getId()));
                }
            );
    }

    /**
     * Get all the departmentAuthorities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<DepartmentAuthorityDTO> findAll(Page<DepartmentAuthority> pageable) {
        log.debug("Request to get all DepartmentAuthorities");
        return this.page(pageable).convert(departmentAuthorityMapper::toDto);
    }

    /**
     * Get one departmentAuthority by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<DepartmentAuthorityDTO> findOne(Long id) {
        log.debug("Request to get DepartmentAuthority : {}", id);
        return Optional.ofNullable(departmentAuthorityRepository.selectById(id)).map(departmentAuthorityMapper::toDto);
    }

    /**
     * Delete the departmentAuthority by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete DepartmentAuthority : {}", id);
        departmentAuthorityRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by departmentAuthority
     */
    @Transactional
    public DepartmentAuthorityDTO updateByIgnoreSpecifiedFields(
        DepartmentAuthorityDTO changeDepartmentAuthorityDTO,
        Set<String> unchangedFields
    ) {
        DepartmentAuthorityDTO departmentAuthorityDTO = findOne(changeDepartmentAuthorityDTO.getId()).get();
        BeanUtil.copyProperties(changeDepartmentAuthorityDTO, departmentAuthorityDTO, unchangedFields.toArray(new String[0]));
        departmentAuthorityDTO = save(departmentAuthorityDTO);
        return departmentAuthorityDTO;
    }

    /**
     * Update specified fields by departmentAuthority
     */
    @Transactional
    public DepartmentAuthorityDTO updateBySpecifiedFields(DepartmentAuthorityDTO changeDepartmentAuthorityDTO, Set<String> fieldNames) {
        UpdateWrapper<DepartmentAuthority> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeDepartmentAuthorityDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                    BeanUtil.getFieldValue(changeDepartmentAuthorityDTO, fieldName)
                );
            }
        );
        this.update(updateWrapper);
        return findOne(changeDepartmentAuthorityDTO.getId()).get();
    }

    /**
     * Update specified field by departmentAuthority
     */
    @Transactional
    public DepartmentAuthorityDTO updateBySpecifiedField(DepartmentAuthorityDTO changeDepartmentAuthorityDTO, String fieldName) {
        DepartmentAuthorityDTO update = new DepartmentAuthorityDTO();
        BeanUtil.setFieldValue(update, "id", changeDepartmentAuthorityDTO.getId());
        BeanUtil.setFieldValue(update, fieldName, BeanUtil.getFieldValue(changeDepartmentAuthorityDTO, fieldName));
        this.updateEntity(departmentAuthorityMapper.toEntity(update));
        return findOne(changeDepartmentAuthorityDTO.getId()).get();
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
