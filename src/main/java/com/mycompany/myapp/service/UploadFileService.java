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
import com.mycompany.myapp.domain.UploadFile;
import com.mycompany.myapp.oss.OssTemplate;
import com.mycompany.myapp.oss.model.BladeFile;
import com.mycompany.myapp.repository.UploadFileRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.dto.UploadFileDTO;
import com.mycompany.myapp.service.mapper.UploadFileMapper;
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
 * Service Implementation for managing {@link UploadFile}.
 */
@Service
public class UploadFileService extends BaseServiceImpl<UploadFileRepository, UploadFile> {

    private final Logger log = LoggerFactory.getLogger(UploadFileService.class);
    private final List<String> relationCacheNames = Arrays.asList(
        com.mycompany.myapp.domain.User.class.getName() + ".uploadFile",
        com.mycompany.myapp.domain.ResourceCategory.class.getName() + ".files"
    );

    private final OssTemplate ossTemplate;

    private final UserRepository userRepository;

    private final UploadFileRepository uploadFileRepository;

    private final CacheManager cacheManager;

    private final UploadFileMapper uploadFileMapper;

    private final ApplicationProperties applicationProperties;

    public UploadFileService(
        OssTemplate ossTemplate,
        UserRepository userRepository,
        UploadFileRepository uploadFileRepository,
        CacheManager cacheManager,
        UploadFileMapper uploadFileMapper,
        ApplicationProperties applicationProperties
    ) {
        this.ossTemplate = ossTemplate;
        this.userRepository = userRepository;
        this.uploadFileRepository = uploadFileRepository;
        this.cacheManager = cacheManager;
        this.uploadFileMapper = uploadFileMapper;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Partially update a uploadFile.
     *
     * @param uploadFileDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<UploadFileDTO> partialUpdate(UploadFileDTO uploadFileDTO) {
        log.debug("Request to partially update UploadFile : {}", uploadFileDTO);

        return uploadFileRepository
            .findById(uploadFileDTO.getId())
            .map(
                existingUploadFile -> {
                    uploadFileMapper.partialUpdate(existingUploadFile, uploadFileDTO);

                    return existingUploadFile;
                }
            )
            .map(
                tempUploadFile -> {
                    uploadFileRepository.save(tempUploadFile);
                    return uploadFileMapper.toDto(uploadFileRepository.selectById(tempUploadFile.getId()));
                }
            );
    }

    /**
     * Get all the uploadFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<UploadFileDTO> findAll(Page<UploadFile> pageable) {
        log.debug("Request to get all UploadFiles");
        return this.page(pageable).convert(uploadFileMapper::toDto);
    }

    /**
     * Save a uploadFile.
     *
     * @param uploadFileDTO the entity to save
     * @return the persisted entity
     */
    @Transactional
    public UploadFileDTO save(UploadFileDTO uploadFileDTO) {
        log.debug("Request to save UploadFile : {}", uploadFileDTO);
        if (!uploadFileDTO.getFile().isEmpty()) {
            final String extName = FilenameUtils.getExtension(uploadFileDTO.getFile().getOriginalFilename());
            final String randomNameNew = UUID.randomUUID().toString().replaceAll("\\-", "");
            final String yearAndMonth = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM"));
            // prettier-ignore
            final String savePathNew = Constants.DATA_PATH + File.separator + applicationProperties.getUpload().getRootPath() + File.separator + yearAndMonth + File.separator ;
            final String saveFileName = savePathNew + randomNameNew + "." + extName;
            // prettier-ignore
            String fileUrl = applicationProperties.getUpload().getDomainUrl() + applicationProperties.getUpload().getRootPath()+ "/" + yearAndMonth + "/" + randomNameNew + "." + extName;
            final long fileSize = uploadFileDTO.getFile().getSize();
            try {
                BladeFile bladeFile = ossTemplate.putFile(
                    uploadFileDTO.getFile().getOriginalFilename(),
                    uploadFileDTO.getFile().getInputStream()
                );
            } catch (Exception e) {
                e.printStackTrace();
                throw new BadRequestAlertException("Invalid file", "UploadFile", "imagesnull");
            }
            uploadFileDTO.setCreateAt(ZonedDateTime.now());
            uploadFileDTO.setExt(extName);
            uploadFileDTO.setFullName(uploadFileDTO.getFile().getOriginalFilename());
            uploadFileDTO.setName(uploadFileDTO.getFile().getName());
            uploadFileDTO.setFolder(savePathNew);
            uploadFileDTO.setUrl(fileUrl);
            uploadFileDTO.setFileSize(fileSize);
        } else {
            throw new BadRequestAlertException("Invalid file", "UploadFile", "imagesnull");
        }
        UploadFile uploadFile = uploadFileMapper.toEntity(uploadFileDTO);
        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(uploadFile::setUser);
        this.saveOrUpdate(uploadFile);
        return uploadFileMapper.toDto(this.getById(uploadFile.getId()));
    }

    /**
     * Get one uploadFile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<UploadFileDTO> findOne(Long id) {
        log.debug("Request to get UploadFile : {}", id);
        return Optional.ofNullable(uploadFileRepository.selectById(id)).map(uploadFileMapper::toDto);
    }

    /**
     * Delete the uploadFile by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete UploadFile : {}", id);
        uploadFileRepository.deleteById(id);
    }

    public List<UploadFileDTO> findByUserIsCurrentUser() {
        return uploadFileMapper.toDto(uploadFileRepository.findByUserIsCurrentUser());
    }

    /**
     * Update ignore specified fields by uploadFile
     */
    @Transactional
    public UploadFileDTO updateByIgnoreSpecifiedFields(UploadFileDTO changeUploadFileDTO, Set<String> unchangedFields) {
        UploadFileDTO uploadFileDTO = findOne(changeUploadFileDTO.getId()).get();
        BeanUtil.copyProperties(changeUploadFileDTO, uploadFileDTO, unchangedFields.toArray(new String[0]));
        uploadFileDTO = save(uploadFileDTO);
        return uploadFileDTO;
    }

    /**
     * Update specified fields by uploadFile
     */
    @Transactional
    public UploadFileDTO updateBySpecifiedFields(UploadFileDTO changeUploadFileDTO, Set<String> fieldNames) {
        UpdateWrapper<UploadFile> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeUploadFileDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                    BeanUtil.getFieldValue(changeUploadFileDTO, fieldName)
                );
            }
        );
        this.update(updateWrapper);
        return findOne(changeUploadFileDTO.getId()).get();
    }

    /**
     * Update specified field by uploadFile
     */
    @Transactional
    public UploadFileDTO updateBySpecifiedField(UploadFileDTO changeUploadFileDTO, String fieldName) {
        UploadFileDTO update = new UploadFileDTO();
        BeanUtil.setFieldValue(update, "id", changeUploadFileDTO.getId());
        BeanUtil.setFieldValue(update, fieldName, BeanUtil.getFieldValue(changeUploadFileDTO, fieldName));
        this.updateEntity(uploadFileMapper.toEntity(update));
        return findOne(changeUploadFileDTO.getId()).get();
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
