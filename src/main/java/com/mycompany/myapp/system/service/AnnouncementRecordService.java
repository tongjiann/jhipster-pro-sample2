package com.mycompany.myapp.system.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.system.domain.AnnouncementRecord;
import com.mycompany.myapp.system.repository.AnnouncementRecordRepository;
import com.mycompany.myapp.system.service.dto.AnnouncementDTO;
import com.mycompany.myapp.system.service.dto.AnnouncementRecordDTO;
import com.mycompany.myapp.system.service.mapper.AnnouncementRecordMapper;
import java.time.ZonedDateTime;
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
 * Service Implementation for managing {@link AnnouncementRecord}.
 */
@Service
public class AnnouncementRecordService extends BaseServiceImpl<AnnouncementRecordRepository, AnnouncementRecord> {

    private final Logger log = LoggerFactory.getLogger(AnnouncementRecordService.class);

    private final AnnouncementRecordRepository announcementRecordRepository;

    private final CacheManager cacheManager;

    private final AnnouncementRecordMapper announcementRecordMapper;

    public AnnouncementRecordService(
        AnnouncementRecordRepository announcementRecordRepository,
        CacheManager cacheManager,
        AnnouncementRecordMapper announcementRecordMapper
    ) {
        this.announcementRecordRepository = announcementRecordRepository;
        this.cacheManager = cacheManager;
        this.announcementRecordMapper = announcementRecordMapper;
    }

    /**
     * Save a announcementRecord.
     *
     * @param announcementRecordDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public AnnouncementRecordDTO save(AnnouncementRecordDTO announcementRecordDTO) {
        log.debug("Request to save AnnouncementRecord : {}", announcementRecordDTO);
        AnnouncementRecord announcementRecord = announcementRecordMapper.toEntity(announcementRecordDTO);
        this.saveOrUpdate(announcementRecord);
        return announcementRecordMapper.toDto(announcementRecord);
    }

    /**
     * Partially update a announcementRecord.
     *
     * @param announcementRecordDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<AnnouncementRecordDTO> partialUpdate(AnnouncementRecordDTO announcementRecordDTO) {
        log.debug("Request to partially update AnnouncementRecord : {}", announcementRecordDTO);

        return announcementRecordRepository
            .findById(announcementRecordDTO.getId())
            .map(
                existingAnnouncementRecord -> {
                    announcementRecordMapper.partialUpdate(existingAnnouncementRecord, announcementRecordDTO);

                    return existingAnnouncementRecord;
                }
            )
            .map(
                tempAnnouncementRecord -> {
                    announcementRecordRepository.save(tempAnnouncementRecord);
                    return announcementRecordMapper.toDto(announcementRecordRepository.selectById(tempAnnouncementRecord.getId()));
                }
            );
    }

    /**
     * Get all the announcementRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<AnnouncementRecordDTO> findAll(Page<AnnouncementRecord> pageable) {
        log.debug("Request to get all AnnouncementRecords");
        return this.page(pageable).convert(announcementRecordMapper::toDto);
    }

    @Transactional
    public void updateRecord(List<AnnouncementDTO> announcementDTOS) {
        Long userId = SecurityUtils.getCurrentUserId().get();
        announcementDTOS.forEach(
            announcementDTO -> {
                Long anntId = announcementDTO.getId();
                if (
                    announcementRecordRepository.selectCount(
                        new LambdaQueryWrapper<AnnouncementRecord>()
                            .eq(AnnouncementRecord::getAnntId, announcementDTO.getId())
                            .eq(AnnouncementRecord::getUserId, userId)
                    ) ==
                    0
                ) {
                    announcementRecordRepository.save(new AnnouncementRecord().anntId(anntId).userId(userId));
                }
            }
        );
    }

    @Transactional
    public void setRead(Long anntId) {
        Long userId = SecurityUtils.getCurrentUserId().get();
        this.update(
                new LambdaUpdateWrapper<AnnouncementRecord>()
                    .set(AnnouncementRecord::getHasRead, true)
                    .set(AnnouncementRecord::getReadTime, ZonedDateTime.now())
                    .eq(AnnouncementRecord::getAnntId, anntId)
                    .eq(AnnouncementRecord::getUserId, userId)
            );
    }

    /**
     * Get one announcementRecord by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<AnnouncementRecordDTO> findOne(Long id) {
        log.debug("Request to get AnnouncementRecord : {}", id);
        return Optional.ofNullable(announcementRecordRepository.selectById(id)).map(announcementRecordMapper::toDto);
    }

    /**
     * Delete the announcementRecord by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete AnnouncementRecord : {}", id);
        announcementRecordRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by announcementRecord
     */
    @Transactional
    public AnnouncementRecordDTO updateByIgnoreSpecifiedFields(
        AnnouncementRecordDTO changeAnnouncementRecordDTO,
        Set<String> unchangedFields
    ) {
        AnnouncementRecordDTO announcementRecordDTO = findOne(changeAnnouncementRecordDTO.getId()).get();
        BeanUtil.copyProperties(changeAnnouncementRecordDTO, announcementRecordDTO, unchangedFields.toArray(new String[0]));
        announcementRecordDTO = save(announcementRecordDTO);
        return announcementRecordDTO;
    }

    /**
     * Update specified fields by announcementRecord
     */
    @Transactional
    public AnnouncementRecordDTO updateBySpecifiedFields(AnnouncementRecordDTO changeAnnouncementRecordDTO, Set<String> fieldNames) {
        UpdateWrapper<AnnouncementRecord> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeAnnouncementRecordDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                    BeanUtil.getFieldValue(changeAnnouncementRecordDTO, fieldName)
                );
            }
        );
        this.update(updateWrapper);
        return findOne(changeAnnouncementRecordDTO.getId()).get();
    }

    /**
     * Update specified field by announcementRecord
     */
    @Transactional
    public AnnouncementRecordDTO updateBySpecifiedField(AnnouncementRecordDTO changeAnnouncementRecordDTO, String fieldName) {
        AnnouncementRecordDTO update = new AnnouncementRecordDTO();
        BeanUtil.setFieldValue(update, "id", changeAnnouncementRecordDTO.getId());
        BeanUtil.setFieldValue(update, fieldName, BeanUtil.getFieldValue(changeAnnouncementRecordDTO, fieldName));
        this.updateEntity(announcementRecordMapper.toEntity(update));
        return findOne(changeAnnouncementRecordDTO.getId()).get();
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
