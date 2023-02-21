package com.mycompany.myapp.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.domain.ApiPermission;
import com.mycompany.myapp.repository.ApiPermissionRepository;
import com.mycompany.myapp.service.ApiPermissionQueryService;
import com.mycompany.myapp.service.ApiPermissionService;
import com.mycompany.myapp.service.criteria.ApiPermissionCriteria;
import com.mycompany.myapp.service.dto.ApiPermissionDTO;
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

 * 管理实体{@link com.mycompany.myapp.domain.ApiPermission}的REST Controller。
 */
@RestController
@RequestMapping("/api")
@Api(value = "api-permissions", tags = "API权限API接口")
public class ApiPermissionResource {

    private final Logger log = LoggerFactory.getLogger(ApiPermissionResource.class);

    private static final String ENTITY_NAME = "systemApiPermission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApiPermissionService apiPermissionService;

    private final ApiPermissionRepository apiPermissionRepository;

    private final ApiPermissionQueryService apiPermissionQueryService;

    public ApiPermissionResource(
        ApiPermissionService apiPermissionService,
        ApiPermissionRepository apiPermissionRepository,
        ApiPermissionQueryService apiPermissionQueryService
    ) {
        this.apiPermissionService = apiPermissionService;
        this.apiPermissionRepository = apiPermissionRepository;
        this.apiPermissionQueryService = apiPermissionQueryService;
    }

    /**
     * {@code POST  /api-permissions} : Create a new apiPermission.
     *
     * @param apiPermissionDTO the apiPermissionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apiPermissionDTO, or with status {@code 400 (Bad Request)} if the apiPermission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/api-permissions")
    @ApiOperation(value = "新建API权限", notes = "创建并返回一个新的API权限")
    public ResponseEntity<ApiPermissionDTO> createApiPermission(@RequestBody ApiPermissionDTO apiPermissionDTO) throws URISyntaxException {
        log.debug("REST request to save ApiPermission : {}", apiPermissionDTO);
        if (apiPermissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new apiPermission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApiPermissionDTO result = apiPermissionService.save(apiPermissionDTO);
        return ResponseEntity
            .created(new URI("/api/api-permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /api-permissions/:id} : Updates an existing apiPermission.
     *
     * @param id the id of the apiPermissionDTO to save.
     * @param apiPermissionDTO the apiPermissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apiPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the apiPermissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apiPermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/api-permissions/{id}")
    @ApiOperation(value = "更新API权限", notes = "根据主键更新并返回一个更新后的API权限")
    public ResponseEntity<ApiPermissionDTO> updateApiPermission(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApiPermissionDTO apiPermissionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ApiPermission : {}, {}", id, apiPermissionDTO);
        if (apiPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apiPermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apiPermissionService.exists(ApiPermission::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApiPermissionDTO result = apiPermissionService.save(apiPermissionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apiPermissionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /api-permissions/generate : regenerate all api permissions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of apiPermissions in body
     */
    @GetMapping("/api-permissions/generate")
    public ResponseEntity<Void> generate() {
        log.debug("REST request to get a page of ApiPermissions");
        apiPermissionService.regenerateApiPermissions();
        return ResponseEntity.ok().build();
    }

    /**
     * {@code PATCH  /api-permissions/:id} : Partial updates given fields of an existing apiPermission, field will ignore if it is null.
     *
     * @param id the id of the apiPermissionDTO to save.
     * @param apiPermissionDTO the apiPermissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apiPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the apiPermissionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the apiPermissionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the apiPermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(value = "部分更新API权限", notes = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的API权限")
    @PatchMapping(value = "/api-permissions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ApiPermissionDTO> partialUpdateApiPermission(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApiPermissionDTO apiPermissionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApiPermission partially : {}, {}", id, apiPermissionDTO);
        if (apiPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apiPermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (apiPermissionRepository.findById(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApiPermissionDTO> result = apiPermissionService.partialUpdate(apiPermissionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apiPermissionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /api-permissions} : get all the apiPermissions.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apiPermissions in body.
     */
    @GetMapping("/api-permissions")
    @ApiOperation(value = "获取API权限分页列表", notes = "获取API权限的分页列表数据")
    public ResponseEntity<List<ApiPermissionDTO>> getAllApiPermissions(
        ApiPermissionCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get ApiPermissions by criteria: {}", criteria);
        IPage<ApiPermissionDTO> page;
        page = apiPermissionQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /api-permissions/count} : count all the apiPermissions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/api-permissions/count")
    public ResponseEntity<Long> countApiPermissions(ApiPermissionCriteria criteria) {
        log.debug("REST request to count ApiPermissions by criteria: {}", criteria);
        return ResponseEntity.ok().body(apiPermissionQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /api-permissions/tree : get all the apiPermissions for parent is null.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of apiPermissions in body
     */
    @GetMapping("/api-permissions/tree")
    @ApiOperation(value = "获取API权限树形列表", notes = "获取API权限的树形列表数据")
    public ResponseEntity<List<ApiPermissionDTO>> getAllApiPermissionsofTree(Pageable pageable) {
        log.debug("REST request to get a page of ApiPermissions");
        IPage<ApiPermissionDTO> page = apiPermissionService.findAllTop(PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * GET  /api-permissions/{parentId}/tree : get all the apiPermissions for parent is parentId.
     *
     * @param parentId the parent of Id
     * @return the ResponseEntity with status 200 (OK) and the list of apiPermissions in body
     */
    @GetMapping("/api-permissions/{parentId}/tree")
    @ApiOperation(value = "获取API权限指定节点下的树形列表", notes = "获取API权限指定节点下的树形列表数据")
    public ResponseEntity<List<ApiPermissionDTO>> getAllApiPermissionsofParent(@PathVariable Long parentId) {
        log.debug("REST request to get all ApiPermissions of parentId");
        List<ApiPermissionDTO> list = apiPermissionService.findChildrenByParentId(parentId);
        return ResponseEntity.ok().body(list);
    }

    /**
     * {@code GET  /api-permissions/:id} : get the "id" apiPermission.
     *
     * @param id the id of the apiPermissionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apiPermissionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/api-permissions/{id}")
    @ApiOperation(value = "获取指定主键的API权限", notes = "获取指定主键的API权限信息")
    public ResponseEntity<ApiPermissionDTO> getApiPermission(@PathVariable Long id) {
        log.debug("REST request to get ApiPermission : {}", id);
        Optional<ApiPermissionDTO> apiPermissionDTO = apiPermissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(apiPermissionDTO);
    }

    /**
     * GET  /api-permissions/export : export the apiPermissions.
     *
     */
    @GetMapping("/api-permissions/export")
    @ApiOperation(value = "API权限EXCEL导出", notes = "导出全部API权限为EXCEL文件")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<ApiPermissionDTO> data = apiPermissionService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("API权限一览表", "API权限"), ApiPermissionDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "API权限_" + sdf.format(new Date()) + ".xlsx";
        ExportUtil.excel(workbook, filename, response);
    }

    /**
     * POST  /api-permissions/import : import the apiPermissions from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the apiPermissionDTO, or with status 404 (Not Found)
     */
    @PostMapping("/api-permissions/import")
    @ApiOperation(value = "API权限EXCEL导入", notes = "根据API权限EXCEL文件导入全部数据")
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<ApiPermissionDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), ApiPermissionDTO.class, params);
        list.forEach(apiPermissionService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /api-permissions/:id} : delete the "id" apiPermission.
     *
     * @param id the id of the apiPermissionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/api-permissions/{id}")
    @ApiOperation(value = "删除一个API权限", notes = "根据主键删除单个API权限")
    public ResponseEntity<Void> deleteApiPermission(@PathVariable Long id) {
        log.debug("REST request to delete ApiPermission : {}", id);
        apiPermissionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /api-permissions} : delete all the "ids" ApiPermissions.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/api-permissions")
    @ApiOperation(value = "删除多个API权限", notes = "根据主键删除多个API权限")
    public ResponseEntity<Void> deleteApiPermissionsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete ApiPermissions : {}", ids);
        if (ids != null) {
            ids.forEach(apiPermissionService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /api-permissions/specified-fields} : Updates an existing apiPermission by specified fields.
     *
     * @param apiPermissionDTOAndSpecifiedFields the apiPermissionDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apiPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the apiPermissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apiPermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/api-permissions/specified-fields")
    @ApiOperation(value = "根据字段部分更新API权限", notes = "根据指定字段部分更新API权限，给定的属性值可以为任何值，包括null")
    public ResponseEntity<ApiPermissionDTO> updateApiPermissionDTOBySpecifiedFields(
        @RequestBody ApiPermissionDTOAndSpecifiedFields apiPermissionDTOAndSpecifiedFields
    ) {
        log.debug("REST request to update ApiPermissionDTO : {}", apiPermissionDTOAndSpecifiedFields);
        ApiPermissionDTO apiPermissionDTO = apiPermissionDTOAndSpecifiedFields.getApiPermission();
        Set<String> specifiedFields = apiPermissionDTOAndSpecifiedFields.getSpecifiedFields();
        if (apiPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ApiPermissionDTO result = apiPermissionService.updateBySpecifiedFields(apiPermissionDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apiPermissionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /api-permissions/specified-field} : Updates an existing apiPermissionDTO by specified field.
     *
     * @param apiPermissionDTOAndSpecifiedFields the apiPermissionDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apiPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the apiPermissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apiPermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/api-permissions/specified-field")
    @ApiOperation(value = "更新API权限单个属性", notes = "根据指定字段更新API权限，给定的属性值可以为任何值，包括null")
    public ResponseEntity<ApiPermissionDTO> updateApiPermissionBySpecifiedField(
        @RequestBody ApiPermissionDTOAndSpecifiedFields apiPermissionDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update apiPermissionDTO : {}", apiPermissionDTOAndSpecifiedFields);
        ApiPermissionDTO apiPermissionDTO = apiPermissionDTOAndSpecifiedFields.getApiPermission();
        String fieldName = apiPermissionDTOAndSpecifiedFields.getSpecifiedField();
        if (apiPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ApiPermissionDTO result = apiPermissionService.updateBySpecifiedField(apiPermissionDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class ApiPermissionDTOAndSpecifiedFields {

        private ApiPermissionDTO apiPermission;
        private Set<String> specifiedFields;
        private String specifiedField;

        private ApiPermissionDTO getApiPermission() {
            return apiPermission;
        }

        private void setApiPermission(ApiPermissionDTO apiPermission) {
            this.apiPermission = apiPermission;
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
