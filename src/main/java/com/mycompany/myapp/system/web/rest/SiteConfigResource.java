package com.mycompany.myapp.system.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.system.domain.SiteConfig;
import com.mycompany.myapp.system.repository.SiteConfigRepository;
import com.mycompany.myapp.system.service.SiteConfigQueryService;
import com.mycompany.myapp.system.service.SiteConfigService;
import com.mycompany.myapp.system.service.criteria.SiteConfigCriteria;
import com.mycompany.myapp.system.service.dto.SiteConfigDTO;
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

 * 管理实体{@link com.mycompany.myapp.system.domain.SiteConfig}的REST Controller。
 */
@RestController
@RequestMapping("/api")
@Api(value = "site-configs", tags = "网站配置API接口")
public class SiteConfigResource {

    private final Logger log = LoggerFactory.getLogger(SiteConfigResource.class);

    private static final String ENTITY_NAME = "siteConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SiteConfigService siteConfigService;

    private final SiteConfigRepository siteConfigRepository;

    private final SiteConfigQueryService siteConfigQueryService;

    public SiteConfigResource(
        SiteConfigService siteConfigService,
        SiteConfigRepository siteConfigRepository,
        SiteConfigQueryService siteConfigQueryService
    ) {
        this.siteConfigService = siteConfigService;
        this.siteConfigRepository = siteConfigRepository;
        this.siteConfigQueryService = siteConfigQueryService;
    }

    /**
     * {@code POST  /site-configs} : Create a new siteConfig.
     *
     * @param siteConfigDTO the siteConfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new siteConfigDTO, or with status {@code 400 (Bad Request)} if the siteConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/site-configs")
    @ApiOperation(value = "新建网站配置", notes = "创建并返回一个新的网站配置")
    public ResponseEntity<SiteConfigDTO> createSiteConfig(@RequestBody SiteConfigDTO siteConfigDTO) throws URISyntaxException {
        log.debug("REST request to save SiteConfig : {}", siteConfigDTO);
        if (siteConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new siteConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SiteConfigDTO result = siteConfigService.save(siteConfigDTO);
        return ResponseEntity
            .created(new URI("/api/site-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /site-configs/:id} : Updates an existing siteConfig.
     *
     * @param id the id of the siteConfigDTO to save.
     * @param siteConfigDTO the siteConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated siteConfigDTO,
     * or with status {@code 400 (Bad Request)} if the siteConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the siteConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/site-configs/{id}")
    @ApiOperation(value = "更新网站配置", notes = "根据主键更新并返回一个更新后的网站配置")
    public ResponseEntity<SiteConfigDTO> updateSiteConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SiteConfigDTO siteConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SiteConfig : {}, {}", id, siteConfigDTO);
        if (siteConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, siteConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!siteConfigService.exists(SiteConfig::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SiteConfigDTO result = siteConfigService.save(siteConfigDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, siteConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /site-configs/:id} : Partial updates given fields of an existing siteConfig, field will ignore if it is null.
     *
     * @param id the id of the siteConfigDTO to save.
     * @param siteConfigDTO the siteConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated siteConfigDTO,
     * or with status {@code 400 (Bad Request)} if the siteConfigDTO is not valid,
     * or with status {@code 404 (Not Found)} if the siteConfigDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the siteConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(value = "部分更新网站配置", notes = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的网站配置")
    @PatchMapping(value = "/site-configs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SiteConfigDTO> partialUpdateSiteConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SiteConfigDTO siteConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SiteConfig partially : {}, {}", id, siteConfigDTO);
        if (siteConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, siteConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (siteConfigRepository.findById(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SiteConfigDTO> result = siteConfigService.partialUpdate(siteConfigDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, siteConfigDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /site-configs} : get all the siteConfigs.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of siteConfigs in body.
     */
    @GetMapping("/site-configs")
    @ApiOperation(value = "获取网站配置分页列表", notes = "获取网站配置的分页列表数据")
    public ResponseEntity<List<SiteConfigDTO>> getAllSiteConfigs(
        SiteConfigCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get SiteConfigs by criteria: {}", criteria);
        IPage<SiteConfigDTO> page;
        page = siteConfigQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /site-configs/count} : count all the siteConfigs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/site-configs/count")
    public ResponseEntity<Long> countSiteConfigs(SiteConfigCriteria criteria) {
        log.debug("REST request to count SiteConfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(siteConfigQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /site-configs/:id} : get the "id" siteConfig.
     *
     * @param id the id of the siteConfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the siteConfigDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/site-configs/{id}")
    @ApiOperation(value = "获取指定主键的网站配置", notes = "获取指定主键的网站配置信息")
    public ResponseEntity<SiteConfigDTO> getSiteConfig(@PathVariable Long id) {
        log.debug("REST request to get SiteConfig : {}", id);
        Optional<SiteConfigDTO> siteConfigDTO = siteConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(siteConfigDTO);
    }

    /**
     * GET  /site-configs/export : export the siteConfigs.
     *
     */
    @GetMapping("/site-configs/export")
    @ApiOperation(value = "网站配置EXCEL导出", notes = "导出全部网站配置为EXCEL文件")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<SiteConfigDTO> data = siteConfigService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("网站配置一览表", "网站配置"), SiteConfigDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "网站配置_" + sdf.format(new Date()) + ".xlsx";
        ExportUtil.excel(workbook, filename, response);
    }

    /**
     * POST  /site-configs/import : import the siteConfigs from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the siteConfigDTO, or with status 404 (Not Found)
     */
    @PostMapping("/site-configs/import")
    @ApiOperation(value = "网站配置EXCEL导入", notes = "根据网站配置EXCEL文件导入全部数据")
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<SiteConfigDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), SiteConfigDTO.class, params);
        list.forEach(siteConfigService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /site-configs/:id} : delete the "id" siteConfig.
     *
     * @param id the id of the siteConfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/site-configs/{id}")
    @ApiOperation(value = "删除一个网站配置", notes = "根据主键删除单个网站配置")
    public ResponseEntity<Void> deleteSiteConfig(@PathVariable Long id) {
        log.debug("REST request to delete SiteConfig : {}", id);
        siteConfigService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /site-configs} : delete all the "ids" SiteConfigs.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/site-configs")
    @ApiOperation(value = "删除多个网站配置", notes = "根据主键删除多个网站配置")
    public ResponseEntity<Void> deleteSiteConfigsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete SiteConfigs : {}", ids);
        if (ids != null) {
            ids.forEach(siteConfigService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /site-configs/specified-fields} : Updates an existing siteConfig by specified fields.
     *
     * @param siteConfigDTOAndSpecifiedFields the siteConfigDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated siteConfigDTO,
     * or with status {@code 400 (Bad Request)} if the siteConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the siteConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/site-configs/specified-fields")
    @ApiOperation(value = "根据字段部分更新网站配置", notes = "根据指定字段部分更新网站配置，给定的属性值可以为任何值，包括null")
    public ResponseEntity<SiteConfigDTO> updateSiteConfigDTOBySpecifiedFields(
        @RequestBody SiteConfigDTOAndSpecifiedFields siteConfigDTOAndSpecifiedFields
    ) {
        log.debug("REST request to update SiteConfigDTO : {}", siteConfigDTOAndSpecifiedFields);
        SiteConfigDTO siteConfigDTO = siteConfigDTOAndSpecifiedFields.getSiteConfig();
        Set<String> specifiedFields = siteConfigDTOAndSpecifiedFields.getSpecifiedFields();
        if (siteConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SiteConfigDTO result = siteConfigService.updateBySpecifiedFields(siteConfigDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, siteConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /site-configs/specified-field} : Updates an existing siteConfigDTO by specified field.
     *
     * @param siteConfigDTOAndSpecifiedFields the siteConfigDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated siteConfigDTO,
     * or with status {@code 400 (Bad Request)} if the siteConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the siteConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/site-configs/specified-field")
    @ApiOperation(value = "更新网站配置单个属性", notes = "根据指定字段更新网站配置，给定的属性值可以为任何值，包括null")
    public ResponseEntity<SiteConfigDTO> updateSiteConfigBySpecifiedField(
        @RequestBody SiteConfigDTOAndSpecifiedFields siteConfigDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update siteConfigDTO : {}", siteConfigDTOAndSpecifiedFields);
        SiteConfigDTO siteConfigDTO = siteConfigDTOAndSpecifiedFields.getSiteConfig();
        String fieldName = siteConfigDTOAndSpecifiedFields.getSpecifiedField();
        if (siteConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SiteConfigDTO result = siteConfigService.updateBySpecifiedField(siteConfigDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class SiteConfigDTOAndSpecifiedFields {

        private SiteConfigDTO siteConfig;
        private Set<String> specifiedFields;
        private String specifiedField;

        private SiteConfigDTO getSiteConfig() {
            return siteConfig;
        }

        private void setSiteConfig(SiteConfigDTO siteConfig) {
            this.siteConfig = siteConfig;
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
