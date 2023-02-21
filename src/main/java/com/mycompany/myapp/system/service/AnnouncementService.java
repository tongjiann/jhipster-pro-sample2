package com.mycompany.myapp.system.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import com.mycompany.myapp.domain.enumeration.AnnoCategory;
import com.mycompany.myapp.domain.enumeration.AnnoSendStatus;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.UserQueryService;
import com.mycompany.myapp.service.criteria.UserCriteria;
import com.mycompany.myapp.system.domain.Announcement;
import com.mycompany.myapp.system.domain.AnnouncementRecord;
import com.mycompany.myapp.system.repository.AnnouncementRecordRepository;
import com.mycompany.myapp.system.repository.AnnouncementRepository;
import com.mycompany.myapp.system.service.dto.AnnouncementDTO;
import com.mycompany.myapp.system.service.mapper.AnnouncementMapper;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
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
 * Service Implementation for managing {@link Announcement}.
 */
@Service
public class AnnouncementService extends BaseServiceImpl<AnnouncementRepository, Announcement> {

    private final Logger log = LoggerFactory.getLogger(AnnouncementService.class);

    private final UserQueryService userQueryService;

    private final AnnouncementRecordRepository announcementRecordRepository;

    private final AnnouncementRepository announcementRepository;

    private final CacheManager cacheManager;

    private final AnnouncementMapper announcementMapper;

    public AnnouncementService(
        UserQueryService userQueryService,
        AnnouncementRecordRepository announcementRecordRepository,
        AnnouncementRepository announcementRepository,
        CacheManager cacheManager,
        AnnouncementMapper announcementMapper
    ) {
        this.userQueryService = userQueryService;
        this.announcementRecordRepository = announcementRecordRepository;
        this.announcementRepository = announcementRepository;
        this.cacheManager = cacheManager;
        this.announcementMapper = announcementMapper;
    }

    /**
     * Save a announcement.
     *
     * @param announcementDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public AnnouncementDTO save(AnnouncementDTO announcementDTO) {
        log.debug("Request to save Announcement : {}", announcementDTO);
        Announcement announcement = announcementMapper.toEntity(announcementDTO);
        this.saveOrUpdate(announcement);
        return announcementMapper.toDto(announcement);
    }

    /**
     * Partially update a announcement.
     *
     * @param announcementDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<AnnouncementDTO> partialUpdate(AnnouncementDTO announcementDTO) {
        log.debug("Request to partially update Announcement : {}", announcementDTO);

        return announcementRepository
            .findById(announcementDTO.getId())
            .map(
                existingAnnouncement -> {
                    announcementMapper.partialUpdate(existingAnnouncement, announcementDTO);

                    return existingAnnouncement;
                }
            )
            .map(
                tempAnnouncement -> {
                    announcementRepository.save(tempAnnouncement);
                    return announcementMapper.toDto(announcementRepository.selectById(tempAnnouncement.getId()));
                }
            );
    }

    /**
     * Get all the announcements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<AnnouncementDTO> findAll(Page<Announcement> pageable) {
        log.debug("Request to get all Announcements");
        return this.page(pageable).convert(announcementMapper::toDto);
    }

    @Transactional
    public void release(Long id) {
        Announcement announcement = announcementRepository.selectById(id);
        if (announcement != null) {
            announcement
                .sendStatus(AnnoSendStatus.RELEASED)
                .sendTime(ZonedDateTime.now())
                .senderId(SecurityUtils.getCurrentUserId().orElse(null));
            announcementRepository.save(announcement);
            List<AnnouncementRecord> records = new ArrayList<>();
            ZonedDateTime sendTime = ZonedDateTime.now();
            Long[] userIds = {};
            UserCriteria criteria = new UserCriteria();
            switch (announcement.getReceiverType()) {
                case ALL:
                    return;
                case USER:
                    userIds = announcement.getReceiverIds();
                    break;
                case POSITION:
                    criteria.positionId().setIn(Arrays.asList(announcement.getReceiverIds()));
                    userIds = userQueryService.getFieldByCriteria(Long.class, "id", true, criteria).toArray(userIds);
                    break;
                case DEPARTMENT:
                    criteria.departmentId().setIn(Arrays.asList(announcement.getReceiverIds()));
                    userIds = userQueryService.getFieldByCriteria(Long.class, "id", true, criteria).toArray(userIds);
                    break;
                case AUTHORITY:
                    criteria.authoritiesId().setIn(Arrays.asList(announcement.getReceiverIds()));
                    userIds = userQueryService.getFieldByCriteria(Long.class, "id", true, criteria).toArray(userIds);
                    break;
            }

            for (Long userId : userIds) {
                announcementRecordRepository.save(new AnnouncementRecord().anntId(announcement.getId()).userId(userId).hasRead(false));
            }
        } else {
            throw new BadRequestAlertException("未找到指定Id的通知", "Announcement", "IdNotFound");
        }
    }

    /**
     * Get one announcement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<AnnouncementDTO> findOne(Long id) {
        log.debug("Request to get Announcement : {}", id);
        return Optional.ofNullable(announcementRepository.selectById(id)).map(announcementMapper::toDto);
    }

    /**
     * Delete the announcement by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Announcement : {}", id);
        announcementRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by announcement
     */
    @Transactional
    public AnnouncementDTO updateByIgnoreSpecifiedFields(AnnouncementDTO changeAnnouncementDTO, Set<String> unchangedFields) {
        AnnouncementDTO announcementDTO = findOne(changeAnnouncementDTO.getId()).get();
        BeanUtil.copyProperties(changeAnnouncementDTO, announcementDTO, unchangedFields.toArray(new String[0]));
        announcementDTO = save(announcementDTO);
        return announcementDTO;
    }

    /**
     * Update specified fields by announcement
     */
    @Transactional
    public AnnouncementDTO updateBySpecifiedFields(AnnouncementDTO changeAnnouncementDTO, Set<String> fieldNames) {
        UpdateWrapper<Announcement> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeAnnouncementDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                    BeanUtil.getFieldValue(changeAnnouncementDTO, fieldName)
                );
            }
        );
        this.update(updateWrapper);
        return findOne(changeAnnouncementDTO.getId()).get();
    }

    /**
     * Update specified field by announcement
     */
    @Transactional
    public AnnouncementDTO updateBySpecifiedField(AnnouncementDTO changeAnnouncementDTO, String fieldName) {
        AnnouncementDTO update = new AnnouncementDTO();
        BeanUtil.setFieldValue(update, "id", changeAnnouncementDTO.getId());
        BeanUtil.setFieldValue(update, fieldName, BeanUtil.getFieldValue(changeAnnouncementDTO, fieldName));
        this.updateEntity(announcementMapper.toEntity(update));
        return findOne(changeAnnouncementDTO.getId()).get();
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
