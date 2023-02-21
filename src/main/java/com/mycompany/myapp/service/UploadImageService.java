package com.mycompany.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import com.mycompany.myapp.config.ApplicationProperties;
import com.mycompany.myapp.config.Constants;
import com.mycompany.myapp.domain.UploadImage;
import com.mycompany.myapp.oss.OssTemplate;
import com.mycompany.myapp.oss.model.BladeFile;
import com.mycompany.myapp.repository.UploadImageRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.dto.UploadImageDTO;
import com.mycompany.myapp.service.mapper.UploadImageMapper;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link UploadImage}.
 */
@Service
public class UploadImageService extends BaseServiceImpl<UploadImageRepository, UploadImage> {

    private final Logger log = LoggerFactory.getLogger(UploadImageService.class);
    private final List<String> relationCacheNames = Arrays.asList(
        com.mycompany.myapp.domain.User.class.getName() + ".uploadImage",
        com.mycompany.myapp.domain.ResourceCategory.class.getName() + ".images"
    );

    private final OssTemplate ossTemplate;

    private final UserRepository userRepository;

    private final UploadImageRepository uploadImageRepository;

    private final CacheManager cacheManager;

    private final UploadImageMapper uploadImageMapper;

    private final ApplicationProperties applicationProperties;

    public UploadImageService(
        OssTemplate ossTemplate,
        UserRepository userRepository,
        UploadImageRepository uploadImageRepository,
        CacheManager cacheManager,
        UploadImageMapper uploadImageMapper,
        ApplicationProperties applicationProperties
    ) {
        this.ossTemplate = ossTemplate;
        this.userRepository = userRepository;
        this.uploadImageRepository = uploadImageRepository;
        this.cacheManager = cacheManager;
        this.uploadImageMapper = uploadImageMapper;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Partially update a uploadImage.
     *
     * @param uploadImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<UploadImageDTO> partialUpdate(UploadImageDTO uploadImageDTO) {
        log.debug("Request to partially update UploadImage : {}", uploadImageDTO);

        return uploadImageRepository
            .findById(uploadImageDTO.getId())
            .map(
                existingUploadImage -> {
                    uploadImageMapper.partialUpdate(existingUploadImage, uploadImageDTO);

                    return existingUploadImage;
                }
            )
            .map(
                tempUploadImage -> {
                    uploadImageRepository.save(tempUploadImage);
                    return uploadImageMapper.toDto(uploadImageRepository.selectById(tempUploadImage.getId()));
                }
            );
    }

    /**
     * Get all the uploadImages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<UploadImageDTO> findAll(Page<UploadImage> pageable) {
        log.debug("Request to get all UploadImages");
        return this.page(pageable).convert(uploadImageMapper::toDto);
    }

    /**
     * Save a uploadImage.
     *
     * @param uploadImageDTO the entity to save
     * @return the persisted entity
     */
    @Transactional
    public UploadImageDTO save(UploadImageDTO uploadImageDTO) {
        log.debug("Request to save UploadImage : {}", uploadImageDTO);
        if (!uploadImageDTO.getImage().isEmpty()) {
            final String extName = FilenameUtils.getExtension(uploadImageDTO.getImage().getOriginalFilename());
            final String yearAndMonth = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM"));
            final String savePathNew =
                Constants.DATA_PATH +
                File.separator +
                applicationProperties.getUpload().getRootPath() +
                File.separator +
                yearAndMonth +
                File.separator;
            BladeFile bladeFile = null;
            try {
                bladeFile =
                    ossTemplate.putFile(uploadImageDTO.getImage().getOriginalFilename(), uploadImageDTO.getImage().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
                throw new BadRequestAlertException("Invalid file", "UploadFile", "imagesnull");
            }
            uploadImageDTO.setCreateAt(ZonedDateTime.now());
            uploadImageDTO.setExt(extName);
            uploadImageDTO.setFullName(uploadImageDTO.getImage().getOriginalFilename());
            uploadImageDTO.setName(uploadImageDTO.getImage().getName());
            uploadImageDTO.setFolder(savePathNew);
            uploadImageDTO.setUrl(bladeFile.getLink());
            uploadImageDTO.setFileSize(uploadImageDTO.getImage().getSize());
        } else {
            throw new BadRequestAlertException("Invalid file", "UploadFile", "imagesnull");
        }
        UploadImage uploadImage = uploadImageMapper.toEntity(uploadImageDTO);
        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(uploadImage::setUser);
        this.saveOrUpdate(uploadImage);
        return uploadImageMapper.toDto(this.getById(uploadImage.getId()));
    }

    /**
     * Get one uploadImage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<UploadImageDTO> findOne(Long id) {
        log.debug("Request to get UploadImage : {}", id);
        return Optional.ofNullable(uploadImageRepository.selectById(id)).map(uploadImageMapper::toDto);
    }

    /**
     * Delete the uploadImage by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete UploadImage : {}", id);
        uploadImageRepository.deleteById(id);
    }

    public List<UploadImageDTO> findByUserIsCurrentUser() {
        return uploadImageMapper.toDto(uploadImageRepository.findByUserIsCurrentUser());
    }

    /**
     * Update ignore specified fields by uploadImage
     */
    @Transactional
    public UploadImageDTO updateByIgnoreSpecifiedFields(UploadImageDTO changeUploadImageDTO, Set<String> unchangedFields) {
        UploadImageDTO uploadImageDTO = findOne(changeUploadImageDTO.getId()).get();
        BeanUtil.copyProperties(changeUploadImageDTO, uploadImageDTO, unchangedFields.toArray(new String[0]));
        uploadImageDTO = save(uploadImageDTO);
        return uploadImageDTO;
    }

    /**
     * Update specified fields by uploadImage
     */
    @Transactional
    public UploadImageDTO updateBySpecifiedFields(UploadImageDTO changeUploadImageDTO, Set<String> fieldNames) {
        UpdateWrapper<UploadImage> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeUploadImageDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                    BeanUtil.getFieldValue(changeUploadImageDTO, fieldName)
                );
            }
        );
        this.update(updateWrapper);
        return findOne(changeUploadImageDTO.getId()).get();
    }

    /**
     * Update specified field by uploadImage
     */
    @Transactional
    public UploadImageDTO updateBySpecifiedField(UploadImageDTO changeUploadImageDTO, String fieldName) {
        UploadImageDTO update = new UploadImageDTO();
        BeanUtil.setFieldValue(update, "id", changeUploadImageDTO.getId());
        BeanUtil.setFieldValue(update, fieldName, BeanUtil.getFieldValue(changeUploadImageDTO, fieldName));
        this.updateEntity(uploadImageMapper.toEntity(update));
        return findOne(changeUploadImageDTO.getId()).get();
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
