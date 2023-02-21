package com.mycompany.myapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.RegionCodeLevel;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 行政区划码
 */

@TableName(value = "region_code")
public class RegionCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 地区代码
     */
    @TableField(value = "area_code")
    private String areaCode;

    /**
     * 城市代码
     */
    @TableField(value = "city_code")
    private String cityCode;

    /**
     * 全名
     */
    @TableField(value = "merger_name")
    private String mergerName;

    /**
     * 短名称
     */
    @TableField(value = "short_name")
    private String shortName;

    /**
     * 邮政编码
     */
    @TableField(value = "zip_code")
    private String zipCode;

    /**
     * 等级
     */
    @TableField(value = "level")
    private RegionCodeLevel level;

    /**
     * 经度
     */
    @TableField(value = "lng")
    private Double lng;

    /**
     * 纬度
     */
    @TableField(value = "lat")
    private Double lat;

    /**
     * 软删除时间
     */
    @TableField(value = "removed_at")
    private LocalDate removedAt;

    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 子节点
     */
    @TableField(exist = false)
    @BindEntityList(entity = RegionCode.class, deepBind = true, condition = "id=parent_id")
    @JsonIgnoreProperties(value = { "children", "parent" }, allowSetters = true)
    private List<RegionCode> children = new ArrayList<>();

    /**
     * 上级节点
     */
    @TableField(exist = false)
    @BindEntity(entity = RegionCode.class, condition = "this.parent_id=id")
    @JsonIgnoreProperties(value = { "children", "parent" }, allowSetters = true)
    private RegionCode parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegionCode id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public RegionCode name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAreaCode() {
        return this.areaCode;
    }

    public RegionCode areaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getCityCode() {
        return this.cityCode;
    }

    public RegionCode cityCode(String cityCode) {
        this.cityCode = cityCode;
        return this;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getMergerName() {
        return this.mergerName;
    }

    public RegionCode mergerName(String mergerName) {
        this.mergerName = mergerName;
        return this;
    }

    public void setMergerName(String mergerName) {
        this.mergerName = mergerName;
    }

    public String getShortName() {
        return this.shortName;
    }

    public RegionCode shortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public RegionCode zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public RegionCodeLevel getLevel() {
        return this.level;
    }

    public RegionCode level(RegionCodeLevel level) {
        this.level = level;
        return this;
    }

    public void setLevel(RegionCodeLevel level) {
        this.level = level;
    }

    public Double getLng() {
        return this.lng;
    }

    public RegionCode lng(Double lng) {
        this.lng = lng;
        return this;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return this.lat;
    }

    public RegionCode lat(Double lat) {
        this.lat = lat;
        return this;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public LocalDate getRemovedAt() {
        return this.removedAt;
    }

    public RegionCode removedAt(LocalDate removedAt) {
        this.removedAt = removedAt;
        return this;
    }

    public void setRemovedAt(LocalDate removedAt) {
        this.removedAt = removedAt;
    }

    public List<RegionCode> getChildren() {
        return this.children;
    }

    public RegionCode children(List<RegionCode> regionCodes) {
        this.setChildren(regionCodes);
        return this;
    }

    public RegionCode addChildren(RegionCode regionCode) {
        this.children.add(regionCode);
        regionCode.setParent(this);
        return this;
    }

    public RegionCode removeChildren(RegionCode regionCode) {
        this.children.remove(regionCode);
        regionCode.setParent(null);
        return this;
    }

    public void setChildren(List<RegionCode> regionCodes) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (regionCodes != null) {
            regionCodes.forEach(i -> i.setParent(this));
        }
        this.children = regionCodes;
    }

    public RegionCode getParent() {
        return this.parent;
    }

    public RegionCode parent(RegionCode regionCode) {
        this.setParent(regionCode);
        return this;
    }

    public void setParent(RegionCode regionCode) {
        this.parent = regionCode;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegionCode)) {
            return false;
        }
        return id != null && id.equals(((RegionCode) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegionCode{" +
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
            "}";
    }
}
