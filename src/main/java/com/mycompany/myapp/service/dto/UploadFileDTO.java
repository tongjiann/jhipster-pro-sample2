package com.mycompany.myapp.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import org.springframework.web.multipart.MultipartFile;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.mycompany.myapp.domain.UploadFile}的DTO。
 */
@ApiModel(description = "上传文件")
public class UploadFileDTO implements Serializable {

    /**
     *
     */
    private Long id;

    /**
     * 完整文件名\n不含路径
     */
    @ApiModelProperty(value = "完整文件名\n不含路径")
    private String fullName;

    /**
     * 文件名\n不含扩展名
     */
    @ApiModelProperty(value = "文件名\n不含扩展名")
    private String name;

    /**
     * 扩展名
     */
    @ApiModelProperty(value = "扩展名")
    private String ext;

    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型")
    private String type;

    /**
     * Url地址
     */
    @ApiModelProperty(value = "Url地址")
    private String url;

    /**
     * 本地路径
     */
    @ApiModelProperty(value = "本地路径")
    private String path;

    /**
     * 存储目录
     */
    @ApiModelProperty(value = "存储目录")
    private String folder;

    /**
     * 实体名称
     */
    @ApiModelProperty(value = "实体名称")
    private String entityName;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private ZonedDateTime createAt;

    /**
     * 文件大小
     */
    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    /**
     * 被引次数
     */
    @ApiModelProperty(value = "被引次数")
    private Long referenceCount;

    /**
     * 软删除时间
     */
    @ApiModelProperty(value = "软删除时间")
    private LocalDate removedAt;

    /**
     *
     */
    private UserDTO user;

    /**
     *
     */
    private ResourceCategoryDTO category;

    private MultipartFile file;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getReferenceCount() {
        return referenceCount;
    }

    public void setReferenceCount(Long referenceCount) {
        this.referenceCount = referenceCount;
    }

    public LocalDate getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(LocalDate removedAt) {
        this.removedAt = removedAt;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public ResourceCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(ResourceCategoryDTO category) {
        this.category = category;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UploadFileDTO)) {
            return false;
        }

        UploadFileDTO uploadFileDTO = (UploadFileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, uploadFileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UploadFileDTO{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", name='" + getName() + "'" +
            ", ext='" + getExt() + "'" +
            ", type='" + getType() + "'" +
            ", url='" + getUrl() + "'" +
            ", path='" + getPath() + "'" +
            ", folder='" + getFolder() + "'" +
            ", entityName='" + getEntityName() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", fileSize=" + getFileSize() +
            ", referenceCount=" + getReferenceCount() +
            ", removedAt='" + getRemovedAt() + "'" +
            ", user=" + getUser() +
            ", category=" + getCategory() +
            "}";
    }
}
