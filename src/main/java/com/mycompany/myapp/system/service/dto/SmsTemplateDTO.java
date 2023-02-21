package com.mycompany.myapp.system.service.dto;

import com.mycompany.myapp.domain.enumeration.MessageSendType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.mycompany.myapp.system.domain.SmsTemplate}的DTO。
 */
@ApiModel(description = "消息模板")
public class SmsTemplateDTO implements Serializable {

    /**
     *
     */
    private Long id;

    /**
     * 模板标题
     */
    @ApiModelProperty(value = "模板标题")
    private String name;

    /**
     * 模板CODE
     */
    @ApiModelProperty(value = "模板CODE")
    private String code;

    /**
     * 模板类型
     */
    @ApiModelProperty(value = "模板类型")
    private MessageSendType type;

    /**
     * 模板内容
     */
    @ApiModelProperty(value = "模板内容")
    private String content;

    /**
     * 模板测试json
     */
    @ApiModelProperty(value = "模板测试json")
    private String testJson;

    /**
     * 软删除时间
     */
    @ApiModelProperty(value = "软删除时间")
    private LocalDate removedAt;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public MessageSendType getType() {
        return type;
    }

    public void setType(MessageSendType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTestJson() {
        return testJson;
    }

    public void setTestJson(String testJson) {
        this.testJson = testJson;
    }

    public LocalDate getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(LocalDate removedAt) {
        this.removedAt = removedAt;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmsTemplateDTO)) {
            return false;
        }

        SmsTemplateDTO smsTemplateDTO = (SmsTemplateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, smsTemplateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SmsTemplateDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", type='" + getType() + "'" +
            ", content='" + getContent() + "'" +
            ", testJson='" + getTestJson() + "'" +
            ", removedAt='" + getRemovedAt() + "'" +
            "}";
    }
}
