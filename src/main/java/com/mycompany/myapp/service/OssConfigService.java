package com.mycompany.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import com.mycompany.myapp.domain.OssConfig;
import com.mycompany.myapp.repository.OssConfigRepository;
import com.mycompany.myapp.service.dto.OssConfigDTO;
import com.mycompany.myapp.service.mapper.OssConfigMapper;
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
 * Service Implementation for managing {@link OssConfig}.
 */
@Service
public class OssConfigService extends BaseServiceImpl<OssConfigRepository, OssConfig> {

    private final Logger log = LoggerFactory.getLogger(OssConfigService.class);

    private final OssConfigRepository ossConfigRepository;

    private final CacheManager cacheManager;

    private final OssConfigMapper ossConfigMapper;

    public OssConfigService(OssConfigRepository ossConfigRepository, CacheManager cacheManager, OssConfigMapper ossConfigMapper) {
        this.ossConfigRepository = ossConfigRepository;
        this.cacheManager = cacheManager;
        this.ossConfigMapper = ossConfigMapper;
    }

    /**
     * Save a ossConfig.
     *
     * @param ossConfigDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public OssConfigDTO save(OssConfigDTO ossConfigDTO) {
        log.debug("Request to save OssConfig : {}", ossConfigDTO);
        OssConfig ossConfig = ossConfigMapper.toEntity(ossConfigDTO);
        this.saveOrUpdate(ossConfig);
        return ossConfigMapper.toDto(ossConfig);
    }

    /**
     * Partially update a ossConfig.
     *
     * @param ossConfigDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<OssConfigDTO> partialUpdate(OssConfigDTO ossConfigDTO) {
        log.debug("Request to partially update OssConfig : {}", ossConfigDTO);

        return ossConfigRepository
            .findById(ossConfigDTO.getId())
            .map(
                existingOssConfig -> {
                    ossConfigMapper.partialUpdate(existingOssConfig, ossConfigDTO);

                    return existingOssConfig;
                }
            )
            .map(
                tempOssConfig -> {
                    ossConfigRepository.save(tempOssConfig);
                    return ossConfigMapper.toDto(ossConfigRepository.selectById(tempOssConfig.getId()));
                }
            );
    }

    /**
     * Get all the ossConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<OssConfigDTO> findAll(Page<OssConfig> pageable) {
        log.debug("Request to get all OssConfigs");
        return this.page(pageable).convert(ossConfigMapper::toDto);
    }

    /**
     * Get one ossConfig by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<OssConfigDTO> findOne(Long id) {
        log.debug("Request to get OssConfig : {}", id);
        return Optional.ofNullable(ossConfigRepository.selectById(id)).map(ossConfigMapper::toDto);
    }

    /**
     * Delete the ossConfig by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete OssConfig : {}", id);
        ossConfigRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by ossConfig
     */
    @Transactional
    public OssConfigDTO updateByIgnoreSpecifiedFields(OssConfigDTO changeOssConfigDTO, Set<String> unchangedFields) {
        OssConfigDTO ossConfigDTO = findOne(changeOssConfigDTO.getId()).get();
        BeanUtil.copyProperties(changeOssConfigDTO, ossConfigDTO, unchangedFields.toArray(new String[0]));
        ossConfigDTO = save(ossConfigDTO);
        return ossConfigDTO;
    }

    /**
     * Update specified fields by ossConfig
     */
    @Transactional
    public OssConfigDTO updateBySpecifiedFields(OssConfigDTO changeOssConfigDTO, Set<String> fieldNames) {
        UpdateWrapper<OssConfig> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeOssConfigDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                    BeanUtil.getFieldValue(changeOssConfigDTO, fieldName)
                );
            }
        );
        this.update(updateWrapper);
        return findOne(changeOssConfigDTO.getId()).get();
    }

    /**
     * Update specified field by ossConfig
     */
    @Transactional
    public OssConfigDTO updateBySpecifiedField(OssConfigDTO changeOssConfigDTO, String fieldName) {
        OssConfigDTO update = new OssConfigDTO();
        BeanUtil.setFieldValue(update, "id", changeOssConfigDTO.getId());
        BeanUtil.setFieldValue(update, fieldName, BeanUtil.getFieldValue(changeOssConfigDTO, fieldName));
        this.updateEntity(ossConfigMapper.toEntity(update));
        return findOne(changeOssConfigDTO.getId()).get();
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
