package com.mycompany.myapp.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.domain.BusinessType;
import com.mycompany.myapp.repository.BusinessTypeRepository;
import com.mycompany.myapp.service.BusinessTypeQueryService;
import com.mycompany.myapp.service.BusinessTypeService;
import com.mycompany.myapp.service.criteria.BusinessTypeCriteria;
import com.mycompany.myapp.service.dto.BusinessTypeDTO;
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

 * 管理实体{@link com.mycompany.myapp.domain.BusinessType}的REST Controller。
 */
@RestController
@RequestMapping("/api")
@Api(value = "business-types", tags = "业务类型API接口")
public class BusinessTypeResource {

    private final Logger log = LoggerFactory.getLogger(BusinessTypeResource.class);

    private static final String ENTITY_NAME = "settingsBusinessType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusinessTypeService businessTypeService;

    private final BusinessTypeRepository businessTypeRepository;

    private final BusinessTypeQueryService businessTypeQueryService;

    public BusinessTypeResource(
        BusinessTypeService businessTypeService,
        BusinessTypeRepository businessTypeRepository,
        BusinessTypeQueryService businessTypeQueryService
    ) {
        this.businessTypeService = businessTypeService;
        this.businessTypeRepository = businessTypeRepository;
        this.businessTypeQueryService = businessTypeQueryService;
    }

    /**
     * {@code POST  /business-types} : Create a new businessType.
     *
     * @param businessTypeDTO the businessTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new businessTypeDTO, or with status {@code 400 (Bad Request)} if the businessType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/business-types")
    @ApiOperation(value = "新建业务类型", notes = "创建并返回一个新的业务类型")
    public ResponseEntity<BusinessTypeDTO> createBusinessType(@RequestBody BusinessTypeDTO businessTypeDTO) throws URISyntaxException {
        log.debug("REST request to save BusinessType : {}", businessTypeDTO);
        if (businessTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new businessType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusinessTypeDTO result = businessTypeService.save(businessTypeDTO);
        return ResponseEntity
            .created(new URI("/api/business-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /business-types/:id} : Updates an existing businessType.
     *
     * @param id the id of the businessTypeDTO to save.
     * @param businessTypeDTO the businessTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessTypeDTO,
     * or with status {@code 400 (Bad Request)} if the businessTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/business-types/{id}")
    @ApiOperation(value = "更新业务类型", notes = "根据主键更新并返回一个更新后的业务类型")
    public ResponseEntity<BusinessTypeDTO> updateBusinessType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BusinessTypeDTO businessTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BusinessType : {}, {}", id, businessTypeDTO);
        if (businessTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessTypeService.exists(BusinessType::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BusinessTypeDTO result = businessTypeService.save(businessTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /business-types/:id} : Partial updates given fields of an existing businessType, field will ignore if it is null.
     *
     * @param id the id of the businessTypeDTO to save.
     * @param businessTypeDTO the businessTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessTypeDTO,
     * or with status {@code 400 (Bad Request)} if the businessTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the businessTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the businessTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(value = "部分更新业务类型", notes = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的业务类型")
    @PatchMapping(value = "/business-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BusinessTypeDTO> partialUpdateBusinessType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BusinessTypeDTO businessTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BusinessType partially : {}, {}", id, businessTypeDTO);
        if (businessTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (businessTypeRepository.findById(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusinessTypeDTO> result = businessTypeService.partialUpdate(businessTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /business-types} : get all the businessTypes.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of businessTypes in body.
     */
    @GetMapping("/business-types")
    @ApiOperation(value = "获取业务类型分页列表", notes = "获取业务类型的分页列表数据")
    public ResponseEntity<List<BusinessTypeDTO>> getAllBusinessTypes(
        BusinessTypeCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get BusinessTypes by criteria: {}", criteria);
        IPage<BusinessTypeDTO> page;
        page = businessTypeQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /business-types/count} : count all the businessTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/business-types/count")
    public ResponseEntity<Long> countBusinessTypes(BusinessTypeCriteria criteria) {
        log.debug("REST request to count BusinessTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(businessTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /business-types/:id} : get the "id" businessType.
     *
     * @param id the id of the businessTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the businessTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/business-types/{id}")
    @ApiOperation(value = "获取指定主键的业务类型", notes = "获取指定主键的业务类型信息")
    public ResponseEntity<BusinessTypeDTO> getBusinessType(@PathVariable Long id) {
        log.debug("REST request to get BusinessType : {}", id);
        Optional<BusinessTypeDTO> businessTypeDTO = businessTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(businessTypeDTO);
    }

    /**
     * GET  /business-types/export : export the businessTypes.
     *
     */
    @GetMapping("/business-types/export")
    @ApiOperation(value = "业务类型EXCEL导出", notes = "导出全部业务类型为EXCEL文件")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<BusinessTypeDTO> data = businessTypeService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("业务类型一览表", "业务类型"), BusinessTypeDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "业务类型_" + sdf.format(new Date()) + ".xlsx";
        ExportUtil.excel(workbook, filename, response);
    }

    /**
     * POST  /business-types/import : import the businessTypes from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the businessTypeDTO, or with status 404 (Not Found)
     */
    @PostMapping("/business-types/import")
    @ApiOperation(value = "业务类型EXCEL导入", notes = "根据业务类型EXCEL文件导入全部数据")
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<BusinessTypeDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), BusinessTypeDTO.class, params);
        list.forEach(businessTypeService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /business-types/:id} : delete the "id" businessType.
     *
     * @param id the id of the businessTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/business-types/{id}")
    @ApiOperation(value = "删除一个业务类型", notes = "根据主键删除单个业务类型")
    public ResponseEntity<Void> deleteBusinessType(@PathVariable Long id) {
        log.debug("REST request to delete BusinessType : {}", id);
        businessTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /business-types} : delete all the "ids" BusinessTypes.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/business-types")
    @ApiOperation(value = "删除多个业务类型", notes = "根据主键删除多个业务类型")
    public ResponseEntity<Void> deleteBusinessTypesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete BusinessTypes : {}", ids);
        if (ids != null) {
            ids.forEach(businessTypeService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /business-types/specified-fields} : Updates an existing businessType by specified fields.
     *
     * @param businessTypeDTOAndSpecifiedFields the businessTypeDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessTypeDTO,
     * or with status {@code 400 (Bad Request)} if the businessTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/business-types/specified-fields")
    @ApiOperation(value = "根据字段部分更新业务类型", notes = "根据指定字段部分更新业务类型，给定的属性值可以为任何值，包括null")
    public ResponseEntity<BusinessTypeDTO> updateBusinessTypeDTOBySpecifiedFields(
        @RequestBody BusinessTypeDTOAndSpecifiedFields businessTypeDTOAndSpecifiedFields
    ) {
        log.debug("REST request to update BusinessTypeDTO : {}", businessTypeDTOAndSpecifiedFields);
        BusinessTypeDTO businessTypeDTO = businessTypeDTOAndSpecifiedFields.getBusinessType();
        Set<String> specifiedFields = businessTypeDTOAndSpecifiedFields.getSpecifiedFields();
        if (businessTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BusinessTypeDTO result = businessTypeService.updateBySpecifiedFields(businessTypeDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /business-types/specified-field} : Updates an existing businessTypeDTO by specified field.
     *
     * @param businessTypeDTOAndSpecifiedFields the businessTypeDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessTypeDTO,
     * or with status {@code 400 (Bad Request)} if the businessTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/business-types/specified-field")
    @ApiOperation(value = "更新业务类型单个属性", notes = "根据指定字段更新业务类型，给定的属性值可以为任何值，包括null")
    public ResponseEntity<BusinessTypeDTO> updateBusinessTypeBySpecifiedField(
        @RequestBody BusinessTypeDTOAndSpecifiedFields businessTypeDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update businessTypeDTO : {}", businessTypeDTOAndSpecifiedFields);
        BusinessTypeDTO businessTypeDTO = businessTypeDTOAndSpecifiedFields.getBusinessType();
        String fieldName = businessTypeDTOAndSpecifiedFields.getSpecifiedField();
        if (businessTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BusinessTypeDTO result = businessTypeService.updateBySpecifiedField(businessTypeDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class BusinessTypeDTOAndSpecifiedFields {

        private BusinessTypeDTO businessType;
        private Set<String> specifiedFields;
        private String specifiedField;

        private BusinessTypeDTO getBusinessType() {
            return businessType;
        }

        private void setBusinessType(BusinessTypeDTO businessType) {
            this.businessType = businessType;
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
