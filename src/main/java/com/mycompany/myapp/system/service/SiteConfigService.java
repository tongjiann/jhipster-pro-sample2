package com.mycompany.myapp.system.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import com.mycompany.myapp.system.domain.SiteConfig;
import com.mycompany.myapp.system.repository.SiteConfigRepository;
import com.mycompany.myapp.system.service.dto.SiteConfigDTO;
import com.mycompany.myapp.system.service.mapper.SiteConfigMapper;
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
 * Service Implementation for managing {@link SiteConfig}.
 */
@Service
public class SiteConfigService extends BaseServiceImpl<SiteConfigRepository, SiteConfig> {

    private final Logger log = LoggerFactory.getLogger(SiteConfigService.class);

    private final SiteConfigRepository siteConfigRepository;

    private final CacheManager cacheManager;

    private final SiteConfigMapper siteConfigMapper;

    public SiteConfigService(SiteConfigRepository siteConfigRepository, CacheManager cacheManager, SiteConfigMapper siteConfigMapper) {
        this.siteConfigRepository = siteConfigRepository;
        this.cacheManager = cacheManager;
        this.siteConfigMapper = siteConfigMapper;
    }

    /**
     * Save a siteConfig.
     *
     * @param siteConfigDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public SiteConfigDTO save(SiteConfigDTO siteConfigDTO) {
        log.debug("Request to save SiteConfig : {}", siteConfigDTO);
        SiteConfig siteConfig = siteConfigMapper.toEntity(siteConfigDTO);
        this.saveOrUpdate(siteConfig);
        return siteConfigMapper.toDto(siteConfig);
    }

    /**
     * Partially update a siteConfig.
     *
     * @param siteConfigDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<SiteConfigDTO> partialUpdate(SiteConfigDTO siteConfigDTO) {
        log.debug("Request to partially update SiteConfig : {}", siteConfigDTO);

        return siteConfigRepository
            .findById(siteConfigDTO.getId())
            .map(
                existingSiteConfig -> {
                    siteConfigMapper.partialUpdate(existingSiteConfig, siteConfigDTO);

                    return existingSiteConfig;
                }
            )
            .map(
                tempSiteConfig -> {
                    siteConfigRepository.save(tempSiteConfig);
                    return siteConfigMapper.toDto(siteConfigRepository.selectById(tempSiteConfig.getId()));
                }
            );
    }

    /**
     * Get all the siteConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<SiteConfigDTO> findAll(Page<SiteConfig> pageable) {
        log.debug("Request to get all SiteConfigs");
        return this.page(pageable).convert(siteConfigMapper::toDto);
    }

    /**
     * Get one siteConfig by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<SiteConfigDTO> findOne(Long id) {
        log.debug("Request to get SiteConfig : {}", id);
        return Optional.ofNullable(siteConfigRepository.selectById(id)).map(siteConfigMapper::toDto);
    }

    /**
     * Delete the siteConfig by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete SiteConfig : {}", id);
        siteConfigRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by siteConfig
     */
    @Transactional
    public SiteConfigDTO updateByIgnoreSpecifiedFields(SiteConfigDTO changeSiteConfigDTO, Set<String> unchangedFields) {
        SiteConfigDTO siteConfigDTO = findOne(changeSiteConfigDTO.getId()).get();
        BeanUtil.copyProperties(changeSiteConfigDTO, siteConfigDTO, unchangedFields.toArray(new String[0]));
        siteConfigDTO = save(siteConfigDTO);
        return siteConfigDTO;
    }

    /**
     * Update specified fields by siteConfig
     */
    @Transactional
    public SiteConfigDTO updateBySpecifiedFields(SiteConfigDTO changeSiteConfigDTO, Set<String> fieldNames) {
        UpdateWrapper<SiteConfig> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeSiteConfigDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                    BeanUtil.getFieldValue(changeSiteConfigDTO, fieldName)
                );
            }
        );
        this.update(updateWrapper);
        return findOne(changeSiteConfigDTO.getId()).get();
    }

    /**
     * Update specified field by siteConfig
     */
    @Transactional
    public SiteConfigDTO updateBySpecifiedField(SiteConfigDTO changeSiteConfigDTO, String fieldName) {
        SiteConfigDTO update = new SiteConfigDTO();
        BeanUtil.setFieldValue(update, "id", changeSiteConfigDTO.getId());
        BeanUtil.setFieldValue(update, fieldName, BeanUtil.getFieldValue(changeSiteConfigDTO, fieldName));
        this.updateEntity(siteConfigMapper.toEntity(update));
        return findOne(changeSiteConfigDTO.getId()).get();
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
