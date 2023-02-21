package com.mycompany.myapp.system.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import com.mycompany.myapp.system.domain.SmsTemplate;
import com.mycompany.myapp.system.repository.SmsTemplateRepository;
import com.mycompany.myapp.system.service.dto.SmsTemplateDTO;
import com.mycompany.myapp.system.service.mapper.SmsTemplateMapper;
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
 * Service Implementation for managing {@link SmsTemplate}.
 */
@Service
public class SmsTemplateService extends BaseServiceImpl<SmsTemplateRepository, SmsTemplate> {

    private final Logger log = LoggerFactory.getLogger(SmsTemplateService.class);

    private final SmsTemplateRepository smsTemplateRepository;

    private final CacheManager cacheManager;

    private final SmsTemplateMapper smsTemplateMapper;

    public SmsTemplateService(SmsTemplateRepository smsTemplateRepository, CacheManager cacheManager, SmsTemplateMapper smsTemplateMapper) {
        this.smsTemplateRepository = smsTemplateRepository;
        this.cacheManager = cacheManager;
        this.smsTemplateMapper = smsTemplateMapper;
    }

    /**
     * Save a smsTemplate.
     *
     * @param smsTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public SmsTemplateDTO save(SmsTemplateDTO smsTemplateDTO) {
        log.debug("Request to save SmsTemplate : {}", smsTemplateDTO);
        SmsTemplate smsTemplate = smsTemplateMapper.toEntity(smsTemplateDTO);
        this.saveOrUpdate(smsTemplate);
        return smsTemplateMapper.toDto(smsTemplate);
    }

    /**
     * Partially update a smsTemplate.
     *
     * @param smsTemplateDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<SmsTemplateDTO> partialUpdate(SmsTemplateDTO smsTemplateDTO) {
        log.debug("Request to partially update SmsTemplate : {}", smsTemplateDTO);

        return smsTemplateRepository
            .findById(smsTemplateDTO.getId())
            .map(
                existingSmsTemplate -> {
                    smsTemplateMapper.partialUpdate(existingSmsTemplate, smsTemplateDTO);

                    return existingSmsTemplate;
                }
            )
            .map(
                tempSmsTemplate -> {
                    smsTemplateRepository.save(tempSmsTemplate);
                    return smsTemplateMapper.toDto(smsTemplateRepository.selectById(tempSmsTemplate.getId()));
                }
            );
    }

    /**
     * Get all the smsTemplates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<SmsTemplateDTO> findAll(Page<SmsTemplate> pageable) {
        log.debug("Request to get all SmsTemplates");
        return this.page(pageable).convert(smsTemplateMapper::toDto);
    }

    /**
     * Get one smsTemplate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<SmsTemplateDTO> findOne(Long id) {
        log.debug("Request to get SmsTemplate : {}", id);
        return Optional.ofNullable(smsTemplateRepository.selectById(id)).map(smsTemplateMapper::toDto);
    }

    /**
     * Delete the smsTemplate by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete SmsTemplate : {}", id);
        smsTemplateRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by smsTemplate
     */
    @Transactional
    public SmsTemplateDTO updateByIgnoreSpecifiedFields(SmsTemplateDTO changeSmsTemplateDTO, Set<String> unchangedFields) {
        SmsTemplateDTO smsTemplateDTO = findOne(changeSmsTemplateDTO.getId()).get();
        BeanUtil.copyProperties(changeSmsTemplateDTO, smsTemplateDTO, unchangedFields.toArray(new String[0]));
        smsTemplateDTO = save(smsTemplateDTO);
        return smsTemplateDTO;
    }

    /**
     * Update specified fields by smsTemplate
     */
    @Transactional
    public SmsTemplateDTO updateBySpecifiedFields(SmsTemplateDTO changeSmsTemplateDTO, Set<String> fieldNames) {
        UpdateWrapper<SmsTemplate> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeSmsTemplateDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                    BeanUtil.getFieldValue(changeSmsTemplateDTO, fieldName)
                );
            }
        );
        this.update(updateWrapper);
        return findOne(changeSmsTemplateDTO.getId()).get();
    }

    /**
     * Update specified field by smsTemplate
     */
    @Transactional
    public SmsTemplateDTO updateBySpecifiedField(SmsTemplateDTO changeSmsTemplateDTO, String fieldName) {
        SmsTemplateDTO update = new SmsTemplateDTO();
        BeanUtil.setFieldValue(update, "id", changeSmsTemplateDTO.getId());
        BeanUtil.setFieldValue(update, fieldName, BeanUtil.getFieldValue(changeSmsTemplateDTO, fieldName));
        this.updateEntity(smsTemplateMapper.toEntity(update));
        return findOne(changeSmsTemplateDTO.getId()).get();
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
