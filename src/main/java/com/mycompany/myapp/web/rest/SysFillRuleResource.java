package com.mycompany.myapp.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.domain.SysFillRule;
import com.mycompany.myapp.repository.SysFillRuleRepository;
import com.mycompany.myapp.service.SysFillRuleQueryService;
import com.mycompany.myapp.service.SysFillRuleService;
import com.mycompany.myapp.service.criteria.SysFillRuleCriteria;
import com.mycompany.myapp.service.dto.SysFillRuleDTO;
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

 * 管理实体{@link com.mycompany.myapp.domain.SysFillRule}的REST Controller。
 */
@RestController
@RequestMapping("/api")
@Api(value = "sys-fill-rules", tags = "填充规则API接口")
public class SysFillRuleResource {

    private final Logger log = LoggerFactory.getLogger(SysFillRuleResource.class);

    private static final String ENTITY_NAME = "settingsSysFillRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SysFillRuleService sysFillRuleService;

    private final SysFillRuleRepository sysFillRuleRepository;

    private final SysFillRuleQueryService sysFillRuleQueryService;

    public SysFillRuleResource(
        SysFillRuleService sysFillRuleService,
        SysFillRuleRepository sysFillRuleRepository,
        SysFillRuleQueryService sysFillRuleQueryService
    ) {
        this.sysFillRuleService = sysFillRuleService;
        this.sysFillRuleRepository = sysFillRuleRepository;
        this.sysFillRuleQueryService = sysFillRuleQueryService;
    }

    /**
     * {@code POST  /sys-fill-rules} : Create a new sysFillRule.
     *
     * @param sysFillRuleDTO the sysFillRuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sysFillRuleDTO, or with status {@code 400 (Bad Request)} if the sysFillRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sys-fill-rules")
    @ApiOperation(value = "新建填充规则", notes = "创建并返回一个新的填充规则")
    public ResponseEntity<SysFillRuleDTO> createSysFillRule(@RequestBody SysFillRuleDTO sysFillRuleDTO) throws URISyntaxException {
        log.debug("REST request to save SysFillRule : {}", sysFillRuleDTO);
        if (sysFillRuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new sysFillRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SysFillRuleDTO result = sysFillRuleService.save(sysFillRuleDTO);
        return ResponseEntity
            .created(new URI("/api/sys-fill-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sys-fill-rules/:id} : Updates an existing sysFillRule.
     *
     * @param id the id of the sysFillRuleDTO to save.
     * @param sysFillRuleDTO the sysFillRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysFillRuleDTO,
     * or with status {@code 400 (Bad Request)} if the sysFillRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysFillRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sys-fill-rules/{id}")
    @ApiOperation(value = "更新填充规则", notes = "根据主键更新并返回一个更新后的填充规则")
    public ResponseEntity<SysFillRuleDTO> updateSysFillRule(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SysFillRuleDTO sysFillRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SysFillRule : {}, {}", id, sysFillRuleDTO);
        if (sysFillRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sysFillRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sysFillRuleService.exists(SysFillRule::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SysFillRuleDTO result = sysFillRuleService.save(sysFillRuleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sysFillRuleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sys-fill-rules/:id} : Partial updates given fields of an existing sysFillRule, field will ignore if it is null.
     *
     * @param id the id of the sysFillRuleDTO to save.
     * @param sysFillRuleDTO the sysFillRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysFillRuleDTO,
     * or with status {@code 400 (Bad Request)} if the sysFillRuleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sysFillRuleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sysFillRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(value = "部分更新填充规则", notes = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的填充规则")
    @PatchMapping(value = "/sys-fill-rules/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SysFillRuleDTO> partialUpdateSysFillRule(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SysFillRuleDTO sysFillRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SysFillRule partially : {}, {}", id, sysFillRuleDTO);
        if (sysFillRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sysFillRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (sysFillRuleRepository.findById(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SysFillRuleDTO> result = sysFillRuleService.partialUpdate(sysFillRuleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sysFillRuleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sys-fill-rules} : get all the sysFillRules.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sysFillRules in body.
     */
    @GetMapping("/sys-fill-rules")
    @ApiOperation(value = "获取填充规则分页列表", notes = "获取填充规则的分页列表数据")
    public ResponseEntity<List<SysFillRuleDTO>> getAllSysFillRules(
        SysFillRuleCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get SysFillRules by criteria: {}", criteria);
        IPage<SysFillRuleDTO> page;
        page = sysFillRuleQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /sys-fill-rules/count} : count all the sysFillRules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sys-fill-rules/count")
    public ResponseEntity<Long> countSysFillRules(SysFillRuleCriteria criteria) {
        log.debug("REST request to count SysFillRules by criteria: {}", criteria);
        return ResponseEntity.ok().body(sysFillRuleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sys-fill-rules/:id} : get the "id" sysFillRule.
     *
     * @param id the id of the sysFillRuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sysFillRuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sys-fill-rules/{id}")
    @ApiOperation(value = "获取指定主键的填充规则", notes = "获取指定主键的填充规则信息")
    public ResponseEntity<SysFillRuleDTO> getSysFillRule(@PathVariable Long id) {
        log.debug("REST request to get SysFillRule : {}", id);
        Optional<SysFillRuleDTO> sysFillRuleDTO = sysFillRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sysFillRuleDTO);
    }

    /**
     * GET  /sys-fill-rules/export : export the sysFillRules.
     *
     */
    @GetMapping("/sys-fill-rules/export")
    @ApiOperation(value = "填充规则EXCEL导出", notes = "导出全部填充规则为EXCEL文件")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<SysFillRuleDTO> data = sysFillRuleService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("填充规则一览表", "填充规则"), SysFillRuleDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "填充规则_" + sdf.format(new Date()) + ".xlsx";
        ExportUtil.excel(workbook, filename, response);
    }

    /**
     * POST  /sys-fill-rules/import : import the sysFillRules from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the sysFillRuleDTO, or with status 404 (Not Found)
     */
    @PostMapping("/sys-fill-rules/import")
    @ApiOperation(value = "填充规则EXCEL导入", notes = "根据填充规则EXCEL文件导入全部数据")
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<SysFillRuleDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), SysFillRuleDTO.class, params);
        list.forEach(sysFillRuleService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /sys-fill-rules/:id} : delete the "id" sysFillRule.
     *
     * @param id the id of the sysFillRuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sys-fill-rules/{id}")
    @ApiOperation(value = "删除一个填充规则", notes = "根据主键删除单个填充规则")
    public ResponseEntity<Void> deleteSysFillRule(@PathVariable Long id) {
        log.debug("REST request to delete SysFillRule : {}", id);
        sysFillRuleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /sys-fill-rules} : delete all the "ids" SysFillRules.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sys-fill-rules")
    @ApiOperation(value = "删除多个填充规则", notes = "根据主键删除多个填充规则")
    public ResponseEntity<Void> deleteSysFillRulesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete SysFillRules : {}", ids);
        if (ids != null) {
            ids.forEach(sysFillRuleService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /sys-fill-rules/specified-fields} : Updates an existing sysFillRule by specified fields.
     *
     * @param sysFillRuleDTOAndSpecifiedFields the sysFillRuleDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysFillRuleDTO,
     * or with status {@code 400 (Bad Request)} if the sysFillRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysFillRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sys-fill-rules/specified-fields")
    @ApiOperation(value = "根据字段部分更新填充规则", notes = "根据指定字段部分更新填充规则，给定的属性值可以为任何值，包括null")
    public ResponseEntity<SysFillRuleDTO> updateSysFillRuleDTOBySpecifiedFields(
        @RequestBody SysFillRuleDTOAndSpecifiedFields sysFillRuleDTOAndSpecifiedFields
    ) {
        log.debug("REST request to update SysFillRuleDTO : {}", sysFillRuleDTOAndSpecifiedFields);
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleDTOAndSpecifiedFields.getSysFillRule();
        Set<String> specifiedFields = sysFillRuleDTOAndSpecifiedFields.getSpecifiedFields();
        if (sysFillRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SysFillRuleDTO result = sysFillRuleService.updateBySpecifiedFields(sysFillRuleDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sysFillRuleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sys-fill-rules/specified-field} : Updates an existing sysFillRuleDTO by specified field.
     *
     * @param sysFillRuleDTOAndSpecifiedFields the sysFillRuleDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysFillRuleDTO,
     * or with status {@code 400 (Bad Request)} if the sysFillRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysFillRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sys-fill-rules/specified-field")
    @ApiOperation(value = "更新填充规则单个属性", notes = "根据指定字段更新填充规则，给定的属性值可以为任何值，包括null")
    public ResponseEntity<SysFillRuleDTO> updateSysFillRuleBySpecifiedField(
        @RequestBody SysFillRuleDTOAndSpecifiedFields sysFillRuleDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update sysFillRuleDTO : {}", sysFillRuleDTOAndSpecifiedFields);
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleDTOAndSpecifiedFields.getSysFillRule();
        String fieldName = sysFillRuleDTOAndSpecifiedFields.getSpecifiedField();
        if (sysFillRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SysFillRuleDTO result = sysFillRuleService.updateBySpecifiedField(sysFillRuleDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class SysFillRuleDTOAndSpecifiedFields {

        private SysFillRuleDTO sysFillRule;
        private Set<String> specifiedFields;
        private String specifiedField;

        private SysFillRuleDTO getSysFillRule() {
            return sysFillRule;
        }

        private void setSysFillRule(SysFillRuleDTO sysFillRule) {
            this.sysFillRule = sysFillRule;
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
