package com.mycompany.myapp.system.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import com.mycompany.myapp.system.domain.SysLog;
import com.mycompany.myapp.system.repository.SysLogRepository;
import com.mycompany.myapp.system.service.dto.SysLogDTO;
import com.mycompany.myapp.system.service.mapper.SysLogMapper;
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
 * Service Implementation for managing {@link SysLog}.
 */
@Service
public class SysLogService extends BaseServiceImpl<SysLogRepository, SysLog> {

    private final Logger log = LoggerFactory.getLogger(SysLogService.class);

    private final SysLogRepository sysLogRepository;

    private final CacheManager cacheManager;

    private final SysLogMapper sysLogMapper;

    public SysLogService(SysLogRepository sysLogRepository, CacheManager cacheManager, SysLogMapper sysLogMapper) {
        this.sysLogRepository = sysLogRepository;
        this.cacheManager = cacheManager;
        this.sysLogMapper = sysLogMapper;
    }

    /**
     * Save a sysLog.
     *
     * @param sysLogDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public SysLogDTO save(SysLogDTO sysLogDTO) {
        log.debug("Request to save SysLog : {}", sysLogDTO);
        SysLog sysLog = sysLogMapper.toEntity(sysLogDTO);
        this.saveOrUpdate(sysLog);
        return sysLogMapper.toDto(sysLog);
    }

    /**
     * Partially update a sysLog.
     *
     * @param sysLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<SysLogDTO> partialUpdate(SysLogDTO sysLogDTO) {
        log.debug("Request to partially update SysLog : {}", sysLogDTO);

        return sysLogRepository
            .findById(sysLogDTO.getId())
            .map(
                existingSysLog -> {
                    sysLogMapper.partialUpdate(existingSysLog, sysLogDTO);

                    return existingSysLog;
                }
            )
            .map(
                tempSysLog -> {
                    sysLogRepository.save(tempSysLog);
                    return sysLogMapper.toDto(sysLogRepository.selectById(tempSysLog.getId()));
                }
            );
    }

    /**
     * Get all the sysLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<SysLogDTO> findAll(Page<SysLog> pageable) {
        log.debug("Request to get all SysLogs");
        return this.page(pageable).convert(sysLogMapper::toDto);
    }

    /**
     * Get one sysLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<SysLogDTO> findOne(Long id) {
        log.debug("Request to get SysLog : {}", id);
        return Optional.ofNullable(sysLogRepository.selectById(id)).map(sysLogMapper::toDto);
    }

    /**
     * Delete the sysLog by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete SysLog : {}", id);
        sysLogRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by sysLog
     */
    @Transactional
    public SysLogDTO updateByIgnoreSpecifiedFields(SysLogDTO changeSysLogDTO, Set<String> unchangedFields) {
        SysLogDTO sysLogDTO = findOne(changeSysLogDTO.getId()).get();
        BeanUtil.copyProperties(changeSysLogDTO, sysLogDTO, unchangedFields.toArray(new String[0]));
        sysLogDTO = save(sysLogDTO);
        return sysLogDTO;
    }

    /**
     * Update specified fields by sysLog
     */
    @Transactional
    public SysLogDTO updateBySpecifiedFields(SysLogDTO changeSysLogDTO, Set<String> fieldNames) {
        UpdateWrapper<SysLog> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeSysLogDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                    BeanUtil.getFieldValue(changeSysLogDTO, fieldName)
                );
            }
        );
        this.update(updateWrapper);
        return findOne(changeSysLogDTO.getId()).get();
    }

    /**
     * Update specified field by sysLog
     */
    @Transactional
    public SysLogDTO updateBySpecifiedField(SysLogDTO changeSysLogDTO, String fieldName) {
        SysLogDTO update = new SysLogDTO();
        BeanUtil.setFieldValue(update, "id", changeSysLogDTO.getId());
        BeanUtil.setFieldValue(update, fieldName, BeanUtil.getFieldValue(changeSysLogDTO, fieldName));
        this.updateEntity(sysLogMapper.toEntity(update));
        return findOne(changeSysLogDTO.getId()).get();
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
