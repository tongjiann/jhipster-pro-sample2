package com.mycompany.myapp.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.mycompany.myapp.domain.AbstractAuditingEntity;
import com.mycompany.myapp.domain.enumeration.MessageSendType;
import java.time.LocalDate;

/**
 * 消息模板
 */

@TableName(value = "sms_template")
public class SmsTemplate extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 模板标题
     */
    @TableField(value = "name")
    private String name;

    /**
     * 模板CODE
     */
    @TableField(value = "code")
    private String code;

    /**
     * 模板类型
     */
    @TableField(value = "type")
    private MessageSendType type;

    /**
     * 模板内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 模板测试json
     */
    @TableField(value = "test_json")
    private String testJson;

    /**
     * 软删除时间
     */
    @TableField(value = "removed_at")
    private LocalDate removedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SmsTemplate id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public SmsTemplate name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public SmsTemplate code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public MessageSendType getType() {
        return this.type;
    }

    public SmsTemplate type(MessageSendType type) {
        this.type = type;
        return this;
    }

    public void setType(MessageSendType type) {
        this.type = type;
    }

    public String getContent() {
        return this.content;
    }

    public SmsTemplate content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTestJson() {
        return this.testJson;
    }

    public SmsTemplate testJson(String testJson) {
        this.testJson = testJson;
        return this;
    }

    public void setTestJson(String testJson) {
        this.testJson = testJson;
    }

    public LocalDate getRemovedAt() {
        return this.removedAt;
    }

    public SmsTemplate removedAt(LocalDate removedAt) {
        this.removedAt = removedAt;
        return this;
    }

    public void setRemovedAt(LocalDate removedAt) {
        this.removedAt = removedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmsTemplate)) {
            return false;
        }
        return id != null && id.equals(((SmsTemplate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SmsTemplate{" +
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
