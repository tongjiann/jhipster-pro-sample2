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
 * A DTO for the {@link com.mycompany.myapp.domain.RegionCode} entity.
 */
@ApiModel(description = "行政区划码")
public class RegionCodeSimpleDTO implements Serializable {

    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

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

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegionCodeDTO)) {
            return false;
        }

        RegionCodeSimpleDTO regionCodeDTO = (RegionCodeSimpleDTO) o;
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
            "}";
    }
}
