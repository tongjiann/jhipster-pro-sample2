package com.mycompany.myapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import com.mycompany.myapp.domain.UReportFile;
import com.mycompany.myapp.repository.UReportFileRepository;
import com.mycompany.myapp.service.dto.UReportFileDTO;
import com.mycompany.myapp.service.mapper.UReportFileMapper;
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
 * Service Implementation for managing {@link UReportFile}.
 */
@Service
public class UReportFileService extends BaseServiceImpl<UReportFileRepository, UReportFile> {

    private final Logger log = LoggerFactory.getLogger(UReportFileService.class);

    private final UReportFileRepository uReportFileRepository;

    private final CacheManager cacheManager;

    private final UReportFileMapper uReportFileMapper;

    public UReportFileService(UReportFileRepository uReportFileRepository, CacheManager cacheManager, UReportFileMapper uReportFileMapper) {
        this.uReportFileRepository = uReportFileRepository;
        this.cacheManager = cacheManager;
        this.uReportFileMapper = uReportFileMapper;
    }

    /**
     * Save a uReportFile.
     *
     * @param uReportFileDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public UReportFileDTO save(UReportFileDTO uReportFileDTO) {
        log.debug("Request to save UReportFile : {}", uReportFileDTO);
        UReportFile uReportFile = uReportFileMapper.toEntity(uReportFileDTO);
        this.saveOrUpdate(uReportFile);
        return uReportFileMapper.toDto(uReportFile);
    }

    /**
     * Partially update a uReportFile.
     *
     * @param uReportFileDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<UReportFileDTO> partialUpdate(UReportFileDTO uReportFileDTO) {
        log.debug("Request to partially update UReportFile : {}", uReportFileDTO);

        return uReportFileRepository
            .findById(uReportFileDTO.getId())
            .map(
                existingUReportFile -> {
                    uReportFileMapper.partialUpdate(existingUReportFile, uReportFileDTO);

                    return existingUReportFile;
                }
            )
            .map(
                tempUReportFile -> {
                    uReportFileRepository.save(tempUReportFile);
                    return uReportFileMapper.toDto(uReportFileRepository.selectById(tempUReportFile.getId()));
                }
            );
    }

    /**
     * Get all the uReportFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<UReportFileDTO> findAll(Page<UReportFile> pageable) {
        log.debug("Request to get all UReportFiles");
        return this.page(pageable).convert(uReportFileMapper::toDto);
    }

    /**
     * Get the uReportFile by name.
     *
     * @param name the name of the entity.
     */
    public Optional<UReportFileDTO> getByName(String name) {
        log.debug("Request to delete UReportFile : {}", name);
        return uReportFileRepository.getByName(name).map(uReportFileMapper::toDto);
    }

    /**
     * Delete the uReportFile by name.
     *
     * @param name the name of the entity.
     */
    public void deleteByName(String name) {
        log.debug("Request to delete UReportFile : {}", name);
        uReportFileRepository.deleteByName(name);
    }

    /**
     * Get one uReportFile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<UReportFileDTO> findOne(Long id) {
        log.debug("Request to get UReportFile : {}", id);
        return Optional.ofNullable(uReportFileRepository.selectById(id)).map(uReportFileMapper::toDto);
    }

    /**
     * Delete the uReportFile by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete UReportFile : {}", id);
        uReportFileRepository.deleteById(id);
    }

    /**
     * Update ignore specified fields by uReportFile
     */
    @Transactional
    public UReportFileDTO updateByIgnoreSpecifiedFields(UReportFileDTO changeUReportFileDTO, Set<String> unchangedFields) {
        UReportFileDTO uReportFileDTO = findOne(changeUReportFileDTO.getId()).get();
        BeanUtil.copyProperties(changeUReportFileDTO, uReportFileDTO, unchangedFields.toArray(new String[0]));
        uReportFileDTO = save(uReportFileDTO);
        return uReportFileDTO;
    }

    /**
     * Update specified fields by uReportFile
     */
    @Transactional
    public UReportFileDTO updateBySpecifiedFields(UReportFileDTO changeUReportFileDTO, Set<String> fieldNames) {
        UpdateWrapper<UReportFile> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", changeUReportFileDTO.getId());
        fieldNames.forEach(
            fieldName -> {
                updateWrapper.set(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                    BeanUtil.getFieldValue(changeUReportFileDTO, fieldName)
                );
            }
        );
        this.update(updateWrapper);
        return findOne(changeUReportFileDTO.getId()).get();
    }

    /**
     * Update specified field by uReportFile
     */
    @Transactional
    public UReportFileDTO updateBySpecifiedField(UReportFileDTO changeUReportFileDTO, String fieldName) {
        UReportFileDTO update = new UReportFileDTO();
        BeanUtil.setFieldValue(update, "id", changeUReportFileDTO.getId());
        BeanUtil.setFieldValue(update, fieldName, BeanUtil.getFieldValue(changeUReportFileDTO, fieldName));
        this.updateEntity(uReportFileMapper.toEntity(update));
        return findOne(changeUReportFileDTO.getId()).get();
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
