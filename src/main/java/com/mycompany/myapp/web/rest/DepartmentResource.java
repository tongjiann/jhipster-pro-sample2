package com.mycompany.myapp.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.domain.Department;
import com.mycompany.myapp.repository.DepartmentRepository;
import com.mycompany.myapp.service.DepartmentQueryService;
import com.mycompany.myapp.service.DepartmentService;
import com.mycompany.myapp.service.criteria.DepartmentCriteria;
import com.mycompany.myapp.service.dto.DepartmentDTO;
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

 * 管理实体{@link com.mycompany.myapp.domain.Department}的REST Controller。
 */
@RestController
@RequestMapping("/api")
@Api(value = "departments", tags = "部门API接口")
public class DepartmentResource {

    private final Logger log = LoggerFactory.getLogger(DepartmentResource.class);

    private static final String ENTITY_NAME = "settingsDepartment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartmentService departmentService;

    private final DepartmentRepository departmentRepository;

    private final DepartmentQueryService departmentQueryService;

    public DepartmentResource(
        DepartmentService departmentService,
        DepartmentRepository departmentRepository,
        DepartmentQueryService departmentQueryService
    ) {
        this.departmentService = departmentService;
        this.departmentRepository = departmentRepository;
        this.departmentQueryService = departmentQueryService;
    }

    /**
     * {@code POST  /departments} : Create a new department.
     *
     * @param departmentDTO the departmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departmentDTO, or with status {@code 400 (Bad Request)} if the department has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/departments")
    @ApiOperation(value = "新建部门", notes = "创建并返回一个新的部门")
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentDTO departmentDTO) throws URISyntaxException {
        log.debug("REST request to save Department : {}", departmentDTO);
        if (departmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new department cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepartmentDTO result = departmentService.save(departmentDTO);
        return ResponseEntity
            .created(new URI("/api/departments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /departments/:id} : Updates an existing department.
     *
     * @param id the id of the departmentDTO to save.
     * @param departmentDTO the departmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departmentDTO,
     * or with status {@code 400 (Bad Request)} if the departmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/departments/{id}")
    @ApiOperation(value = "更新部门", notes = "根据主键更新并返回一个更新后的部门")
    public ResponseEntity<DepartmentDTO> updateDepartment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DepartmentDTO departmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Department : {}, {}", id, departmentDTO);
        if (departmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departmentService.exists(Department::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DepartmentDTO result = departmentService.save(departmentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /departments/:id} : Partial updates given fields of an existing department, field will ignore if it is null.
     *
     * @param id the id of the departmentDTO to save.
     * @param departmentDTO the departmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departmentDTO,
     * or with status {@code 400 (Bad Request)} if the departmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the departmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the departmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(value = "部分更新部门", notes = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的部门")
    @PatchMapping(value = "/departments/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DepartmentDTO> partialUpdateDepartment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DepartmentDTO departmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Department partially : {}, {}", id, departmentDTO);
        if (departmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (departmentRepository.findById(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DepartmentDTO> result = departmentService.partialUpdate(departmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /departments} : get all the departments.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departments in body.
     */
    @GetMapping("/departments")
    @ApiOperation(value = "获取部门分页列表", notes = "获取部门的分页列表数据")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments(
        DepartmentCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get Departments by criteria: {}", criteria);
        IPage<DepartmentDTO> page;
        page = departmentQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /departments/count} : count all the departments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/departments/count")
    public ResponseEntity<Long> countDepartments(DepartmentCriteria criteria) {
        log.debug("REST request to count Departments by criteria: {}", criteria);
        return ResponseEntity.ok().body(departmentQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /departments/tree : get all the departments for parent is null.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of departments in body
     */
    @GetMapping("/departments/tree")
    @ApiOperation(value = "获取部门树形列表", notes = "获取部门的树形列表数据")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartmentsofTree(Pageable pageable) {
        log.debug("REST request to get a page of Departments");
        IPage<DepartmentDTO> page = departmentService.findAllTop(PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * GET  /departments/{parentId}/tree : get all the departments for parent is parentId.
     *
     * @param parentId the parent of Id
     * @return the ResponseEntity with status 200 (OK) and the list of departments in body
     */
    @GetMapping("/departments/{parentId}/tree")
    @ApiOperation(value = "获取部门指定节点下的树形列表", notes = "获取部门指定节点下的树形列表数据")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartmentsofParent(@PathVariable Long parentId) {
        log.debug("REST request to get all Departments of parentId");
        List<DepartmentDTO> list = departmentService.findChildrenByParentId(parentId);
        return ResponseEntity.ok().body(list);
    }

    /**
     * {@code GET  /departments/:id} : get the "id" department.
     *
     * @param id the id of the departmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/departments/{id}")
    @ApiOperation(value = "获取指定主键的部门", notes = "获取指定主键的部门信息")
    public ResponseEntity<DepartmentDTO> getDepartment(@PathVariable Long id) {
        log.debug("REST request to get Department : {}", id);
        Optional<DepartmentDTO> departmentDTO = departmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departmentDTO);
    }

    /**
     * GET  /departments/export : export the departments.
     *
     */
    @GetMapping("/departments/export")
    @ApiOperation(value = "部门EXCEL导出", notes = "导出全部部门为EXCEL文件")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<DepartmentDTO> data = departmentService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("部门一览表", "部门"), DepartmentDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "部门_" + sdf.format(new Date()) + ".xlsx";
        ExportUtil.excel(workbook, filename, response);
    }

    /**
     * POST  /departments/import : import the departments from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the departmentDTO, or with status 404 (Not Found)
     */
    @PostMapping("/departments/import")
    @ApiOperation(value = "部门EXCEL导入", notes = "根据部门EXCEL文件导入全部数据")
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<DepartmentDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), DepartmentDTO.class, params);
        list.forEach(departmentService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /departments/:id} : delete the "id" department.
     *
     * @param id the id of the departmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/departments/{id}")
    @ApiOperation(value = "删除一个部门", notes = "根据主键删除单个部门")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        log.debug("REST request to delete Department : {}", id);
        departmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /departments} : delete all the "ids" Departments.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/departments")
    @ApiOperation(value = "删除多个部门", notes = "根据主键删除多个部门")
    public ResponseEntity<Void> deleteDepartmentsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete Departments : {}", ids);
        if (ids != null) {
            ids.forEach(departmentService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /departments/specified-fields} : Updates an existing department by specified fields.
     *
     * @param departmentDTOAndSpecifiedFields the departmentDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departmentDTO,
     * or with status {@code 400 (Bad Request)} if the departmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/departments/specified-fields")
    @ApiOperation(value = "根据字段部分更新部门", notes = "根据指定字段部分更新部门，给定的属性值可以为任何值，包括null")
    public ResponseEntity<DepartmentDTO> updateDepartmentDTOBySpecifiedFields(
        @RequestBody DepartmentDTOAndSpecifiedFields departmentDTOAndSpecifiedFields
    ) {
        log.debug("REST request to update DepartmentDTO : {}", departmentDTOAndSpecifiedFields);
        DepartmentDTO departmentDTO = departmentDTOAndSpecifiedFields.getDepartment();
        Set<String> specifiedFields = departmentDTOAndSpecifiedFields.getSpecifiedFields();
        if (departmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DepartmentDTO result = departmentService.updateBySpecifiedFields(departmentDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /departments/specified-field} : Updates an existing departmentDTO by specified field.
     *
     * @param departmentDTOAndSpecifiedFields the departmentDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departmentDTO,
     * or with status {@code 400 (Bad Request)} if the departmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/departments/specified-field")
    @ApiOperation(value = "更新部门单个属性", notes = "根据指定字段更新部门，给定的属性值可以为任何值，包括null")
    public ResponseEntity<DepartmentDTO> updateDepartmentBySpecifiedField(
        @RequestBody DepartmentDTOAndSpecifiedFields departmentDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update departmentDTO : {}", departmentDTOAndSpecifiedFields);
        DepartmentDTO departmentDTO = departmentDTOAndSpecifiedFields.getDepartment();
        String fieldName = departmentDTOAndSpecifiedFields.getSpecifiedField();
        if (departmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DepartmentDTO result = departmentService.updateBySpecifiedField(departmentDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class DepartmentDTOAndSpecifiedFields {

        private DepartmentDTO department;
        private Set<String> specifiedFields;
        private String specifiedField;

        private DepartmentDTO getDepartment() {
            return department;
        }

        private void setDepartment(DepartmentDTO department) {
            this.department = department;
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
