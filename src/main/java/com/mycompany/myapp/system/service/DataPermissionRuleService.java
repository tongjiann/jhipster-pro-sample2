package com.mycompany.myapp.system.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import com.mycompany.myapp.system.domain.DataPermissionRule;
import com.mycompany.myapp.system.repository.DataPermissionRuleRepository;
import com.mycompany.myapp.system.service.dto.DataPermissionRuleDTO;
import com.mycompany.myapp.system.service.mapper.DataPermissionRuleMapper;
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
 * Service Implementation for managing {@link DataPermissionRule}.
 */
@Service
public class DataPermissionRuleService extends BaseServiceImpl<DataPermissionRuleRepository, DataPermissionRule> {

    private final Logger log = LoggerFactory.getLogger(DataPermissionRuleService.class);

    private final DataPermissionRuleRepository dataPermissionRuleRepository;

    private final CacheManager cacheManager;

    private final DataPermissionRuleMapper dataPermissionRuleMapper;

    public DataPermissionRuleService(
        DataPermissionRuleRepository dataPermissionRuleRepository,
        CacheManager cacheManager,
        DataPermissionRuleMapper dataPermissionRuleMapper
    ) {
        this.dataPermissionRuleRepository = dataPermissionRuleRepository;
        this.cacheManager = cacheManager;
        this.dataPermissionRuleMapper = dataPermissionRuleMapper;
    }

    /**
     * Save a dataPermissionRule.
     *
     * @param dataPermissionRuleDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public DataPermissionRuleDTO save(DataPermissionRuleDTO dataPermissionRuleDTO) {
        log.debug("Request to save DataPermissionRule : {}", dataPermissionRuleDTO);
        DataPermissionRule dataPermissionRule = dataPermissionRuleMapper.toEntity(dataPermissionRuleDTO);
        this.saveOrUpdate(dataPermissionRule);
        return dataPermissionRuleMapper.toDto(dataPermissionRule);
    }

    /**
     * Partially update a dataPermissionRule.
     *
     * @param dataPermissionRuleDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<DataPermissionRuleDTO> partialUpdate(DataPermissionRuleDTO dataPermissionRuleDTO) {
        log.debug("Request to partially update DataPermissionRule : {}", dataPermissionRuleDTO);

        return dataPermissionRuleRepository
            .findById(dataPermissionRuleDTO.getId())
            .map(
                existingDataPermissionRule -> {
                    dataPermissionRuleMapper.partialUpdate(existingDataPermissionRule, dataPermissionRuleDTO);

                    return existingDataPermissionRule;
                }
            )
            .map(
                tempDataPermissionRule -> {
                    dataPermissionRuleRepository.save(tempDataPermissionRule);
                    return dataPermissionRuleMapper.toDto(dataPermissionRuleRepository.selectById(tempDataPermissionRule.getId()));
                }
            );
    }

    /**
     * Get all the dataPermissionRules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<DataPermissionRuleDTO> findAll(Page<DataPermissionRule> pageable) {
        log.debug("Request to get all DataPermissionRules");
        return this.page(pageable).convert(dataPermissionRuleMapper::toDto);
    }

    /**
     * Get one dataPermissionRule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<DataPermissionRuleDTO> findOne(Long id) {
        log.debug("Request to get DataPermissionRule : {}", id);
        return Optional.ofNullable(dataPermissionRuleRepository.selectById(id)).map(dataPermissionRuleMapper::toDto);
    }

    /**
     * Delete the dataPermissionRule by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete DataPermissionRule : {}", id);
        dataPermissionRuleRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by dataPermissionRule
     */
    @Transactional
    public DataPermissionRuleDTO updateByIgnoreSpecifiedFields(
        DataPermissionRuleDTO changeDataPermissionRuleDTO,
        Set<String> unchangedFields
    ) {
        DataPermissionRuleDTO dataPermissionRuleDTO = findOne(changeDataPermissionRuleDTO.getId()).get();
        BeanUtil.copyProperties(changeDataPermissionRuleDTO, dataPermissionRuleDTO, unchangedFields.toArray(new String[0]));
        dataPermissionRuleDTO = save(dataPermissionRuleDTO);
        return dataPermissionRuleDTO;
    }

    /**
     * Update specified fields by dataPermissionRule
     */
    @Transactional
    public DataPermissionRuleDTO updateBySpecifiedFields(DataPermissionRuleDTO changeDataPermissionRuleDTO, Set<String> fieldNames) {
        UpdateWrapper<DataPermissionRule> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeDataPermissionRuleDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                    BeanUtil.getFieldValue(changeDataPermissionRuleDTO, fieldName)
                );
            }
        );
        this.update(updateWrapper);
        return findOne(changeDataPermissionRuleDTO.getId()).get();
    }

    /**
     * Update specified field by dataPermissionRule
     */
    @Transactional
    public DataPermissionRuleDTO updateBySpecifiedField(DataPermissionRuleDTO changeDataPermissionRuleDTO, String fieldName) {
        DataPermissionRuleDTO update = new DataPermissionRuleDTO();
        BeanUtil.setFieldValue(update, "id", changeDataPermissionRuleDTO.getId());
        BeanUtil.setFieldValue(update, fieldName, BeanUtil.getFieldValue(changeDataPermissionRuleDTO, fieldName));
        this.updateEntity(dataPermissionRuleMapper.toEntity(update));
        return findOne(changeDataPermissionRuleDTO.getId()).get();
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
