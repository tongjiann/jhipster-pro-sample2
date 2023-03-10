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

 * {@link com.mycompany.myapp.domain.UploadImage}的DTO。
 */
@ApiModel(description = "上传图片")
public class UploadImageDTO implements Serializable {

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
     * Web Url地址
     */
    @ApiModelProperty(value = "Web Url地址")
    private String url;

    /**
     * 本地路径
     */
    @ApiModelProperty(value = "本地路径")
    private String path;

    /**
     * 本地存储目录
     */
    @ApiModelProperty(value = "本地存储目录")
    private String folder;

    /**
     * 使用实体名称
     */
    @ApiModelProperty(value = "使用实体名称")
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
     * 小图Url
     */
    @ApiModelProperty(value = "小图Url")
    private String smartUrl;

    /**
     * 中等图Url
     */
    @ApiModelProperty(value = "中等图Url")
    private String mediumUrl;

    /**
     * 文件被引用次数
     */
    @ApiModelProperty(value = "文件被引用次数")
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

    private MultipartFile image;

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

    public String getSmartUrl() {
        return smartUrl;
    }

    public void setSmartUrl(String smartUrl) {
        this.smartUrl = smartUrl;
    }

    public String getMediumUrl() {
        return mediumUrl;
    }

    public void setMediumUrl(String mediumUrl) {
        this.mediumUrl = mediumUrl;
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

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UploadImageDTO)) {
            return false;
        }

        UploadImageDTO uploadImageDTO = (UploadImageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, uploadImageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UploadImageDTO{" +
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
            ", smartUrl='" + getSmartUrl() + "'" +
            ", mediumUrl='" + getMediumUrl() + "'" +
            ", referenceCount=" + getReferenceCount() +
            ", removedAt='" + getRemovedAt() + "'" +
            ", user=" + getUser() +
            ", category=" + getCategory() +
            "}";
    }
}
