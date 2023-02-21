package com.mycompany.myapp.system.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.system.domain.SysLog;
import com.mycompany.myapp.system.repository.SysLogRepository;
import com.mycompany.myapp.system.service.SysLogQueryService;
import com.mycompany.myapp.system.service.SysLogService;
import com.mycompany.myapp.system.service.criteria.SysLogCriteria;
import com.mycompany.myapp.system.service.dto.SysLogDTO;
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

 * 管理实体{@link com.mycompany.myapp.system.domain.SysLog}的REST Controller。
 */
@RestController
@RequestMapping("/api")
@Api(value = "sys-logs", tags = "系统日志API接口")
public class SysLogResource {

    private final Logger log = LoggerFactory.getLogger(SysLogResource.class);

    private static final String ENTITY_NAME = "sysLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SysLogService sysLogService;

    private final SysLogRepository sysLogRepository;

    private final SysLogQueryService sysLogQueryService;

    public SysLogResource(SysLogService sysLogService, SysLogRepository sysLogRepository, SysLogQueryService sysLogQueryService) {
        this.sysLogService = sysLogService;
        this.sysLogRepository = sysLogRepository;
        this.sysLogQueryService = sysLogQueryService;
    }

    /**
     * {@code POST  /sys-logs} : Create a new sysLog.
     *
     * @param sysLogDTO the sysLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sysLogDTO, or with status {@code 400 (Bad Request)} if the sysLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sys-logs")
    @ApiOperation(value = "新建系统日志", notes = "创建并返回一个新的系统日志")
    public ResponseEntity<SysLogDTO> createSysLog(@RequestBody SysLogDTO sysLogDTO) throws URISyntaxException {
        log.debug("REST request to save SysLog : {}", sysLogDTO);
        if (sysLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new sysLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SysLogDTO result = sysLogService.save(sysLogDTO);
        return ResponseEntity
            .created(new URI("/api/sys-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sys-logs/:id} : Updates an existing sysLog.
     *
     * @param id the id of the sysLogDTO to save.
     * @param sysLogDTO the sysLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysLogDTO,
     * or with status {@code 400 (Bad Request)} if the sysLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sys-logs/{id}")
    @ApiOperation(value = "更新系统日志", notes = "根据主键更新并返回一个更新后的系统日志")
    public ResponseEntity<SysLogDTO> updateSysLog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SysLogDTO sysLogDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SysLog : {}, {}", id, sysLogDTO);
        if (sysLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sysLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sysLogService.exists(SysLog::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SysLogDTO result = sysLogService.save(sysLogDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sysLogDTO.getId().toString()))
            .body(result);
    }

    /**
     * 获取访客统计
     * @return ResponseEntity<List<Map<String,Object>>>
     */
    @GetMapping("/sys-logs/visit-info")
    public ResponseEntity<List<Map<String, Object>>> visitInfo() {
        return ResponseEntity.ok(sysLogQueryService.countVisit());
    }

    /**
     * 获取访问量统计
     */
    @GetMapping("/sys-logs/stats")
    public ResponseEntity<Map<String, Object>> loginfo() {
        return ResponseEntity.ok(sysLogQueryService.logInfo());
    }

    /**
     * {@code PATCH  /sys-logs/:id} : Partial updates given fields of an existing sysLog, field will ignore if it is null.
     *
     * @param id the id of the sysLogDTO to save.
     * @param sysLogDTO the sysLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysLogDTO,
     * or with status {@code 400 (Bad Request)} if the sysLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sysLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sysLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(value = "部分更新系统日志", notes = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的系统日志")
    @PatchMapping(value = "/sys-logs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SysLogDTO> partialUpdateSysLog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SysLogDTO sysLogDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SysLog partially : {}, {}", id, sysLogDTO);
        if (sysLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sysLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (sysLogRepository.findById(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SysLogDTO> result = sysLogService.partialUpdate(sysLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sysLogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sys-logs} : get all the sysLogs.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sysLogs in body.
     */
    @GetMapping("/sys-logs")
    @ApiOperation(value = "获取系统日志分页列表", notes = "获取系统日志的分页列表数据")
    public ResponseEntity<List<SysLogDTO>> getAllSysLogs(
        SysLogCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get SysLogs by criteria: {}", criteria);
        IPage<SysLogDTO> page;
        page = sysLogQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /sys-logs/count} : count all the sysLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sys-logs/count")
    public ResponseEntity<Long> countSysLogs(SysLogCriteria criteria) {
        log.debug("REST request to count SysLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(sysLogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sys-logs/:id} : get the "id" sysLog.
     *
     * @param id the id of the sysLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sysLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sys-logs/{id}")
    @ApiOperation(value = "获取指定主键的系统日志", notes = "获取指定主键的系统日志信息")
    public ResponseEntity<SysLogDTO> getSysLog(@PathVariable Long id) {
        log.debug("REST request to get SysLog : {}", id);
        Optional<SysLogDTO> sysLogDTO = sysLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sysLogDTO);
    }

    /**
     * GET  /sys-logs/export : export the sysLogs.
     *
     */
    @GetMapping("/sys-logs/export")
    @ApiOperation(value = "系统日志EXCEL导出", notes = "导出全部系统日志为EXCEL文件")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<SysLogDTO> data = sysLogService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("系统日志一览表", "系统日志"), SysLogDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "系统日志_" + sdf.format(new Date()) + ".xlsx";
        ExportUtil.excel(workbook, filename, response);
    }

    /**
     * POST  /sys-logs/import : import the sysLogs from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the sysLogDTO, or with status 404 (Not Found)
     */
    @PostMapping("/sys-logs/import")
    @ApiOperation(value = "系统日志EXCEL导入", notes = "根据系统日志EXCEL文件导入全部数据")
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<SysLogDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), SysLogDTO.class, params);
        list.forEach(sysLogService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /sys-logs/:id} : delete the "id" sysLog.
     *
     * @param id the id of the sysLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sys-logs/{id}")
    @ApiOperation(value = "删除一个系统日志", notes = "根据主键删除单个系统日志")
    public ResponseEntity<Void> deleteSysLog(@PathVariable Long id) {
        log.debug("REST request to delete SysLog : {}", id);
        sysLogService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /sys-logs} : delete all the "ids" SysLogs.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sys-logs")
    @ApiOperation(value = "删除多个系统日志", notes = "根据主键删除多个系统日志")
    public ResponseEntity<Void> deleteSysLogsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete SysLogs : {}", ids);
        if (ids != null) {
            ids.forEach(sysLogService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /sys-logs/specified-fields} : Updates an existing sysLog by specified fields.
     *
     * @param sysLogDTOAndSpecifiedFields the sysLogDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysLogDTO,
     * or with status {@code 400 (Bad Request)} if the sysLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sys-logs/specified-fields")
    @ApiOperation(value = "根据字段部分更新系统日志", notes = "根据指定字段部分更新系统日志，给定的属性值可以为任何值，包括null")
    public ResponseEntity<SysLogDTO> updateSysLogDTOBySpecifiedFields(
        @RequestBody SysLogDTOAndSpecifiedFields sysLogDTOAndSpecifiedFields
    ) {
        log.debug("REST request to update SysLogDTO : {}", sysLogDTOAndSpecifiedFields);
        SysLogDTO sysLogDTO = sysLogDTOAndSpecifiedFields.getSysLog();
        Set<String> specifiedFields = sysLogDTOAndSpecifiedFields.getSpecifiedFields();
        if (sysLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SysLogDTO result = sysLogService.updateBySpecifiedFields(sysLogDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sysLogDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sys-logs/specified-field} : Updates an existing sysLogDTO by specified field.
     *
     * @param sysLogDTOAndSpecifiedFields the sysLogDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysLogDTO,
     * or with status {@code 400 (Bad Request)} if the sysLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sys-logs/specified-field")
    @ApiOperation(value = "更新系统日志单个属性", notes = "根据指定字段更新系统日志，给定的属性值可以为任何值，包括null")
    public ResponseEntity<SysLogDTO> updateSysLogBySpecifiedField(@RequestBody SysLogDTOAndSpecifiedFields sysLogDTOAndSpecifiedFields)
        throws URISyntaxException {
        log.debug("REST request to update sysLogDTO : {}", sysLogDTOAndSpecifiedFields);
        SysLogDTO sysLogDTO = sysLogDTOAndSpecifiedFields.getSysLog();
        String fieldName = sysLogDTOAndSpecifiedFields.getSpecifiedField();
        if (sysLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SysLogDTO result = sysLogService.updateBySpecifiedField(sysLogDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class SysLogDTOAndSpecifiedFields {

        private SysLogDTO sysLog;
        private Set<String> specifiedFields;
        private String specifiedField;

        private SysLogDTO getSysLog() {
            return sysLog;
        }

        private void setSysLog(SysLogDTO sysLog) {
            this.sysLog = sysLog;
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
