package com.mycompany.myapp.system.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.system.domain.AnnouncementRecord;
import com.mycompany.myapp.system.repository.AnnouncementRecordRepository;
import com.mycompany.myapp.system.service.AnnouncementRecordQueryService;
import com.mycompany.myapp.system.service.AnnouncementRecordService;
import com.mycompany.myapp.system.service.criteria.AnnouncementRecordCriteria;
import com.mycompany.myapp.system.service.dto.AnnouncementRecordDTO;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.mycompany.myapp.system.domain.AnnouncementRecord}的REST Controller。
 */
@RestController
@RequestMapping("/api")
@Api(value = "announcement-records", tags = "通告阅读记录API接口")
public class AnnouncementRecordResource {

    private final Logger log = LoggerFactory.getLogger(AnnouncementRecordResource.class);

    private static final String ENTITY_NAME = "announcementRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnnouncementRecordService announcementRecordService;

    private final AnnouncementRecordRepository announcementRecordRepository;

    private final AnnouncementRecordQueryService announcementRecordQueryService;

    public AnnouncementRecordResource(
        AnnouncementRecordService announcementRecordService,
        AnnouncementRecordRepository announcementRecordRepository,
        AnnouncementRecordQueryService announcementRecordQueryService
    ) {
        this.announcementRecordService = announcementRecordService;
        this.announcementRecordRepository = announcementRecordRepository;
        this.announcementRecordQueryService = announcementRecordQueryService;
    }

    /**
     * {@code POST  /announcement-records} : Create a new announcementRecord.
     *
     * @param announcementRecordDTO the announcementRecordDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new announcementRecordDTO, or with status {@code 400 (Bad Request)} if the announcementRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/announcement-records")
    @ApiOperation(value = "新建通告阅读记录", notes = "创建并返回一个新的通告阅读记录")
    public ResponseEntity<AnnouncementRecordDTO> createAnnouncementRecord(@RequestBody AnnouncementRecordDTO announcementRecordDTO)
        throws URISyntaxException {
        log.debug("REST request to save AnnouncementRecord : {}", announcementRecordDTO);
        if (announcementRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new announcementRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnnouncementRecordDTO result = announcementRecordService.save(announcementRecordDTO);
        return ResponseEntity
            .created(new URI("/api/announcement-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /announcement-records/:id} : Updates an existing announcementRecord.
     *
     * @param id the id of the announcementRecordDTO to save.
     * @param announcementRecordDTO the announcementRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated announcementRecordDTO,
     * or with status {@code 400 (Bad Request)} if the announcementRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the announcementRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/announcement-records/{id}")
    @ApiOperation(value = "更新通告阅读记录", notes = "根据主键更新并返回一个更新后的通告阅读记录")
    public ResponseEntity<AnnouncementRecordDTO> updateAnnouncementRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnnouncementRecordDTO announcementRecordDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AnnouncementRecord : {}, {}", id, announcementRecordDTO);
        if (announcementRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, announcementRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!announcementRecordService.exists(AnnouncementRecord::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AnnouncementRecordDTO result = announcementRecordService.save(announcementRecordDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, announcementRecordDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /announcement-records/:id} : Partial updates given fields of an existing announcementRecord, field will ignore if it is null.
     *
     * @param id the id of the announcementRecordDTO to save.
     * @param announcementRecordDTO the announcementRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated announcementRecordDTO,
     * or with status {@code 400 (Bad Request)} if the announcementRecordDTO is not valid,
     * or with status {@code 404 (Not Found)} if the announcementRecordDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the announcementRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(
        value = "部分更新通告阅读记录",
        notes = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的通告阅读记录"
    )
    @PatchMapping(value = "/announcement-records/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AnnouncementRecordDTO> partialUpdateAnnouncementRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnnouncementRecordDTO announcementRecordDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AnnouncementRecord partially : {}, {}", id, announcementRecordDTO);
        if (announcementRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, announcementRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (announcementRecordRepository.findById(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnnouncementRecordDTO> result = announcementRecordService.partialUpdate(announcementRecordDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, announcementRecordDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /announcement-records} : get all the announcementRecords.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of announcementRecords in body.
     */
    @GetMapping("/announcement-records")
    @ApiOperation(value = "获取通告阅读记录分页列表", notes = "获取通告阅读记录的分页列表数据")
    public ResponseEntity<List<AnnouncementRecordDTO>> getAllAnnouncementRecords(
        AnnouncementRecordCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get AnnouncementRecords by criteria: {}", criteria);
        IPage<AnnouncementRecordDTO> page;
        page = announcementRecordQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /announcement-records/count} : count all the announcementRecords.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/announcement-records/count")
    public ResponseEntity<Long> countAnnouncementRecords(AnnouncementRecordCriteria criteria) {
        log.debug("REST request to count AnnouncementRecords by criteria: {}", criteria);
        return ResponseEntity.ok().body(announcementRecordQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /announcement-records/:id} : get the "id" announcementRecord.
     *
     * @param id the id of the announcementRecordDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the announcementRecordDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/announcement-records/{id}")
    @ApiOperation(value = "获取指定主键的通告阅读记录", notes = "获取指定主键的通告阅读记录信息")
    public ResponseEntity<AnnouncementRecordDTO> getAnnouncementRecord(@PathVariable Long id) {
        log.debug("REST request to get AnnouncementRecord : {}", id);
        Optional<AnnouncementRecordDTO> announcementRecordDTO = announcementRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(announcementRecordDTO);
    }

    /**
     * GET  /announcement-records/export : export the announcementRecords.
     *
     */
    @GetMapping("/announcement-records/export")
    @ApiOperation(value = "通告阅读记录EXCEL导出", notes = "导出全部通告阅读记录为EXCEL文件")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<AnnouncementRecordDTO> data = announcementRecordService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(
            new ExportParams("通告阅读记录一览表", "通告阅读记录"),
            AnnouncementRecordDTO.class,
            data
        );
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "通告阅读记录_" + sdf.format(new Date()) + ".xlsx";
        ExportUtil.excel(workbook, filename, response);
    }

    /**
     * POST  /announcement-records/import : import the announcementRecords from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the announcementRecordDTO, or with status 404 (Not Found)
     */
    @PostMapping("/announcement-records/import")
    @ApiOperation(value = "通告阅读记录EXCEL导入", notes = "根据通告阅读记录EXCEL文件导入全部数据")
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<AnnouncementRecordDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), AnnouncementRecordDTO.class, params);
        list.forEach(announcementRecordService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /announcement-records/:id} : delete the "id" announcementRecord.
     *
     * @param id the id of the announcementRecordDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/announcement-records/{id}")
    @ApiOperation(value = "删除一个通告阅读记录", notes = "根据主键删除单个通告阅读记录")
    public ResponseEntity<Void> deleteAnnouncementRecord(@PathVariable Long id) {
        log.debug("REST request to delete AnnouncementRecord : {}", id);
        announcementRecordService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /announcement-records} : delete all the "ids" AnnouncementRecords.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/announcement-records")
    @ApiOperation(value = "删除多个通告阅读记录", notes = "根据主键删除多个通告阅读记录")
    public ResponseEntity<Void> deleteAnnouncementRecordsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete AnnouncementRecords : {}", ids);
        if (ids != null) {
            ids.forEach(announcementRecordService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /announcement-records/specified-fields} : Updates an existing announcementRecord by specified fields.
     *
     * @param announcementRecordDTOAndSpecifiedFields the announcementRecordDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated announcementRecordDTO,
     * or with status {@code 400 (Bad Request)} if the announcementRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the announcementRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/announcement-records/specified-fields")
    @ApiOperation(value = "根据字段部分更新通告阅读记录", notes = "根据指定字段部分更新通告阅读记录，给定的属性值可以为任何值，包括null")
    public ResponseEntity<AnnouncementRecordDTO> updateAnnouncementRecordDTOBySpecifiedFields(
        @RequestBody AnnouncementRecordDTOAndSpecifiedFields announcementRecordDTOAndSpecifiedFields
    ) {
        log.debug("REST request to update AnnouncementRecordDTO : {}", announcementRecordDTOAndSpecifiedFields);
        AnnouncementRecordDTO announcementRecordDTO = announcementRecordDTOAndSpecifiedFields.getAnnouncementRecord();
        Set<String> specifiedFields = announcementRecordDTOAndSpecifiedFields.getSpecifiedFields();
        if (announcementRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnnouncementRecordDTO result = announcementRecordService.updateBySpecifiedFields(announcementRecordDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, announcementRecordDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /announcement-records/specified-field} : Updates an existing announcementRecordDTO by specified field.
     *
     * @param announcementRecordDTOAndSpecifiedFields the announcementRecordDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated announcementRecordDTO,
     * or with status {@code 400 (Bad Request)} if the announcementRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the announcementRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/announcement-records/specified-field")
    @ApiOperation(value = "更新通告阅读记录单个属性", notes = "根据指定字段更新通告阅读记录，给定的属性值可以为任何值，包括null")
    public ResponseEntity<AnnouncementRecordDTO> updateAnnouncementRecordBySpecifiedField(
        @RequestBody AnnouncementRecordDTOAndSpecifiedFields announcementRecordDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update announcementRecordDTO : {}", announcementRecordDTOAndSpecifiedFields);
        AnnouncementRecordDTO announcementRecordDTO = announcementRecordDTOAndSpecifiedFields.getAnnouncementRecord();
        String fieldName = announcementRecordDTOAndSpecifiedFields.getSpecifiedField();
        if (announcementRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnnouncementRecordDTO result = announcementRecordService.updateBySpecifiedField(announcementRecordDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class AnnouncementRecordDTOAndSpecifiedFields {

        private AnnouncementRecordDTO announcementRecord;
        private Set<String> specifiedFields;
        private String specifiedField;

        private AnnouncementRecordDTO getAnnouncementRecord() {
            return announcementRecord;
        }

        private void setAnnouncementRecord(AnnouncementRecordDTO announcementRecord) {
            this.announcementRecord = announcementRecord;
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
