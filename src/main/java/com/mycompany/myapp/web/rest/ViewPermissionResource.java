package com.mycompany.myapp.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.domain.ViewPermission;
import com.mycompany.myapp.repository.ViewPermissionRepository;
import com.mycompany.myapp.service.ViewPermissionQueryService;
import com.mycompany.myapp.service.ViewPermissionService;
import com.mycompany.myapp.service.criteria.ViewPermissionCriteria;
import com.mycompany.myapp.service.dto.ViewPermissionDTO;
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

 * 管理实体{@link com.mycompany.myapp.domain.ViewPermission}的REST Controller。
 */
@RestController
@RequestMapping("/api")
@Api(value = "view-permissions", tags = "可视权限API接口")
public class ViewPermissionResource {

    private final Logger log = LoggerFactory.getLogger(ViewPermissionResource.class);

    private static final String ENTITY_NAME = "systemViewPermission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ViewPermissionService viewPermissionService;

    private final ViewPermissionRepository viewPermissionRepository;

    private final ViewPermissionQueryService viewPermissionQueryService;

    public ViewPermissionResource(
        ViewPermissionService viewPermissionService,
        ViewPermissionRepository viewPermissionRepository,
        ViewPermissionQueryService viewPermissionQueryService
    ) {
        this.viewPermissionService = viewPermissionService;
        this.viewPermissionRepository = viewPermissionRepository;
        this.viewPermissionQueryService = viewPermissionQueryService;
    }

    /**
     * {@code POST  /view-permissions} : Create a new viewPermission.
     *
     * @param viewPermissionDTO the viewPermissionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new viewPermissionDTO, or with status {@code 400 (Bad Request)} if the viewPermission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/view-permissions")
    @ApiOperation(value = "新建可视权限", notes = "创建并返回一个新的可视权限")
    public ResponseEntity<ViewPermissionDTO> createViewPermission(@RequestBody ViewPermissionDTO viewPermissionDTO)
        throws URISyntaxException {
        log.debug("REST request to save ViewPermission : {}", viewPermissionDTO);
        if (viewPermissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new viewPermission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ViewPermissionDTO result = viewPermissionService.save(viewPermissionDTO);
        return ResponseEntity
            .created(new URI("/api/view-permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /view-permissions/:id} : Updates an existing viewPermission.
     *
     * @param id the id of the viewPermissionDTO to save.
     * @param viewPermissionDTO the viewPermissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viewPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the viewPermissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the viewPermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/view-permissions/{id}")
    @ApiOperation(value = "更新可视权限", notes = "根据主键更新并返回一个更新后的可视权限")
    public ResponseEntity<ViewPermissionDTO> updateViewPermission(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ViewPermissionDTO viewPermissionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ViewPermission : {}, {}", id, viewPermissionDTO);
        if (viewPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, viewPermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!viewPermissionService.exists(ViewPermission::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ViewPermissionDTO result = viewPermissionService.save(viewPermissionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, viewPermissionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /view-permissions/current-user} : get all the viewPermissions of currentUser.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of viewPermissions in body.
     */
    @GetMapping("/view-permissions/current-user")
    public ResponseEntity<List<ViewPermissionDTO>> getAllViewPermissionsByLogin() {
        log.debug("REST request to get ViewPermissions by current-user");
        return ResponseEntity.ok().body(viewPermissionService.getAllByLogin());
    }

    /**
     * {@code PATCH  /view-permissions/:id} : Partial updates given fields of an existing viewPermission, field will ignore if it is null.
     *
     * @param id the id of the viewPermissionDTO to save.
     * @param viewPermissionDTO the viewPermissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viewPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the viewPermissionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the viewPermissionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the viewPermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(value = "部分更新可视权限", notes = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的可视权限")
    @PatchMapping(value = "/view-permissions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ViewPermissionDTO> partialUpdateViewPermission(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ViewPermissionDTO viewPermissionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ViewPermission partially : {}, {}", id, viewPermissionDTO);
        if (viewPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, viewPermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (viewPermissionRepository.findById(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ViewPermissionDTO> result = viewPermissionService.partialUpdate(viewPermissionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, viewPermissionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /view-permissions} : get all the viewPermissions.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of viewPermissions in body.
     */
    @GetMapping("/view-permissions")
    @ApiOperation(value = "获取可视权限分页列表", notes = "获取可视权限的分页列表数据")
    public ResponseEntity<List<ViewPermissionDTO>> getAllViewPermissions(
        ViewPermissionCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get ViewPermissions by criteria: {}", criteria);
        IPage<ViewPermissionDTO> page;
        page = viewPermissionQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /view-permissions/count} : count all the viewPermissions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/view-permissions/count")
    public ResponseEntity<Long> countViewPermissions(ViewPermissionCriteria criteria) {
        log.debug("REST request to count ViewPermissions by criteria: {}", criteria);
        return ResponseEntity.ok().body(viewPermissionQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /view-permissions/tree : get all the viewPermissions for parent is null.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of viewPermissions in body
     */
    @GetMapping("/view-permissions/tree")
    @ApiOperation(value = "获取可视权限树形列表", notes = "获取可视权限的树形列表数据")
    public ResponseEntity<List<ViewPermissionDTO>> getAllViewPermissionsofTree(Pageable pageable) {
        log.debug("REST request to get a page of ViewPermissions");
        IPage<ViewPermissionDTO> page = viewPermissionService.findAllTop(PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * GET  /view-permissions/{parentId}/tree : get all the viewPermissions for parent is parentId.
     *
     * @param parentId the parent of Id
     * @return the ResponseEntity with status 200 (OK) and the list of viewPermissions in body
     */
    @GetMapping("/view-permissions/{parentId}/tree")
    @ApiOperation(value = "获取可视权限指定节点下的树形列表", notes = "获取可视权限指定节点下的树形列表数据")
    public ResponseEntity<List<ViewPermissionDTO>> getAllViewPermissionsofParent(@PathVariable Long parentId) {
        log.debug("REST request to get all ViewPermissions of parentId");
        List<ViewPermissionDTO> list = viewPermissionService.findChildrenByParentId(parentId);
        return ResponseEntity.ok().body(list);
    }

    /**
     * {@code GET  /view-permissions/:id} : get the "id" viewPermission.
     *
     * @param id the id of the viewPermissionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the viewPermissionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/view-permissions/{id}")
    @ApiOperation(value = "获取指定主键的可视权限", notes = "获取指定主键的可视权限信息")
    public ResponseEntity<ViewPermissionDTO> getViewPermission(@PathVariable Long id) {
        log.debug("REST request to get ViewPermission : {}", id);
        Optional<ViewPermissionDTO> viewPermissionDTO = viewPermissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(viewPermissionDTO);
    }

    /**
     * GET  /view-permissions/export : export the viewPermissions.
     *
     */
    @GetMapping("/view-permissions/export")
    @ApiOperation(value = "可视权限EXCEL导出", notes = "导出全部可视权限为EXCEL文件")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<ViewPermissionDTO> data = viewPermissionService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("可视权限一览表", "可视权限"), ViewPermissionDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "可视权限_" + sdf.format(new Date()) + ".xlsx";
        ExportUtil.excel(workbook, filename, response);
    }

    /**
     * POST  /view-permissions/import : import the viewPermissions from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the viewPermissionDTO, or with status 404 (Not Found)
     */
    @PostMapping("/view-permissions/import")
    @ApiOperation(value = "可视权限EXCEL导入", notes = "根据可视权限EXCEL文件导入全部数据")
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<ViewPermissionDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), ViewPermissionDTO.class, params);
        list.forEach(viewPermissionService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /view-permissions/:id} : delete the "id" viewPermission.
     *
     * @param id the id of the viewPermissionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/view-permissions/{id}")
    @ApiOperation(value = "删除一个可视权限", notes = "根据主键删除单个可视权限")
    public ResponseEntity<Void> deleteViewPermission(@PathVariable Long id) {
        log.debug("REST request to delete ViewPermission : {}", id);
        viewPermissionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /view-permissions} : delete all the "ids" ViewPermissions.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/view-permissions")
    @ApiOperation(value = "删除多个可视权限", notes = "根据主键删除多个可视权限")
    public ResponseEntity<Void> deleteViewPermissionsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete ViewPermissions : {}", ids);
        if (ids != null) {
            ids.forEach(viewPermissionService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /view-permissions/specified-fields} : Updates an existing viewPermission by specified fields.
     *
     * @param viewPermissionDTOAndSpecifiedFields the viewPermissionDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viewPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the viewPermissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the viewPermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/view-permissions/specified-fields")
    @ApiOperation(value = "根据字段部分更新可视权限", notes = "根据指定字段部分更新可视权限，给定的属性值可以为任何值，包括null")
    public ResponseEntity<ViewPermissionDTO> updateViewPermissionDTOBySpecifiedFields(
        @RequestBody ViewPermissionDTOAndSpecifiedFields viewPermissionDTOAndSpecifiedFields
    ) {
        log.debug("REST request to update ViewPermissionDTO : {}", viewPermissionDTOAndSpecifiedFields);
        ViewPermissionDTO viewPermissionDTO = viewPermissionDTOAndSpecifiedFields.getViewPermission();
        Set<String> specifiedFields = viewPermissionDTOAndSpecifiedFields.getSpecifiedFields();
        if (viewPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ViewPermissionDTO result = viewPermissionService.updateBySpecifiedFields(viewPermissionDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, viewPermissionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /view-permissions/specified-field} : Updates an existing viewPermissionDTO by specified field.
     *
     * @param viewPermissionDTOAndSpecifiedFields the viewPermissionDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viewPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the viewPermissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the viewPermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/view-permissions/specified-field")
    @ApiOperation(value = "更新可视权限单个属性", notes = "根据指定字段更新可视权限，给定的属性值可以为任何值，包括null")
    public ResponseEntity<ViewPermissionDTO> updateViewPermissionBySpecifiedField(
        @RequestBody ViewPermissionDTOAndSpecifiedFields viewPermissionDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update viewPermissionDTO : {}", viewPermissionDTOAndSpecifiedFields);
        ViewPermissionDTO viewPermissionDTO = viewPermissionDTOAndSpecifiedFields.getViewPermission();
        String fieldName = viewPermissionDTOAndSpecifiedFields.getSpecifiedField();
        if (viewPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ViewPermissionDTO result = viewPermissionService.updateBySpecifiedField(viewPermissionDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class ViewPermissionDTOAndSpecifiedFields {

        private ViewPermissionDTO viewPermission;
        private Set<String> specifiedFields;
        private String specifiedField;

        private ViewPermissionDTO getViewPermission() {
            return viewPermission;
        }

        private void setViewPermission(ViewPermissionDTO viewPermission) {
            this.viewPermission = viewPermission;
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
