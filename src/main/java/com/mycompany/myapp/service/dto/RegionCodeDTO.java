package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.RegionCodeLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.mycompany.myapp.domain.RegionCode}的DTO。
 */
@ApiModel(description = "行政区划码")
public class RegionCodeDTO implements Serializable {

    /**
     *
     */
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 地区代码
     */
    @ApiModelProperty(value = "地区代码")
    private String areaCode;

    /**
     * 城市代码
     */
    @ApiModelProperty(value = "城市代码")
    private String cityCode;

    /**
     * 全名
     */
    @ApiModelProperty(value = "全名")
    private String mergerName;

    /**
     * 短名称
     */
    @ApiModelProperty(value = "短名称")
    private String shortName;

    /**
     * 邮政编码
     */
    @ApiModelProperty(value = "邮政编码")
    private String zipCode;

    /**
     * 等级
     */
    @ApiModelProperty(value = "等级")
    private RegionCodeLevel level;

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private Double lng;

    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private Double lat;

    /**
     * 软删除时间
     */
    @ApiModelProperty(value = "软删除时间")
    private LocalDate removedAt;

    /**
     *
     */
    private List<RegionCodeDTO> children = new ArrayList<>();

    /**
     *
     */
    private RegionCodeSimpleDTO parent;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getMergerName() {
        return mergerName;
    }

    public void setMergerName(String mergerName) {
        this.mergerName = mergerName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public RegionCodeLevel getLevel() {
        return level;
    }

    public void setLevel(RegionCodeLevel level) {
        this.level = level;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public LocalDate getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(LocalDate removedAt) {
        this.removedAt = removedAt;
    }

    public List<RegionCodeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<RegionCodeDTO> children) {
        this.children = children;
    }

    public RegionCodeSimpleDTO getParent() {
        return parent;
    }

    public void setParent(RegionCodeSimpleDTO parent) {
        this.parent = parent;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegionCodeDTO)) {
            return false;
        }

        RegionCodeDTO regionCodeDTO = (RegionCodeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, regionCodeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegionCodeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", areaCode='" + getAreaCode() + "'" +
            ", cityCode='" + getCityCode() + "'" +
            ", mergerName='" + getMergerName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", level='" + getLevel() + "'" +
            ", lng=" + getLng() +
            ", lat=" + getLat() +
            ", removedAt='" + getRemovedAt() + "'" +
            ", children=" + getChildren() +
            ", parent=" + getParent() +
            "}";
    }
}
