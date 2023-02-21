package com.mycompany.myapp.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.domain.ResourceCategory;
import com.mycompany.myapp.repository.ResourceCategoryRepository;
import com.mycompany.myapp.service.ResourceCategoryQueryService;
import com.mycompany.myapp.service.ResourceCategoryService;
import com.mycompany.myapp.service.criteria.ResourceCategoryCriteria;
import com.mycompany.myapp.service.dto.ResourceCategoryDTO;
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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

 * 管理实体{@link com.mycompany.myapp.domain.ResourceCategory}的REST Controller。
 */
@RestController
@RequestMapping("/api")
@Api(value = "resource-categories", tags = "资源分类API接口")
public class ResourceCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ResourceCategoryResource.class);

    private static final String ENTITY_NAME = "filesResourceCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResourceCategoryService resourceCategoryService;

    private final ResourceCategoryRepository resourceCategoryRepository;

    private final ResourceCategoryQueryService resourceCategoryQueryService;

    public ResourceCategoryResource(
        ResourceCategoryService resourceCategoryService,
        ResourceCategoryRepository resourceCategoryRepository,
        ResourceCategoryQueryService resourceCategoryQueryService
    ) {
        this.resourceCategoryService = resourceCategoryService;
        this.resourceCategoryRepository = resourceCategoryRepository;
        this.resourceCategoryQueryService = resourceCategoryQueryService;
    }

    /**
     * {@code POST  /resource-categories} : Create a new resourceCategory.
     *
     * @param resourceCategoryDTO the resourceCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourceCategoryDTO, or with status {@code 400 (Bad Request)} if the resourceCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resource-categories")
    @ApiOperation(value = "新建资源分类", notes = "创建并返回一个新的资源分类")
    public ResponseEntity<ResourceCategoryDTO> createResourceCategory(@Valid @RequestBody ResourceCategoryDTO resourceCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save ResourceCategory : {}", resourceCategoryDTO);
        if (resourceCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new resourceCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResourceCategoryDTO result = resourceCategoryService.save(resourceCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/resource-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resource-categories/:id} : Updates an existing resourceCategory.
     *
     * @param id the id of the resourceCategoryDTO to save.
     * @param resourceCategoryDTO the resourceCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the resourceCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resource-categories/{id}")
    @ApiOperation(value = "更新资源分类", notes = "根据主键更新并返回一个更新后的资源分类")
    public ResponseEntity<ResourceCategoryDTO> updateResourceCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ResourceCategoryDTO resourceCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ResourceCategory : {}, {}", id, resourceCategoryDTO);
        if (resourceCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourceCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resourceCategoryService.exists(ResourceCategory::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ResourceCategoryDTO result = resourceCategoryService.save(resourceCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /resource-categories/:id} : Partial updates given fields of an existing resourceCategory, field will ignore if it is null.
     *
     * @param id the id of the resourceCategoryDTO to save.
     * @param resourceCategoryDTO the resourceCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the resourceCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the resourceCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the resourceCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(value = "部分更新资源分类", notes = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的资源分类")
    @PatchMapping(value = "/resource-categories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ResourceCategoryDTO> partialUpdateResourceCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ResourceCategoryDTO resourceCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ResourceCategory partially : {}, {}", id, resourceCategoryDTO);
        if (resourceCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourceCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (resourceCategoryRepository.findById(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ResourceCategoryDTO> result = resourceCategoryService.partialUpdate(resourceCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /resource-categories} : get all the resourceCategories.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourceCategories in body.
     */
    @GetMapping("/resource-categories")
    @ApiOperation(value = "获取资源分类分页列表", notes = "获取资源分类的分页列表数据")
    public ResponseEntity<List<ResourceCategoryDTO>> getAllResourceCategories(
        ResourceCategoryCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get ResourceCategories by criteria: {}", criteria);
        IPage<ResourceCategoryDTO> page;
        page = resourceCategoryQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /resource-categories/count} : count all the resourceCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/resource-categories/count")
    public ResponseEntity<Long> countResourceCategories(ResourceCategoryCriteria criteria) {
        log.debug("REST request to count ResourceCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(resourceCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /resource-categories/tree : get all the resourceCategories for parent is null.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of resourceCategories in body
     */
    @GetMapping("/resource-categories/tree")
    @ApiOperation(value = "获取资源分类树形列表", notes = "获取资源分类的树形列表数据")
    public ResponseEntity<List<ResourceCategoryDTO>> getAllResourceCategoriesofTree(Pageable pageable) {
        log.debug("REST request to get a page of ResourceCategories");
        IPage<ResourceCategoryDTO> page = resourceCategoryService.findAllTop(PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * GET  /resource-categories/{parentId}/tree : get all the resourceCategories for parent is parentId.
     *
     * @param parentId the parent of Id
     * @return the ResponseEntity with status 200 (OK) and the list of resourceCategories in body
     */
    @GetMapping("/resource-categories/{parentId}/tree")
    @ApiOperation(value = "获取资源分类指定节点下的树形列表", notes = "获取资源分类指定节点下的树形列表数据")
    public ResponseEntity<List<ResourceCategoryDTO>> getAllResourceCategoriesofParent(@PathVariable Long parentId) {
        log.debug("REST request to get all ResourceCategories of parentId");
        List<ResourceCategoryDTO> list = resourceCategoryService.findChildrenByParentId(parentId);
        return ResponseEntity.ok().body(list);
    }

    /**
     * {@code GET  /resource-categories/:id} : get the "id" resourceCategory.
     *
     * @param id the id of the resourceCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourceCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resource-categories/{id}")
    @ApiOperation(value = "获取指定主键的资源分类", notes = "获取指定主键的资源分类信息")
    public ResponseEntity<ResourceCategoryDTO> getResourceCategory(@PathVariable Long id) {
        log.debug("REST request to get ResourceCategory : {}", id);
        Optional<ResourceCategoryDTO> resourceCategoryDTO = resourceCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resourceCategoryDTO);
    }

    /**
     * GET  /resource-categories/export : export the resourceCategories.
     *
     */
    @GetMapping("/resource-categories/export")
    @ApiOperation(value = "资源分类EXCEL导出", notes = "导出全部资源分类为EXCEL文件")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<ResourceCategoryDTO> data = resourceCategoryService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("资源分类一览表", "资源分类"), ResourceCategoryDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "资源分类_" + sdf.format(new Date()) + ".xlsx";
        ExportUtil.excel(workbook, filename, response);
    }

    /**
     * POST  /resource-categories/import : import the resourceCategories from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the resourceCategoryDTO, or with status 404 (Not Found)
     */
    @PostMapping("/resource-categories/import")
    @ApiOperation(value = "资源分类EXCEL导入", notes = "根据资源分类EXCEL文件导入全部数据")
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<ResourceCategoryDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), ResourceCategoryDTO.class, params);
        list.forEach(resourceCategoryService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /resource-categories/:id} : delete the "id" resourceCategory.
     *
     * @param id the id of the resourceCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resource-categories/{id}")
    @ApiOperation(value = "删除一个资源分类", notes = "根据主键删除单个资源分类")
    public ResponseEntity<Void> deleteResourceCategory(@PathVariable Long id) {
        log.debug("REST request to delete ResourceCategory : {}", id);
        resourceCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /resource-categories} : delete all the "ids" ResourceCategories.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resource-categories")
    @ApiOperation(value = "删除多个资源分类", notes = "根据主键删除多个资源分类")
    public ResponseEntity<Void> deleteResourceCategoriesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete ResourceCategories : {}", ids);
        if (ids != null) {
            ids.forEach(resourceCategoryService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /resource-categories/specified-fields} : Updates an existing resourceCategory by specified fields.
     *
     * @param resourceCategoryDTOAndSpecifiedFields the resourceCategoryDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the resourceCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resource-categories/specified-fields")
    @ApiOperation(value = "根据字段部分更新资源分类", notes = "根据指定字段部分更新资源分类，给定的属性值可以为任何值，包括null")
    public ResponseEntity<ResourceCategoryDTO> updateResourceCategoryDTOBySpecifiedFields(
        @RequestBody ResourceCategoryDTOAndSpecifiedFields resourceCategoryDTOAndSpecifiedFields
    ) {
        log.debug("REST request to update ResourceCategoryDTO : {}", resourceCategoryDTOAndSpecifiedFields);
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryDTOAndSpecifiedFields.getResourceCategory();
        Set<String> specifiedFields = resourceCategoryDTOAndSpecifiedFields.getSpecifiedFields();
        if (resourceCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ResourceCategoryDTO result = resourceCategoryService.updateBySpecifiedFields(resourceCategoryDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resource-categories/specified-field} : Updates an existing resourceCategoryDTO by specified field.
     *
     * @param resourceCategoryDTOAndSpecifiedFields the resourceCategoryDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the resourceCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resource-categories/specified-field")
    @ApiOperation(value = "更新资源分类单个属性", notes = "根据指定字段更新资源分类，给定的属性值可以为任何值，包括null")
    public ResponseEntity<ResourceCategoryDTO> updateResourceCategoryBySpecifiedField(
        @RequestBody ResourceCategoryDTOAndSpecifiedFields resourceCategoryDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update resourceCategoryDTO : {}", resourceCategoryDTOAndSpecifiedFields);
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryDTOAndSpecifiedFields.getResourceCategory();
        String fieldName = resourceCategoryDTOAndSpecifiedFields.getSpecifiedField();
        if (resourceCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ResourceCategoryDTO result = resourceCategoryService.updateBySpecifiedField(resourceCategoryDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class ResourceCategoryDTOAndSpecifiedFields {

        private ResourceCategoryDTO resourceCategory;
        private Set<String> specifiedFields;
        private String specifiedField;

        private ResourceCategoryDTO getResourceCategory() {
            return resourceCategory;
        }

        private void setResourceCategory(ResourceCategoryDTO resourceCategory) {
            this.resourceCategory = resourceCategory;
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
