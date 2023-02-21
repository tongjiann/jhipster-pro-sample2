package com.mycompany.myapp.system.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.system.domain.SmsMessage;
import com.mycompany.myapp.system.repository.SmsMessageRepository;
import com.mycompany.myapp.system.service.SmsMessageQueryService;
import com.mycompany.myapp.system.service.SmsMessageService;
import com.mycompany.myapp.system.service.criteria.SmsMessageCriteria;
import com.mycompany.myapp.system.service.dto.SmsMessageDTO;
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

 * 管理实体{@link com.mycompany.myapp.system.domain.SmsMessage}的REST Controller。
 */
@RestController
@RequestMapping("/api")
@Api(value = "sms-messages", tags = "短信消息API接口")
public class SmsMessageResource {

    private final Logger log = LoggerFactory.getLogger(SmsMessageResource.class);

    private static final String ENTITY_NAME = "smsMessage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SmsMessageService smsMessageService;

    private final SmsMessageRepository smsMessageRepository;

    private final SmsMessageQueryService smsMessageQueryService;

    public SmsMessageResource(
        SmsMessageService smsMessageService,
        SmsMessageRepository smsMessageRepository,
        SmsMessageQueryService smsMessageQueryService
    ) {
        this.smsMessageService = smsMessageService;
        this.smsMessageRepository = smsMessageRepository;
        this.smsMessageQueryService = smsMessageQueryService;
    }

    /**
     * {@code POST  /sms-messages} : Create a new smsMessage.
     *
     * @param smsMessageDTO the smsMessageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new smsMessageDTO, or with status {@code 400 (Bad Request)} if the smsMessage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sms-messages")
    @ApiOperation(value = "新建短信消息", notes = "创建并返回一个新的短信消息")
    public ResponseEntity<SmsMessageDTO> createSmsMessage(@RequestBody SmsMessageDTO smsMessageDTO) throws URISyntaxException {
        log.debug("REST request to save SmsMessage : {}", smsMessageDTO);
        if (smsMessageDTO.getId() != null) {
            throw new BadRequestAlertException("A new smsMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmsMessageDTO result = smsMessageService.save(smsMessageDTO);
        return ResponseEntity
            .created(new URI("/api/sms-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sms-messages/:id} : Updates an existing smsMessage.
     *
     * @param id the id of the smsMessageDTO to save.
     * @param smsMessageDTO the smsMessageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsMessageDTO,
     * or with status {@code 400 (Bad Request)} if the smsMessageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the smsMessageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sms-messages/{id}")
    @ApiOperation(value = "更新短信消息", notes = "根据主键更新并返回一个更新后的短信消息")
    public ResponseEntity<SmsMessageDTO> updateSmsMessage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SmsMessageDTO smsMessageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SmsMessage : {}, {}", id, smsMessageDTO);
        if (smsMessageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, smsMessageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!smsMessageService.exists(SmsMessage::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SmsMessageDTO result = smsMessageService.save(smsMessageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, smsMessageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sms-messages/:id} : Partial updates given fields of an existing smsMessage, field will ignore if it is null.
     *
     * @param id the id of the smsMessageDTO to save.
     * @param smsMessageDTO the smsMessageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsMessageDTO,
     * or with status {@code 400 (Bad Request)} if the smsMessageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the smsMessageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the smsMessageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(value = "部分更新短信消息", notes = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的短信消息")
    @PatchMapping(value = "/sms-messages/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SmsMessageDTO> partialUpdateSmsMessage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SmsMessageDTO smsMessageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SmsMessage partially : {}, {}", id, smsMessageDTO);
        if (smsMessageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, smsMessageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (smsMessageRepository.findById(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SmsMessageDTO> result = smsMessageService.partialUpdate(smsMessageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, smsMessageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sms-messages} : get all the smsMessages.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of smsMessages in body.
     */
    @GetMapping("/sms-messages")
    @ApiOperation(value = "获取短信消息分页列表", notes = "获取短信消息的分页列表数据")
    public ResponseEntity<List<SmsMessageDTO>> getAllSmsMessages(
        SmsMessageCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get SmsMessages by criteria: {}", criteria);
        IPage<SmsMessageDTO> page;
        page = smsMessageQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /sms-messages/count} : count all the smsMessages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sms-messages/count")
    public ResponseEntity<Long> countSmsMessages(SmsMessageCriteria criteria) {
        log.debug("REST request to count SmsMessages by criteria: {}", criteria);
        return ResponseEntity.ok().body(smsMessageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sms-messages/:id} : get the "id" smsMessage.
     *
     * @param id the id of the smsMessageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the smsMessageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sms-messages/{id}")
    @ApiOperation(value = "获取指定主键的短信消息", notes = "获取指定主键的短信消息信息")
    public ResponseEntity<SmsMessageDTO> getSmsMessage(@PathVariable Long id) {
        log.debug("REST request to get SmsMessage : {}", id);
        Optional<SmsMessageDTO> smsMessageDTO = smsMessageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smsMessageDTO);
    }

    /**
     * GET  /sms-messages/export : export the smsMessages.
     *
     */
    @GetMapping("/sms-messages/export")
    @ApiOperation(value = "短信消息EXCEL导出", notes = "导出全部短信消息为EXCEL文件")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<SmsMessageDTO> data = smsMessageService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("短信消息一览表", "短信消息"), SmsMessageDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "短信消息_" + sdf.format(new Date()) + ".xlsx";
        ExportUtil.excel(workbook, filename, response);
    }

    /**
     * POST  /sms-messages/import : import the smsMessages from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the smsMessageDTO, or with status 404 (Not Found)
     */
    @PostMapping("/sms-messages/import")
    @ApiOperation(value = "短信消息EXCEL导入", notes = "根据短信消息EXCEL文件导入全部数据")
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<SmsMessageDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), SmsMessageDTO.class, params);
        list.forEach(smsMessageService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /sms-messages/:id} : delete the "id" smsMessage.
     *
     * @param id the id of the smsMessageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sms-messages/{id}")
    @ApiOperation(value = "删除一个短信消息", notes = "根据主键删除单个短信消息")
    public ResponseEntity<Void> deleteSmsMessage(@PathVariable Long id) {
        log.debug("REST request to delete SmsMessage : {}", id);
        smsMessageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /sms-messages} : delete all the "ids" SmsMessages.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sms-messages")
    @ApiOperation(value = "删除多个短信消息", notes = "根据主键删除多个短信消息")
    public ResponseEntity<Void> deleteSmsMessagesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete SmsMessages : {}", ids);
        if (ids != null) {
            ids.forEach(smsMessageService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /sms-messages/specified-fields} : Updates an existing smsMessage by specified fields.
     *
     * @param smsMessageDTOAndSpecifiedFields the smsMessageDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsMessageDTO,
     * or with status {@code 400 (Bad Request)} if the smsMessageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the smsMessageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sms-messages/specified-fields")
    @ApiOperation(value = "根据字段部分更新短信消息", notes = "根据指定字段部分更新短信消息，给定的属性值可以为任何值，包括null")
    public ResponseEntity<SmsMessageDTO> updateSmsMessageDTOBySpecifiedFields(
        @RequestBody SmsMessageDTOAndSpecifiedFields smsMessageDTOAndSpecifiedFields
    ) {
        log.debug("REST request to update SmsMessageDTO : {}", smsMessageDTOAndSpecifiedFields);
        SmsMessageDTO smsMessageDTO = smsMessageDTOAndSpecifiedFields.getSmsMessage();
        Set<String> specifiedFields = smsMessageDTOAndSpecifiedFields.getSpecifiedFields();
        if (smsMessageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmsMessageDTO result = smsMessageService.updateBySpecifiedFields(smsMessageDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, smsMessageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sms-messages/specified-field} : Updates an existing smsMessageDTO by specified field.
     *
     * @param smsMessageDTOAndSpecifiedFields the smsMessageDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsMessageDTO,
     * or with status {@code 400 (Bad Request)} if the smsMessageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the smsMessageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sms-messages/specified-field")
    @ApiOperation(value = "更新短信消息单个属性", notes = "根据指定字段更新短信消息，给定的属性值可以为任何值，包括null")
    public ResponseEntity<SmsMessageDTO> updateSmsMessageBySpecifiedField(
        @RequestBody SmsMessageDTOAndSpecifiedFields smsMessageDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update smsMessageDTO : {}", smsMessageDTOAndSpecifiedFields);
        SmsMessageDTO smsMessageDTO = smsMessageDTOAndSpecifiedFields.getSmsMessage();
        String fieldName = smsMessageDTOAndSpecifiedFields.getSpecifiedField();
        if (smsMessageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmsMessageDTO result = smsMessageService.updateBySpecifiedField(smsMessageDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class SmsMessageDTOAndSpecifiedFields {

        private SmsMessageDTO smsMessage;
        private Set<String> specifiedFields;
        private String specifiedField;

        private SmsMessageDTO getSmsMessage() {
            return smsMessage;
        }

        private void setSmsMessage(SmsMessageDTO smsMessage) {
            this.smsMessage = smsMessage;
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
