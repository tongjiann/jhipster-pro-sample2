package com.mycompany.myapp.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.domain.UploadFile;
import com.mycompany.myapp.repository.UploadFileRepository;
import com.mycompany.myapp.service.UploadFileQueryService;
import com.mycompany.myapp.service.UploadFileService;
import com.mycompany.myapp.service.criteria.UploadFileCriteria;
import com.mycompany.myapp.service.dto.UploadFileDTO;
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

 * 管理实体{@link com.mycompany.myapp.domain.UploadFile}的REST Controller。
 */
@RestController
@RequestMapping("/api")
@Api(value = "upload-files", tags = "上传文件API接口")
public class UploadFileResource {

    private final Logger log = LoggerFactory.getLogger(UploadFileResource.class);

    private static final String ENTITY_NAME = "filesUploadFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UploadFileService uploadFileService;

    private final UploadFileRepository uploadFileRepository;

    private final UploadFileQueryService uploadFileQueryService;

    public UploadFileResource(
        UploadFileService uploadFileService,
        UploadFileRepository uploadFileRepository,
        UploadFileQueryService uploadFileQueryService
    ) {
        this.uploadFileService = uploadFileService;
        this.uploadFileRepository = uploadFileRepository;
        this.uploadFileQueryService = uploadFileQueryService;
    }

    /**
     * {@code PUT  /upload-files/:id} : Updates an existing uploadFile.
     *
     * @param id the id of the uploadFileDTO to save.
     * @param uploadFileDTO the uploadFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadFileDTO,
     * or with status {@code 400 (Bad Request)} if the uploadFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uploadFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/upload-files/{id}")
    @ApiOperation(value = "更新上传文件", notes = "根据主键更新并返回一个更新后的上传文件")
    public ResponseEntity<UploadFileDTO> updateUploadFile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UploadFileDTO uploadFileDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UploadFile : {}, {}", id, uploadFileDTO);
        if (uploadFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uploadFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uploadFileService.exists(UploadFile::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UploadFileDTO result = uploadFileService.save(uploadFileDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uploadFileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /upload-files} : Create a new uploadFile.
     *
     * @param file the uploadFile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uploadFileDTO, or with status {@code 400 (Bad Request)} if the uploadFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/upload-files")
    public ResponseEntity<UploadFileDTO> createUploadFile(@RequestParam(value = "file", required = false) final MultipartFile file)
        throws Exception {
        log.debug("REST request to save UploadFile : {}", file.getOriginalFilename());
        if (file.isEmpty()) {
            throw new BadRequestAlertException("A new uploadImage cannot null", ENTITY_NAME, "imageisnull");
        }
        UploadFileDTO uploadFileDTO = new UploadFileDTO();
        uploadFileDTO.setFile(file);
        UploadFileDTO result = uploadFileService.save(uploadFileDTO);
        return ResponseEntity
            .created(new URI("/api/upload-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /upload-files/:id} : Partial updates given fields of an existing uploadFile, field will ignore if it is null.
     *
     * @param id the id of the uploadFileDTO to save.
     * @param uploadFileDTO the uploadFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadFileDTO,
     * or with status {@code 400 (Bad Request)} if the uploadFileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uploadFileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uploadFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(value = "部分更新上传文件", notes = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的上传文件")
    @PatchMapping(value = "/upload-files/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UploadFileDTO> partialUpdateUploadFile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UploadFileDTO uploadFileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UploadFile partially : {}, {}", id, uploadFileDTO);
        if (uploadFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uploadFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (uploadFileRepository.findById(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UploadFileDTO> result = uploadFileService.partialUpdate(uploadFileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uploadFileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /upload-files} : get all the uploadFiles.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uploadFiles in body.
     */
    @GetMapping("/upload-files")
    @ApiOperation(value = "获取上传文件分页列表", notes = "获取上传文件的分页列表数据")
    public ResponseEntity<List<UploadFileDTO>> getAllUploadFiles(
        UploadFileCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get UploadFiles by criteria: {}", criteria);
        IPage<UploadFileDTO> page;
        page = uploadFileQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /upload-files/count} : count all the uploadFiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/upload-files/count")
    public ResponseEntity<Long> countUploadFiles(UploadFileCriteria criteria) {
        log.debug("REST request to count UploadFiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(uploadFileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /upload-files/:id} : get the "id" uploadFile.
     *
     * @param id the id of the uploadFileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uploadFileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/upload-files/{id}")
    @ApiOperation(value = "获取指定主键的上传文件", notes = "获取指定主键的上传文件信息")
    public ResponseEntity<UploadFileDTO> getUploadFile(@PathVariable Long id) {
        log.debug("REST request to get UploadFile : {}", id);
        Optional<UploadFileDTO> uploadFileDTO = uploadFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uploadFileDTO);
    }

    /**
     * GET  /upload-files/export : export the uploadFiles.
     *
     */
    @GetMapping("/upload-files/export")
    @ApiOperation(value = "上传文件EXCEL导出", notes = "导出全部上传文件为EXCEL文件")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<UploadFileDTO> data = uploadFileService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("上传文件一览表", "上传文件"), UploadFileDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "上传文件_" + sdf.format(new Date()) + ".xlsx";
        ExportUtil.excel(workbook, filename, response);
    }

    /**
     * POST  /upload-files/import : import the uploadFiles from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the uploadFileDTO, or with status 404 (Not Found)
     */
    @PostMapping("/upload-files/import")
    @ApiOperation(value = "上传文件EXCEL导入", notes = "根据上传文件EXCEL文件导入全部数据")
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<UploadFileDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), UploadFileDTO.class, params);
        list.forEach(uploadFileService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /upload-files/:id} : delete the "id" uploadFile.
     *
     * @param id the id of the uploadFileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/upload-files/{id}")
    @ApiOperation(value = "删除一个上传文件", notes = "根据主键删除单个上传文件")
    public ResponseEntity<Void> deleteUploadFile(@PathVariable Long id) {
        log.debug("REST request to delete UploadFile : {}", id);
        uploadFileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /upload-files} : delete all the "ids" UploadFiles.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/upload-files")
    @ApiOperation(value = "删除多个上传文件", notes = "根据主键删除多个上传文件")
    public ResponseEntity<Void> deleteUploadFilesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete UploadFiles : {}", ids);
        if (ids != null) {
            ids.forEach(uploadFileService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @ApiOperation(value = "查询user为当前用户的上传文件列表", notes = "查询user为当前用户的上传文件列表")
    @GetMapping("/upload-files/user/current-user")
    public ResponseEntity<List<UploadFileDTO>> findByUserIsCurrentUser() {
        log.debug("REST request to get UploadFile for current user. ");
        List<UploadFileDTO> result = uploadFileService.findByUserIsCurrentUser();
        return ResponseEntity.ok(result);
    }

    /**
     * {@code PUT  /upload-files/specified-fields} : Updates an existing uploadFile by specified fields.
     *
     * @param uploadFileDTOAndSpecifiedFields the uploadFileDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadFileDTO,
     * or with status {@code 400 (Bad Request)} if the uploadFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uploadFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/upload-files/specified-fields")
    @ApiOperation(value = "根据字段部分更新上传文件", notes = "根据指定字段部分更新上传文件，给定的属性值可以为任何值，包括null")
    public ResponseEntity<UploadFileDTO> updateUploadFileDTOBySpecifiedFields(
        @RequestBody UploadFileDTOAndSpecifiedFields uploadFileDTOAndSpecifiedFields
    ) {
        log.debug("REST request to update UploadFileDTO : {}", uploadFileDTOAndSpecifiedFields);
        UploadFileDTO uploadFileDTO = uploadFileDTOAndSpecifiedFields.getUploadFile();
        Set<String> specifiedFields = uploadFileDTOAndSpecifiedFields.getSpecifiedFields();
        if (uploadFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UploadFileDTO result = uploadFileService.updateBySpecifiedFields(uploadFileDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uploadFileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /upload-files/specified-field} : Updates an existing uploadFileDTO by specified field.
     *
     * @param uploadFileDTOAndSpecifiedFields the uploadFileDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadFileDTO,
     * or with status {@code 400 (Bad Request)} if the uploadFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uploadFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/upload-files/specified-field")
    @ApiOperation(value = "更新上传文件单个属性", notes = "根据指定字段更新上传文件，给定的属性值可以为任何值，包括null")
    public ResponseEntity<UploadFileDTO> updateUploadFileBySpecifiedField(
        @RequestBody UploadFileDTOAndSpecifiedFields uploadFileDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update uploadFileDTO : {}", uploadFileDTOAndSpecifiedFields);
        UploadFileDTO uploadFileDTO = uploadFileDTOAndSpecifiedFields.getUploadFile();
        String fieldName = uploadFileDTOAndSpecifiedFields.getSpecifiedField();
        if (uploadFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UploadFileDTO result = uploadFileService.updateBySpecifiedField(uploadFileDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class UploadFileDTOAndSpecifiedFields {

        private UploadFileDTO uploadFile;
        private Set<String> specifiedFields;
        private String specifiedField;

        private UploadFileDTO getUploadFile() {
            return uploadFile;
        }

        private void setUploadFile(UploadFileDTO uploadFile) {
            this.uploadFile = uploadFile;
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
