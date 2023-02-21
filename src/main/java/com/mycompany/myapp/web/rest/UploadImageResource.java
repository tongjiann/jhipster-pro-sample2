package com.mycompany.myapp.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.domain.UploadImage;
import com.mycompany.myapp.repository.UploadImageRepository;
import com.mycompany.myapp.service.UploadImageQueryService;
import com.mycompany.myapp.service.UploadImageService;
import com.mycompany.myapp.service.criteria.UploadImageCriteria;
import com.mycompany.myapp.service.dto.ResourceCategoryDTO;
import com.mycompany.myapp.service.dto.UploadImageDTO;
import com.mycompany.myapp.util.ExportUtil;
import com.mycompany.myapp.util.web.IPageUtil;
import com.mycompany.myapp.util.web.PageableUtils;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.mycompany.myapp.domain.UploadImage}的REST Controller。
 */
@RestController
@RequestMapping("/api")
@Api(value = "upload-images", tags = "上传图片API接口")
public class UploadImageResource {

    private final Logger log = LoggerFactory.getLogger(UploadImageResource.class);

    private static final String ENTITY_NAME = "filesUploadImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UploadImageService uploadImageService;

    private final UploadImageRepository uploadImageRepository;

    private final UploadImageQueryService uploadImageQueryService;

    public UploadImageResource(
        UploadImageService uploadImageService,
        UploadImageRepository uploadImageRepository,
        UploadImageQueryService uploadImageQueryService
    ) {
        this.uploadImageService = uploadImageService;
        this.uploadImageRepository = uploadImageRepository;
        this.uploadImageQueryService = uploadImageQueryService;
    }

    /**
     * {@code PUT  /upload-images/:id} : Updates an existing uploadImage.
     *
     * @param id the id of the uploadImageDTO to save.
     * @param uploadImageDTO the uploadImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadImageDTO,
     * or with status {@code 400 (Bad Request)} if the uploadImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uploadImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/upload-images/{id}")
    @ApiOperation(value = "更新上传图片", notes = "根据主键更新并返回一个更新后的上传图片")
    public ResponseEntity<UploadImageDTO> updateUploadImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UploadImageDTO uploadImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UploadImage : {}, {}", id, uploadImageDTO);
        if (uploadImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uploadImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uploadImageService.exists(UploadImage::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UploadImageDTO result = uploadImageService.save(uploadImageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uploadImageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /upload-images} : Create a new uploadImage.
     *
     * @param image the uploadImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uploadImageDTO, or with status {@code 400 (Bad Request)} if the uploadImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/upload-images")
    public ResponseEntity<UploadImageDTO> createUploadImage(
        @RequestParam(value = "image", required = false) final MultipartFile image,
        @RequestParam(value = "categoryId", required = false) Long categoryId
    ) throws URISyntaxException {
        log.debug("REST request to save UploadImage : {}", image.getOriginalFilename());
        if (image.isEmpty()) {
            throw new BadRequestAlertException("A new uploadImage cannot null", ENTITY_NAME, "imageisnull");
        }
        UploadImageDTO uploadImageDTO = new UploadImageDTO();
        uploadImageDTO.setImage(image);
        if (categoryId != null) {
            ResourceCategoryDTO resourceCategoryDTO = new ResourceCategoryDTO();
            resourceCategoryDTO.setId(categoryId);
            uploadImageDTO.setCategory(resourceCategoryDTO);
        }
        UploadImageDTO result = null;
        try {
            result = uploadImageService.save(uploadImageDTO);
        } catch (Exception e) {
            throw new BadRequestAlertException("UploadImageError", ENTITY_NAME, "fileError");
        }
        return ResponseEntity
            .created(new URI("/api/upload-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /upload-images/:id} : Partial updates given fields of an existing uploadImage, field will ignore if it is null.
     *
     * @param id the id of the uploadImageDTO to save.
     * @param uploadImageDTO the uploadImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadImageDTO,
     * or with status {@code 400 (Bad Request)} if the uploadImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uploadImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uploadImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(value = "部分更新上传图片", notes = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的上传图片")
    @PatchMapping(value = "/upload-images/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UploadImageDTO> partialUpdateUploadImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UploadImageDTO uploadImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UploadImage partially : {}, {}", id, uploadImageDTO);
        if (uploadImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uploadImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (uploadImageRepository.findById(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UploadImageDTO> result = uploadImageService.partialUpdate(uploadImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uploadImageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /upload-images} : get all the uploadImages.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uploadImages in body.
     */
    @GetMapping("/upload-images")
    @ApiOperation(value = "获取上传图片分页列表", notes = "获取上传图片的分页列表数据")
    public ResponseEntity<List<UploadImageDTO>> getAllUploadImages(
        UploadImageCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get UploadImages by criteria: {}", criteria);
        IPage<UploadImageDTO> page;
        page = uploadImageQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /upload-images/count} : count all the uploadImages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/upload-images/count")
    public ResponseEntity<Long> countUploadImages(UploadImageCriteria criteria) {
        log.debug("REST request to count UploadImages by criteria: {}", criteria);
        return ResponseEntity.ok().body(uploadImageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /upload-images/:id} : get the "id" uploadImage.
     *
     * @param id the id of the uploadImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uploadImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/upload-images/{id}")
    @ApiOperation(value = "获取指定主键的上传图片", notes = "获取指定主键的上传图片信息")
    public ResponseEntity<UploadImageDTO> getUploadImage(@PathVariable Long id) {
        log.debug("REST request to get UploadImage : {}", id);
        Optional<UploadImageDTO> uploadImageDTO = uploadImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uploadImageDTO);
    }

    /**
     * GET  /upload-images/export : export the uploadImages.
     *
     */
    @GetMapping("/upload-images/export")
    @ApiOperation(value = "上传图片EXCEL导出", notes = "导出全部上传图片为EXCEL文件")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<UploadImageDTO> data = uploadImageService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("上传图片一览表", "上传图片"), UploadImageDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "上传图片_" + sdf.format(new Date()) + ".xlsx";
        ExportUtil.excel(workbook, filename, response);
    }

    /**
     * POST  /upload-images/import : import the uploadImages from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the uploadImageDTO, or with status 404 (Not Found)
     */
    @PostMapping("/upload-images/import")
    @ApiOperation(value = "上传图片EXCEL导入", notes = "根据上传图片EXCEL文件导入全部数据")
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<UploadImageDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), UploadImageDTO.class, params);
        list.forEach(uploadImageService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /upload-images/:id} : delete the "id" uploadImage.
     *
     * @param id the id of the uploadImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/upload-images/{id}")
    @ApiOperation(value = "删除一个上传图片", notes = "根据主键删除单个上传图片")
    public ResponseEntity<Void> deleteUploadImage(@PathVariable Long id) {
        log.debug("REST request to delete UploadImage : {}", id);
        uploadImageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /upload-images} : delete all the "ids" UploadImages.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/upload-images")
    @ApiOperation(value = "删除多个上传图片", notes = "根据主键删除多个上传图片")
    public ResponseEntity<Void> deleteUploadImagesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete UploadImages : {}", ids);
        if (ids != null) {
            ids.forEach(uploadImageService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @ApiOperation(value = "查询user为当前用户的上传图片列表", notes = "查询user为当前用户的上传图片列表")
    @GetMapping("/upload-images/user/current-user")
    public ResponseEntity<List<UploadImageDTO>> findByUserIsCurrentUser() {
        log.debug("REST request to get UploadImage for current user. ");
        List<UploadImageDTO> result = uploadImageService.findByUserIsCurrentUser();
        return ResponseEntity.ok(result);
    }

    /**
     * {@code PUT  /upload-images/specified-fields} : Updates an existing uploadImage by specified fields.
     *
     * @param uploadImageDTOAndSpecifiedFields the uploadImageDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadImageDTO,
     * or with status {@code 400 (Bad Request)} if the uploadImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uploadImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/upload-images/specified-fields")
    @ApiOperation(value = "根据字段部分更新上传图片", notes = "根据指定字段部分更新上传图片，给定的属性值可以为任何值，包括null")
    public ResponseEntity<UploadImageDTO> updateUploadImageDTOBySpecifiedFields(
        @RequestBody UploadImageDTOAndSpecifiedFields uploadImageDTOAndSpecifiedFields
    ) {
        log.debug("REST request to update UploadImageDTO : {}", uploadImageDTOAndSpecifiedFields);
        UploadImageDTO uploadImageDTO = uploadImageDTOAndSpecifiedFields.getUploadImage();
        Set<String> specifiedFields = uploadImageDTOAndSpecifiedFields.getSpecifiedFields();
        if (uploadImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UploadImageDTO result = uploadImageService.updateBySpecifiedFields(uploadImageDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uploadImageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /upload-images/specified-field} : Updates an existing uploadImageDTO by specified field.
     *
     * @param uploadImageDTOAndSpecifiedFields the uploadImageDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadImageDTO,
     * or with status {@code 400 (Bad Request)} if the uploadImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uploadImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/upload-images/specified-field")
    @ApiOperation(value = "更新上传图片单个属性", notes = "根据指定字段更新上传图片，给定的属性值可以为任何值，包括null")
    public ResponseEntity<UploadImageDTO> updateUploadImageBySpecifiedField(
        @RequestBody UploadImageDTOAndSpecifiedFields uploadImageDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update uploadImageDTO : {}", uploadImageDTOAndSpecifiedFields);
        UploadImageDTO uploadImageDTO = uploadImageDTOAndSpecifiedFields.getUploadImage();
        String fieldName = uploadImageDTOAndSpecifiedFields.getSpecifiedField();
        if (uploadImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UploadImageDTO result = uploadImageService.updateBySpecifiedField(uploadImageDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class UploadImageDTOAndSpecifiedFields {

        private UploadImageDTO uploadImage;
        private Set<String> specifiedFields;
        private String specifiedField;

        private UploadImageDTO getUploadImage() {
            return uploadImage;
        }

        private void setUploadImage(UploadImageDTO uploadImage) {
            this.uploadImage = uploadImage;
        }

        private Set<String> getSpecifiedFields() {
            return specifiedFields;
        }

        private void setSpecifiedFields(Set<String> specifiedFields) {
            this.specifiedFields = specifiedFields;
        }

        public String getSpecifiedField() {
            return specifiedField;
        }

        public void setSpecifiedField(String specifiedField) {
            this.specifiedField = specifiedField;
        }
    }
}
