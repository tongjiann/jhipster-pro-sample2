package com.mycompany.myapp.system.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.system.domain.DataPermissionRule;
import com.mycompany.myapp.system.repository.DataPermissionRuleRepository;
import com.mycompany.myapp.system.service.DataPermissionRuleQueryService;
import com.mycompany.myapp.system.service.DataPermissionRuleService;
import com.mycompany.myapp.system.service.criteria.DataPermissionRuleCriteria;
import com.mycompany.myapp.system.service.dto.DataPermissionRuleDTO;
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

 * ????????????{@link com.mycompany.myapp.system.domain.DataPermissionRule}???REST Controller???
 */
@RestController
@RequestMapping("/api")
@Api(value = "data-permission-rules", tags = "??????????????????API??????")
public class DataPermissionRuleResource {

    private final Logger log = LoggerFactory.getLogger(DataPermissionRuleResource.class);

    private static final String ENTITY_NAME = "dataPermissionRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DataPermissionRuleService dataPermissionRuleService;

    private final DataPermissionRuleRepository dataPermissionRuleRepository;

    private final DataPermissionRuleQueryService dataPermissionRuleQueryService;

    public DataPermissionRuleResource(
        DataPermissionRuleService dataPermissionRuleService,
        DataPermissionRuleRepository dataPermissionRuleRepository,
        DataPermissionRuleQueryService dataPermissionRuleQueryService
    ) {
        this.dataPermissionRuleService = dataPermissionRuleService;
        this.dataPermissionRuleRepository = dataPermissionRuleRepository;
        this.dataPermissionRuleQueryService = dataPermissionRuleQueryService;
    }

    /**
     * {@code POST  /data-permission-rules} : Create a new dataPermissionRule.
     *
     * @param dataPermissionRuleDTO the dataPermissionRuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dataPermissionRuleDTO, or with status {@code 400 (Bad Request)} if the dataPermissionRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/data-permission-rules")
    @ApiOperation(value = "????????????????????????", notes = "?????????????????????????????????????????????")
    public ResponseEntity<DataPermissionRuleDTO> createDataPermissionRule(@RequestBody DataPermissionRuleDTO dataPermissionRuleDTO)
        throws URISyntaxException {
        log.debug("REST request to save DataPermissionRule : {}", dataPermissionRuleDTO);
        if (dataPermissionRuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new dataPermissionRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DataPermissionRuleDTO result = dataPermissionRuleService.save(dataPermissionRuleDTO);
        return ResponseEntity
            .created(new URI("/api/data-permission-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /data-permission-rules/:id} : Updates an existing dataPermissionRule.
     *
     * @param id the id of the dataPermissionRuleDTO to save.
     * @param dataPermissionRuleDTO the dataPermissionRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataPermissionRuleDTO,
     * or with status {@code 400 (Bad Request)} if the dataPermissionRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataPermissionRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/data-permission-rules/{id}")
    @ApiOperation(value = "????????????????????????", notes = "???????????????????????????????????????????????????????????????")
    public ResponseEntity<DataPermissionRuleDTO> updateDataPermissionRule(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DataPermissionRuleDTO dataPermissionRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DataPermissionRule : {}, {}", id, dataPermissionRuleDTO);
        if (dataPermissionRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataPermissionRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataPermissionRuleService.exists(DataPermissionRule::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DataPermissionRuleDTO result = dataPermissionRuleService.save(dataPermissionRuleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dataPermissionRuleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /data-permission-rules/:id} : Partial updates given fields of an existing dataPermissionRule, field will ignore if it is null.
     *
     * @param id the id of the dataPermissionRuleDTO to save.
     * @param dataPermissionRuleDTO the dataPermissionRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataPermissionRuleDTO,
     * or with status {@code 400 (Bad Request)} if the dataPermissionRuleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dataPermissionRuleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dataPermissionRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(
        value = "??????????????????????????????",
        notes = "??????????????????????????????????????????????????????null??????????????????????????????????????????????????????????????????"
    )
    @PatchMapping(value = "/data-permission-rules/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DataPermissionRuleDTO> partialUpdateDataPermissionRule(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DataPermissionRuleDTO dataPermissionRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DataPermissionRule partially : {}, {}", id, dataPermissionRuleDTO);
        if (dataPermissionRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataPermissionRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (dataPermissionRuleRepository.findById(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DataPermissionRuleDTO> result = dataPermissionRuleService.partialUpdate(dataPermissionRuleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dataPermissionRuleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /data-permission-rules} : get all the dataPermissionRules.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dataPermissionRules in body.
     */
    @GetMapping("/data-permission-rules")
    @ApiOperation(value = "????????????????????????????????????", notes = "?????????????????????????????????????????????")
    public ResponseEntity<List<DataPermissionRuleDTO>> getAllDataPermissionRules(
        DataPermissionRuleCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get DataPermissionRules by criteria: {}", criteria);
        IPage<DataPermissionRuleDTO> page;
        page = dataPermissionRuleQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /data-permission-rules/count} : count all the dataPermissionRules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/data-permission-rules/count")
    public ResponseEntity<Long> countDataPermissionRules(DataPermissionRuleCriteria criteria) {
        log.debug("REST request to count DataPermissionRules by criteria: {}", criteria);
        return ResponseEntity.ok().body(dataPermissionRuleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /data-permission-rules/:id} : get the "id" dataPermissionRule.
     *
     * @param id the id of the dataPermissionRuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dataPermissionRuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/data-permission-rules/{id}")
    @ApiOperation(value = "???????????????????????????????????????", notes = "?????????????????????????????????????????????")
    public ResponseEntity<DataPermissionRuleDTO> getDataPermissionRule(@PathVariable Long id) {
        log.debug("REST request to get DataPermissionRule : {}", id);
        Optional<DataPermissionRuleDTO> dataPermissionRuleDTO = dataPermissionRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dataPermissionRuleDTO);
    }

    /**
     * GET  /data-permission-rules/export : export the dataPermissionRules.
     *
     */
    @GetMapping("/data-permission-rules/export")
    @ApiOperation(value = "??????????????????EXCEL??????", notes = "?????????????????????????????????EXCEL??????")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<DataPermissionRuleDTO> data = dataPermissionRuleService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(
            new ExportParams("???????????????????????????", "??????????????????"),
            DataPermissionRuleDTO.class,
            data
        );
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "??????????????????_" + sdf.format(new Date()) + ".xlsx";
        ExportUtil.excel(workbook, filename, response);
    }

    /**
     * POST  /data-permission-rules/import : import the dataPermissionRules from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the dataPermissionRuleDTO, or with status 404 (Not Found)
     */
    @PostMapping("/data-permission-rules/import")
    @ApiOperation(value = "??????????????????EXCEL??????", notes = "????????????????????????EXCEL????????????????????????")
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<DataPermissionRuleDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), DataPermissionRuleDTO.class, params);
        list.forEach(dataPermissionRuleService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /data-permission-rules/:id} : delete the "id" dataPermissionRule.
     *
     * @param id the id of the dataPermissionRuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/data-permission-rules/{id}")
    @ApiOperation(value = "??????????????????????????????", notes = "??????????????????????????????????????????")
    public ResponseEntity<Void> deleteDataPermissionRule(@PathVariable Long id) {
        log.debug("REST request to delete DataPermissionRule : {}", id);
        dataPermissionRuleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /data-permission-rules} : delete all the "ids" DataPermissionRules.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/data-permission-rules")
    @ApiOperation(value = "??????????????????????????????", notes = "??????????????????????????????????????????")
    public ResponseEntity<Void> deleteDataPermissionRulesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete DataPermissionRules : {}", ids);
        if (ids != null) {
            ids.forEach(dataPermissionRuleService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /data-permission-rules/specified-fields} : Updates an existing dataPermissionRule by specified fields.
     *
     * @param dataPermissionRuleDTOAndSpecifiedFields the dataPermissionRuleDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataPermissionRuleDTO,
     * or with status {@code 400 (Bad Request)} if the dataPermissionRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataPermissionRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/data-permission-rules/specified-fields")
    @ApiOperation(value = "??????????????????????????????????????????", notes = "????????????????????????????????????????????????????????????????????????????????????????????????null")
    public ResponseEntity<DataPermissionRuleDTO> updateDataPermissionRuleDTOBySpecifiedFields(
        @RequestBody DataPermissionRuleDTOAndSpecifiedFields dataPermissionRuleDTOAndSpecifiedFields
    ) {
        log.debug("REST request to update DataPermissionRuleDTO : {}", dataPermissionRuleDTOAndSpecifiedFields);
        DataPermissionRuleDTO dataPermissionRuleDTO = dataPermissionRuleDTOAndSpecifiedFields.getDataPermissionRule();
        Set<String> specifiedFields = dataPermissionRuleDTOAndSpecifiedFields.getSpecifiedFields();
        if (dataPermissionRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DataPermissionRuleDTO result = dataPermissionRuleService.updateBySpecifiedFields(dataPermissionRuleDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dataPermissionRuleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /data-permission-rules/specified-field} : Updates an existing dataPermissionRuleDTO by specified field.
     *
     * @param dataPermissionRuleDTOAndSpecifiedFields the dataPermissionRuleDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataPermissionRuleDTO,
     * or with status {@code 400 (Bad Request)} if the dataPermissionRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataPermissionRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/data-permission-rules/specified-field")
    @ApiOperation(value = "????????????????????????????????????", notes = "??????????????????????????????????????????????????????????????????????????????????????????null")
    public ResponseEntity<DataPermissionRuleDTO> updateDataPermissionRuleBySpecifiedField(
        @RequestBody DataPermissionRuleDTOAndSpecifiedFields dataPermissionRuleDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update dataPermissionRuleDTO : {}", dataPermissionRuleDTOAndSpecifiedFields);
        DataPermissionRuleDTO dataPermissionRuleDTO = dataPermissionRuleDTOAndSpecifiedFields.getDataPermissionRule();
        String fieldName = dataPermissionRuleDTOAndSpecifiedFields.getSpecifiedField();
        if (dataPermissionRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DataPermissionRuleDTO result = dataPermissionRuleService.updateBySpecifiedField(dataPermissionRuleDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class DataPermissionRuleDTOAndSpecifiedFields {

        private DataPermissionRuleDTO dataPermissionRule;
        private Set<String> specifiedFields;
        private String specifiedField;

        private DataPermissionRuleDTO getDataPermissionRule() {
            return dataPermissionRule;
        }

        private void setDataPermissionRule(DataPermissionRuleDTO dataPermissionRule) {
            this.dataPermissionRule = dataPermissionRule;
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
