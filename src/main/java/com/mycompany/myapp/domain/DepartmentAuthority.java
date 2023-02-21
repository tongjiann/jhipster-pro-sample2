package com.mycompany.myapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门角色
 */

@TableName(value = "department_authority")
public class DepartmentAuthority implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 代码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 说明
     */
    @TableField(value = "description")
    private String description;

    /**
     * 创建用户 Id
     */
    @TableField(value = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private ZonedDateTime createTime;

    /**
     * 软删除时间
     */
    @TableField(value = "removed_at")
    private LocalDate removedAt;

    @TableField(value = "department_id")
    private Long departmentId;

    /**
     * 人员列表\n
     */
    @TableField(exist = false)
    @BindEntityList(entity = User.class, condition = "id=department_authority_id")
    @JsonIgnoreProperties(value = { "department", "position" }, allowSetters = true)
    private List<User> users = new ArrayList<>();

    /**
     * API权限\n
     */
    @TableField(exist = false)
    @BindEntityList(entity = ApiPermission.class, condition = "id=department_authority_id")
    @JsonIgnoreProperties(value = { "children", "departmentAuthority", "parent", "authorities" }, allowSetters = true)
    private List<ApiPermission> apiPermissions = new ArrayList<>();

    /**
     * 可视权限\n
     */
    @TableField(exist = false)
    @BindEntityList(entity = ViewPermission.class, condition = "id=department_authority_id")
    @JsonIgnoreProperties(value = { "children", "departmentAuthority", "parent", "authorities" }, allowSetters = true)
    private List<ViewPermission> viewPermissions = new ArrayList<>();

    /**
     * 所属部门
     */
    @TableField(exist = false)
    @BindEntity(entity = Department.class, condition = "this.department_id=id")
    @JsonIgnoreProperties(value = { "children", "authorities", "parent", "users", "departmentAuthorities" }, allowSetters = true)
    private Department department;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DepartmentAuthority id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public DepartmentAuthority name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public DepartmentAuthority code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public DepartmentAuthority description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreateUserId() {
        return this.createUserId;
    }

    public DepartmentAuthority createUserId(Long createUserId) {
        this.createUserId = createUserId;
        return this;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public ZonedDateTime getCreateTime() {
        return this.createTime;
    }

    public DepartmentAuthority createTime(ZonedDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDate getRemovedAt() {
        return this.removedAt;
    }

    public DepartmentAuthority removedAt(LocalDate removedAt) {
        this.removedAt = removedAt;
        return this;
    }

    public void setRemovedAt(LocalDate removedAt) {
        this.removedAt = removedAt;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public DepartmentAuthority users(List<User> users) {
        this.setUsers(users);
        return this;
    }

    public DepartmentAuthority addUsers(User user) {
        this.users.add(user);
        return this;
    }

    public DepartmentAuthority removeUsers(User user) {
        this.users.remove(user);
        return this;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<ApiPermission> getApiPermissions() {
        return this.apiPermissions;
    }

    public DepartmentAuthority apiPermissions(List<ApiPermission> apiPermissions) {
        this.setApiPermissions(apiPermissions);
        return this;
    }

    public DepartmentAuthority addApiPermissions(ApiPermission apiPermission) {
        this.apiPermissions.add(apiPermission);
        return this;
    }

    public DepartmentAuthority removeApiPermissions(ApiPermission apiPermission) {
        this.apiPermissions.remove(apiPermission);
        return this;
    }

    public void setApiPermissions(List<ApiPermission> apiPermissions) {
        this.apiPermissions = apiPermissions;
    }

    public List<ViewPermission> getViewPermissions() {
        return this.viewPermissions;
    }

    public DepartmentAuthority viewPermissions(List<ViewPermission> viewPermissions) {
        this.setViewPermissions(viewPermissions);
        return this;
    }

    public DepartmentAuthority addViewPermissions(ViewPermission viewPermission) {
        this.viewPermissions.add(viewPermission);
        return this;
    }

    public DepartmentAuthority removeViewPermissions(ViewPermission viewPermission) {
        this.viewPermissions.remove(viewPermission);
        return this;
    }

    public void setViewPermissions(List<ViewPermission> viewPermissions) {
        this.viewPermissions = viewPermissions;
    }

    public Department getDepartment() {
        return this.department;
    }

    public DepartmentAuthority department(Department department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartmentAuthority)) {
            return false;
        }
        return id != null && id.equals(((DepartmentAuthority) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartmentAuthority{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", createUserId=" + getCreateUserId() +
            ", createTime='" + getCreateTime() + "'" +
            ", removedAt='" + getRemovedAt() + "'" +
            "}";
    }
}
