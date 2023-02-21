package com.mycompany.myapp.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.repository.AuthorityRepository;
import com.mycompany.myapp.service.AuthorityQueryService;
import com.mycompany.myapp.service.AuthorityService;
import com.mycompany.myapp.service.criteria.AuthorityCriteria;
import com.mycompany.myapp.service.dto.AuthorityDTO;
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

 * 管理实体{@link com.mycompany.myapp.domain.Authority}的REST Controller。
 */
@RestController
@RequestMapping("/api")
@Api(value = "authorities", tags = "角色API接口")
public class AuthorityResource {

    private final Logger log = LoggerFactory.getLogger(AuthorityResource.class);

    private static final String ENTITY_NAME = "systemAuthority";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuthorityService authorityService;

    private final AuthorityRepository authorityRepository;

    private final AuthorityQueryService authorityQueryService;

    public AuthorityResource(
        AuthorityService authorityService,
        AuthorityRepository authorityRepository,
        AuthorityQueryService authorityQueryService
    ) {
        this.authorityService = authorityService;
        this.authorityRepository = authorityRepository;
        this.authorityQueryService = authorityQueryService;
    }

    /**
     * {@code POST  /authorities} : Create a new authority.
     *
     * @param authorityDTO the authorityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new authorityDTO, or with status {@code 400 (Bad Request)} if the authority has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/authorities")
    @ApiOperation(value = "新建角色", notes = "创建并返回一个新的角色")
    public ResponseEntity<AuthorityDTO> createAuthority(@RequestBody AuthorityDTO authorityDTO) throws URISyntaxException {
        log.debug("REST request to save Authority : {}", authorityDTO);
        if (authorityDTO.getId() != null) {
            throw new BadRequestAlertException("A new authority cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuthorityDTO result = authorityService.save(authorityDTO);
        return ResponseEntity
            .created(new URI("/api/authorities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /authorities/:id} : Updates an existing authority.
     *
     * @param id the id of the authorityDTO to save.
     * @param authorityDTO the authorityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authorityDTO,
     * or with status {@code 400 (Bad Request)} if the authorityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the authorityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/authorities/{id}")
    @ApiOperation(value = "更新角色", notes = "根据主键更新并返回一个更新后的角色")
    public ResponseEntity<AuthorityDTO> updateAuthority(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AuthorityDTO authorityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Authority : {}, {}", id, authorityDTO);
        if (authorityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, authorityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!authorityService.exists(Authority::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AuthorityDTO result = authorityService.save(authorityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authorityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /authorities/:id} : Partial updates given fields of an existing authority, field will ignore if it is null.
     *
     * @param id the id of the authorityDTO to save.
     * @param authorityDTO the authorityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authorityDTO,
     * or with status {@code 400 (Bad Request)} if the authorityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the authorityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the authorityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(value = "部分更新角色", notes = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的角色")
    @PatchMapping(value = "/authorities/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AuthorityDTO> partialUpdateAuthority(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AuthorityDTO authorityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Authority partially : {}, {}", id, authorityDTO);
        if (authorityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, authorityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (authorityRepository.findById(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AuthorityDTO> result = authorityService.partialUpdate(authorityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authorityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /authorities} : get all the authorities.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of authorities in body.
     */
    @GetMapping("/authorities")
    @ApiOperation(value = "获取角色分页列表", notes = "获取角色的分页列表数据")
    public ResponseEntity<List<AuthorityDTO>> getAllAuthorities(
        AuthorityCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get Authorities by criteria: {}", criteria);
        IPage<AuthorityDTO> page;
        page = authorityQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /authorities/count} : count all the authorities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/authorities/count")
    public ResponseEntity<Long> countAuthorities(AuthorityCriteria criteria) {
        log.debug("REST request to count Authorities by criteria: {}", criteria);
        return ResponseEntity.ok().body(authorityQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /authorities/tree : get all the authorities for parent is null.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of authorities in body
     */
    @GetMapping("/authorities/tree")
    @ApiOperation(value = "获取角色树形列表", notes = "获取角色的树形列表数据")
    public ResponseEntity<List<AuthorityDTO>> getAllAuthoritiesofTree(Pageable pageable) {
        log.debug("REST request to get a page of Authorities");
        IPage<AuthorityDTO> page = authorityService.findAllTop(PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * GET  /authorities/{parentId}/tree : get all the authorities for parent is parentId.
     *
     * @param parentId the parent of Id
     * @return the ResponseEntity with status 200 (OK) and the list of authorities in body
     */
    @GetMapping("/authorities/{parentId}/tree")
    @ApiOperation(value = "获取角色指定节点下的树形列表", notes = "获取角色指定节点下的树形列表数据")
    public ResponseEntity<List<AuthorityDTO>> getAllAuthoritiesofParent(@PathVariable Long parentId) {
        log.debug("REST request to get all Authorities of parentId");
        List<AuthorityDTO> list = authorityService.findChildrenByParentId(parentId);
        return ResponseEntity.ok().body(list);
    }

    /**
     * {@code GET  /authorities/:id} : get the "id" authority.
     *
     * @param id the id of the authorityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the authorityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/authorities/{id}")
    @ApiOperation(value = "获取指定主键的角色", notes = "获取指定主键的角色信息")
    public ResponseEntity<AuthorityDTO> getAuthority(@PathVariable Long id) {
        log.debug("REST request to get Authority : {}", id);
        Optional<AuthorityDTO> authorityDTO = authorityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(authorityDTO);
    }

    /**
     * GET  /authorities/export : export the authorities.
     *
     */
    @GetMapping("/authorities/export")
    @ApiOperation(value = "角色EXCEL导出", notes = "导出全部角色为EXCEL文件")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<AuthorityDTO> data = authorityService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("角色一览表", "角色"), AuthorityDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "角色_" + sdf.format(new Date()) + ".xlsx";
        ExportUtil.excel(workbook, filename, response);
    }

    /**
     * POST  /authorities/import : import the authorities from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the authorityDTO, or with status 404 (Not Found)
     */
    @PostMapping("/authorities/import")
    @ApiOperation(value = "角色EXCEL导入", notes = "根据角色EXCEL文件导入全部数据")
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<AuthorityDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), AuthorityDTO.class, params);
        list.forEach(authorityService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /authorities/:id} : delete the "id" authority.
     *
     * @param id the id of the authorityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/authorities/{id}")
    @ApiOperation(value = "删除一个角色", notes = "根据主键删除单个角色")
    public ResponseEntity<Void> deleteAuthority(@PathVariable Long id) {
        log.debug("REST request to delete Authority : {}", id);
        authorityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /authorities} : delete all the "ids" Authorities.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/authorities")
    @ApiOperation(value = "删除多个角色", notes = "根据主键删除多个角色")
    public ResponseEntity<Void> deleteAuthoritiesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete Authorities : {}", ids);
        if (ids != null) {
            ids.forEach(authorityService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /authorities/specified-fields} : Updates an existing authority by specified fields.
     *
     * @param authorityDTOAndSpecifiedFields the authorityDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authorityDTO,
     * or with status {@code 400 (Bad Request)} if the authorityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the authorityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/authorities/specified-fields")
    @ApiOperation(value = "根据字段部分更新角色", notes = "根据指定字段部分更新角色，给定的属性值可以为任何值，包括null")
    public ResponseEntity<AuthorityDTO> updateAuthorityDTOBySpecifiedFields(
        @RequestBody AuthorityDTOAndSpecifiedFields authorityDTOAndSpecifiedFields
    ) {
        log.debug("REST request to update AuthorityDTO : {}", authorityDTOAndSpecifiedFields);
        AuthorityDTO authorityDTO = authorityDTOAndSpecifiedFields.getAuthority();
        Set<String> specifiedFields = authorityDTOAndSpecifiedFields.getSpecifiedFields();
        if (authorityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AuthorityDTO result = authorityService.updateBySpecifiedFields(authorityDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authorityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /authorities/specified-field} : Updates an existing authorityDTO by specified field.
     *
     * @param authorityDTOAndSpecifiedFields the authorityDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authorityDTO,
     * or with status {@code 400 (Bad Request)} if the authorityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the authorityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/authorities/specified-field")
    @ApiOperation(value = "更新角色单个属性", notes = "根据指定字段更新角色，给定的属性值可以为任何值，包括null")
    public ResponseEntity<AuthorityDTO> updateAuthorityBySpecifiedField(
        @RequestBody AuthorityDTOAndSpecifiedFields authorityDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update authorityDTO : {}", authorityDTOAndSpecifiedFields);
        AuthorityDTO authorityDTO = authorityDTOAndSpecifiedFields.getAuthority();
        String fieldName = authorityDTOAndSpecifiedFields.getSpecifiedField();
        if (authorityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AuthorityDTO result = authorityService.updateBySpecifiedField(authorityDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class AuthorityDTOAndSpecifiedFields {

        private AuthorityDTO authority;
        private Set<String> specifiedFields;
        private String specifiedField;

        private AuthorityDTO getAuthority() {
            return authority;
        }

        private void setAuthority(AuthorityDTO authority) {
            this.authority = authority;
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
