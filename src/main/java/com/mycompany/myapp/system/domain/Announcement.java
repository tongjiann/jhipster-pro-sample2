package com.mycompany.myapp.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.mycompany.myapp.domain.AbstractAuditingEntity;
import com.mycompany.myapp.domain.enumeration.AnnoBusinessType;
import com.mycompany.myapp.domain.enumeration.AnnoCategory;
import com.mycompany.myapp.domain.enumeration.AnnoOpenType;
import com.mycompany.myapp.domain.enumeration.AnnoSendStatus;
import com.mycompany.myapp.domain.enumeration.PriorityLevel;
import com.mycompany.myapp.domain.enumeration.ReceiverType;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * 系统通告
 */

@TableName(value = "announcement")
public class Announcement extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    @TableField(value = "titile")
    private String titile;

    /**
     * 内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 开始时间
     */
    @TableField(value = "start_time")
    private ZonedDateTime startTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private ZonedDateTime endTime;

    /**
     * 发布人Id
     */
    @TableField(value = "sender_id")
    private Long senderId;

    /**
     * 优先级\n（L低，M中，H高）
     */
    @TableField(value = "priority")
    private PriorityLevel priority;

    /**
     * 消息类型\n通知公告,系统消息
     */
    @TableField(value = "category")
    private AnnoCategory category;

    /**
     * 通告对象类型\n（USER:指定用户，ALL:全体用户）
     */
    @TableField(value = "receiver_type")
    private ReceiverType receiverType;

    /**
     * 发布状态\n
     */
    @TableField(value = "send_status")
    private AnnoSendStatus sendStatus;

    /**
     * 发布时间
     */
    @TableField(value = "send_time")
    private ZonedDateTime sendTime;

    /**
     * 撤销时间
     */
    @TableField(value = "cancel_time")
    private ZonedDateTime cancelTime;

    /**
     * 业务类型\n(email:邮件 bpm:流程)
     */
    @TableField(value = "business_type")
    private AnnoBusinessType businessType;

    /**
     * 业务id
     */
    @TableField(value = "business_id")
    private Long businessId;

    /**
     * 打开方式
     */
    @TableField(value = "open_type")
    private AnnoOpenType openType;

    /**
     * 组件/路由 地址
     */
    @TableField(value = "open_page")
    private String openPage;

    /**
     * 指定接收者id
     */
    @TableField(value = "receiver_ids", typeHandler = org.apache.ibatis.type.ArrayTypeHandler.class)
    private Long[] receiverIds;

    /**
     * 摘要
     */
    @TableField(value = "summary")
    private String summary;

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

    public Announcement id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitile() {
        return this.titile;
    }

    public Announcement titile(String titile) {
        this.titile = titile;
        return this;
    }

    public void setTitile(String titile) {
        this.titile = titile;
    }

    public String getContent() {
        return this.content;
    }

    public Announcement content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getStartTime() {
        return this.startTime;
    }

    public Announcement startTime(ZonedDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return this.endTime;
    }

    public Announcement endTime(ZonedDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getSenderId() {
        return this.senderId;
    }

    public Announcement senderId(Long senderId) {
        this.senderId = senderId;
        return this;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public PriorityLevel getPriority() {
        return this.priority;
    }

    public Announcement priority(PriorityLevel priority) {
        this.priority = priority;
        return this;
    }

    public void setPriority(PriorityLevel priority) {
        this.priority = priority;
    }

    public AnnoCategory getCategory() {
        return this.category;
    }

    public Announcement category(AnnoCategory category) {
        this.category = category;
        return this;
    }

    public void setCategory(AnnoCategory category) {
        this.category = category;
    }

    public ReceiverType getReceiverType() {
        return this.receiverType;
    }

    public Announcement receiverType(ReceiverType receiverType) {
        this.receiverType = receiverType;
        return this;
    }

    public void setReceiverType(ReceiverType receiverType) {
        this.receiverType = receiverType;
    }

    public AnnoSendStatus getSendStatus() {
        return this.sendStatus;
    }

    public Announcement sendStatus(AnnoSendStatus sendStatus) {
        this.sendStatus = sendStatus;
        return this;
    }

    public void setSendStatus(AnnoSendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public ZonedDateTime getSendTime() {
        return this.sendTime;
    }

    public Announcement sendTime(ZonedDateTime sendTime) {
        this.sendTime = sendTime;
        return this;
    }

    public void setSendTime(ZonedDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public ZonedDateTime getCancelTime() {
        return this.cancelTime;
    }

    public Announcement cancelTime(ZonedDateTime cancelTime) {
        this.cancelTime = cancelTime;
        return this;
    }

    public void setCancelTime(ZonedDateTime cancelTime) {
        this.cancelTime = cancelTime;
    }

    public AnnoBusinessType getBusinessType() {
        return this.businessType;
    }

    public Announcement businessType(AnnoBusinessType businessType) {
        this.businessType = businessType;
        return this;
    }

    public void setBusinessType(AnnoBusinessType businessType) {
        this.businessType = businessType;
    }

    public Long getBusinessId() {
        return this.businessId;
    }

    public Announcement businessId(Long businessId) {
        this.businessId = businessId;
        return this;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public AnnoOpenType getOpenType() {
        return this.openType;
    }

    public Announcement openType(AnnoOpenType openType) {
        this.openType = openType;
        return this;
    }

    public void setOpenType(AnnoOpenType openType) {
        this.openType = openType;
    }

    public String getOpenPage() {
        return this.openPage;
    }

    public Announcement openPage(String openPage) {
        this.openPage = openPage;
        return this;
    }

    public void setOpenPage(String openPage) {
        this.openPage = openPage;
    }

    public Long[] getReceiverIds() {
        return this.receiverIds;
    }

    public Announcement receiverIds(Long[] receiverIds) {
        this.receiverIds = receiverIds;
        return this;
    }

    public void setReceiverIds(Long[] receiverIds) {
        this.receiverIds = receiverIds;
    }

    public String getSummary() {
        return this.summary;
    }

    public Announcement summary(String summary) {
        this.summary = summary;
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public LocalDate getRemovedAt() {
        return this.removedAt;
    }

    public Announcement removedAt(LocalDate removedAt) {
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
        if (!(o instanceof Announcement)) {
            return false;
        }
        return id != null && id.equals(((Announcement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Announcement{" +
            "id=" + getId() +
            ", titile='" + getTitile() + "'" +
            ", content='" + getContent() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", senderId=" + getSenderId() +
            ", priority='" + getPriority() + "'" +
            ", category='" + getCategory() + "'" +
            ", receiverType='" + getReceiverType() + "'" +
            ", sendStatus='" + getSendStatus() + "'" +
            ", sendTime='" + getSendTime() + "'" +
            ", cancelTime='" + getCancelTime() + "'" +
            ", businessType='" + getBusinessType() + "'" +
            ", businessId=" + getBusinessId() +
            ", openType='" + getOpenType() + "'" +
            ", openPage='" + getOpenPage() + "'" +
            ", receiverIds='" + getReceiverIds() + "'" +
            ", summary='" + getSummary() + "'" +
            ", removedAt='" + getRemovedAt() + "'" +
            "}";
    }
}
