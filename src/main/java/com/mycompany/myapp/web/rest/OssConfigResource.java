package com.mycompany.myapp.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.domain.OssConfig;
import com.mycompany.myapp.repository.OssConfigRepository;
import com.mycompany.myapp.service.OssConfigQueryService;
import com.mycompany.myapp.service.OssConfigService;
import com.mycompany.myapp.service.criteria.OssConfigCriteria;
import com.mycompany.myapp.service.dto.OssConfigDTO;
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

 * 管理实体{@link com.mycompany.myapp.domain.OssConfig}的REST Controller。
 */
@RestController
@RequestMapping("/api")
@Api(value = "oss-configs", tags = "对象存储配置API接口")
public class OssConfigResource {

    private final Logger log = LoggerFactory.getLogger(OssConfigResource.class);

    private static final String ENTITY_NAME = "filesOssConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OssConfigService ossConfigService;

    private final OssConfigRepository ossConfigRepository;

    private final OssConfigQueryService ossConfigQueryService;

    public OssConfigResource(
        OssConfigService ossConfigService,
        OssConfigRepository ossConfigRepository,
        OssConfigQueryService ossConfigQueryService
    ) {
        this.ossConfigService = ossConfigService;
        this.ossConfigRepository = ossConfigRepository;
        this.ossConfigQueryService = ossConfigQueryService;
    }

    /**
     * {@code POST  /oss-configs} : Create a new ossConfig.
     *
     * @param ossConfigDTO the ossConfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ossConfigDTO, or with status {@code 400 (Bad Request)} if the ossConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/oss-configs")
    @ApiOperation(value = "新建对象存储配置", notes = "创建并返回一个新的对象存储配置")
    public ResponseEntity<OssConfigDTO> createOssConfig(@RequestBody OssConfigDTO ossConfigDTO) throws URISyntaxException {
        log.debug("REST request to save OssConfig : {}", ossConfigDTO);
        if (ossConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new ossConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OssConfigDTO result = ossConfigService.save(ossConfigDTO);
        return ResponseEntity
            .created(new URI("/api/oss-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /oss-configs/:id} : Updates an existing ossConfig.
     *
     * @param id the id of the ossConfigDTO to save.
     * @param ossConfigDTO the ossConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ossConfigDTO,
     * or with status {@code 400 (Bad Request)} if the ossConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ossConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/oss-configs/{id}")
    @ApiOperation(value = "更新对象存储配置", notes = "根据主键更新并返回一个更新后的对象存储配置")
    public ResponseEntity<OssConfigDTO> updateOssConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OssConfigDTO ossConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OssConfig : {}, {}", id, ossConfigDTO);
        if (ossConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ossConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ossConfigService.exists(OssConfig::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OssConfigDTO result = ossConfigService.save(ossConfigDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ossConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /oss-configs/:id} : Partial updates given fields of an existing ossConfig, field will ignore if it is null.
     *
     * @param id the id of the ossConfigDTO to save.
     * @param ossConfigDTO the ossConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ossConfigDTO,
     * or with status {@code 400 (Bad Request)} if the ossConfigDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ossConfigDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ossConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(
        value = "部分更新对象存储配置",
        notes = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的对象存储配置"
    )
    @PatchMapping(value = "/oss-configs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OssConfigDTO> partialUpdateOssConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OssConfigDTO ossConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OssConfig partially : {}, {}", id, ossConfigDTO);
        if (ossConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ossConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (ossConfigRepository.findById(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OssConfigDTO> result = ossConfigService.partialUpdate(ossConfigDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ossConfigDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /oss-configs} : get all the ossConfigs.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ossConfigs in body.
     */
    @GetMapping("/oss-configs")
    @ApiOperation(value = "获取对象存储配置分页列表", notes = "获取对象存储配置的分页列表数据")
    public ResponseEntity<List<OssConfigDTO>> getAllOssConfigs(
        OssConfigCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get OssConfigs by criteria: {}", criteria);
        IPage<OssConfigDTO> page;
        page = ossConfigQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /oss-configs/count} : count all the ossConfigs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/oss-configs/count")
    public ResponseEntity<Long> countOssConfigs(OssConfigCriteria criteria) {
        log.debug("REST request to count OssConfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(ossConfigQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /oss-configs/:id} : get the "id" ossConfig.
     *
     * @param id the id of the ossConfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ossConfigDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/oss-configs/{id}")
    @ApiOperation(value = "获取指定主键的对象存储配置", notes = "获取指定主键的对象存储配置信息")
    public ResponseEntity<OssConfigDTO> getOssConfig(@PathVariable Long id) {
        log.debug("REST request to get OssConfig : {}", id);
        Optional<OssConfigDTO> ossConfigDTO = ossConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ossConfigDTO);
    }

    /**
     * GET  /oss-configs/export : export the ossConfigs.
     *
     */
    @GetMapping("/oss-configs/export")
    @ApiOperation(value = "对象存储配置EXCEL导出", notes = "导出全部对象存储配置为EXCEL文件")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<OssConfigDTO> data = ossConfigService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("对象存储配置一览表", "对象存储配置"), OssConfigDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "对象存储配置_" + sdf.format(new Date()) + ".xlsx";
        ExportUtil.excel(workbook, filename, response);
    }

    /**
     * POST  /oss-configs/import : import the ossConfigs from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the ossConfigDTO, or with status 404 (Not Found)
     */
    @PostMapping("/oss-configs/import")
    @ApiOperation(value = "对象存储配置EXCEL导入", notes = "根据对象存储配置EXCEL文件导入全部数据")
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<OssConfigDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), OssConfigDTO.class, params);
        list.forEach(ossConfigService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /oss-configs/:id} : delete the "id" ossConfig.
     *
     * @param id the id of the ossConfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/oss-configs/{id}")
    @ApiOperation(value = "删除一个对象存储配置", notes = "根据主键删除单个对象存储配置")
    public ResponseEntity<Void> deleteOssConfig(@PathVariable Long id) {
        log.debug("REST request to delete OssConfig : {}", id);
        ossConfigService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /oss-configs} : delete all the "ids" OssConfigs.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/oss-configs")
    @ApiOperation(value = "删除多个对象存储配置", notes = "根据主键删除多个对象存储配置")
    public ResponseEntity<Void> deleteOssConfigsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete OssConfigs : {}", ids);
        if (ids != null) {
            ids.forEach(ossConfigService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /oss-configs/specified-fields} : Updates an existing ossConfig by specified fields.
     *
     * @param ossConfigDTOAndSpecifiedFields the ossConfigDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ossConfigDTO,
     * or with status {@code 400 (Bad Request)} if the ossConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ossConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/oss-configs/specified-fields")
    @ApiOperation(value = "根据字段部分更新对象存储配置", notes = "根据指定字段部分更新对象存储配置，给定的属性值可以为任何值，包括null")
    public ResponseEntity<OssConfigDTO> updateOssConfigDTOBySpecifiedFields(
        @RequestBody OssConfigDTOAndSpecifiedFields ossConfigDTOAndSpecifiedFields
    ) {
        log.debug("REST request to update OssConfigDTO : {}", ossConfigDTOAndSpecifiedFields);
        OssConfigDTO ossConfigDTO = ossConfigDTOAndSpecifiedFields.getOssConfig();
        Set<String> specifiedFields = ossConfigDTOAndSpecifiedFields.getSpecifiedFields();
        if (ossConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OssConfigDTO result = ossConfigService.updateBySpecifiedFields(ossConfigDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ossConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /oss-configs/specified-field} : Updates an existing ossConfigDTO by specified field.
     *
     * @param ossConfigDTOAndSpecifiedFields the ossConfigDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ossConfigDTO,
     * or with status {@code 400 (Bad Request)} if the ossConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ossConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/oss-configs/specified-field")
    @ApiOperation(value = "更新对象存储配置单个属性", notes = "根据指定字段更新对象存储配置，给定的属性值可以为任何值，包括null")
    public ResponseEntity<OssConfigDTO> updateOssConfigBySpecifiedField(
        @RequestBody OssConfigDTOAndSpecifiedFields ossConfigDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update ossConfigDTO : {}", ossConfigDTOAndSpecifiedFields);
        OssConfigDTO ossConfigDTO = ossConfigDTOAndSpecifiedFields.getOssConfig();
        String fieldName = ossConfigDTOAndSpecifiedFields.getSpecifiedField();
        if (ossConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OssConfigDTO result = ossConfigService.updateBySpecifiedField(ossConfigDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class OssConfigDTOAndSpecifiedFields {

        private OssConfigDTO ossConfig;
        private Set<String> specifiedFields;
        private String specifiedField;

        private OssConfigDTO getOssConfig() {
            return ossConfig;
        }

        private void setOssConfig(OssConfigDTO ossConfig) {
            this.ossConfig = ossConfig;
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
