package com.mycompany.myapp.system.service.dto;

import com.mycompany.myapp.domain.enumeration.AnnoBusinessType;
import com.mycompany.myapp.domain.enumeration.AnnoCategory;
import com.mycompany.myapp.domain.enumeration.AnnoOpenType;
import com.mycompany.myapp.domain.enumeration.AnnoSendStatus;
import com.mycompany.myapp.domain.enumeration.PriorityLevel;
import com.mycompany.myapp.domain.enumeration.ReceiverType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.mycompany.myapp.system.domain.Announcement}的DTO。
 */
@ApiModel(description = "系统通告")
public class AnnouncementDTO implements Serializable {

    /**
     *
     */
    private Long id;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String titile;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private ZonedDateTime startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private ZonedDateTime endTime;

    /**
     * 发布人Id
     */
    @ApiModelProperty(value = "发布人Id")
    private Long senderId;

    /**
     * 优先级\n（L低，M中，H高）
     */
    @ApiModelProperty(value = "优先级\n（L低，M中，H高）")
    private PriorityLevel priority;

    /**
     * 消息类型\n通知公告,系统消息
     */
    @ApiModelProperty(value = "消息类型\n通知公告,系统消息")
    private AnnoCategory category;

    /**
     * 通告对象类型\n（USER:指定用户，ALL:全体用户）
     */
    @ApiModelProperty(value = "通告对象类型\n（USER:指定用户，ALL:全体用户）")
    private ReceiverType receiverType;

    /**
     * 发布状态\n
     */
    @ApiModelProperty(value = "发布状态\n")
    private AnnoSendStatus sendStatus;

    /**
     * 发布时间
     */
    @ApiModelProperty(value = "发布时间")
    private ZonedDateTime sendTime;

    /**
     * 撤销时间
     */
    @ApiModelProperty(value = "撤销时间")
    private ZonedDateTime cancelTime;

    /**
     * 业务类型\n(email:邮件 bpm:流程)
     */
    @ApiModelProperty(value = "业务类型\n(email:邮件 bpm:流程)")
    private AnnoBusinessType businessType;

    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    private Long businessId;

    /**
     * 打开方式
     */
    @ApiModelProperty(value = "打开方式")
    private AnnoOpenType openType;

    /**
     * 组件/路由 地址
     */
    @ApiModelProperty(value = "组件/路由 地址")
    private String openPage;

    /**
     * 指定接收者id
     */
    @ApiModelProperty(value = "指定接收者id")
    private Long[] receiverIds;

    /**
     * 摘要
     */
    @ApiModelProperty(value = "摘要")
    private String summary;

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

    public String getTitile() {
        return titile;
    }

    public void setTitile(String titile) {
        this.titile = titile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public PriorityLevel getPriority() {
        return priority;
    }

    public void setPriority(PriorityLevel priority) {
        this.priority = priority;
    }

    public AnnoCategory getCategory() {
        return category;
    }

    public void setCategory(AnnoCategory category) {
        this.category = category;
    }

    public ReceiverType getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(ReceiverType receiverType) {
        this.receiverType = receiverType;
    }

    public AnnoSendStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(AnnoSendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public ZonedDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(ZonedDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public ZonedDateTime getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(ZonedDateTime cancelTime) {
        this.cancelTime = cancelTime;
    }

    public AnnoBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(AnnoBusinessType businessType) {
        this.businessType = businessType;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public AnnoOpenType getOpenType() {
        return openType;
    }

    public void setOpenType(AnnoOpenType openType) {
        this.openType = openType;
    }

    public String getOpenPage() {
        return openPage;
    }

    public void setOpenPage(String openPage) {
        this.openPage = openPage;
    }

    public Long[] getReceiverIds() {
        return receiverIds;
    }

    public void setReceiverIds(Long[] receiverIds) {
        this.receiverIds = receiverIds;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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
        if (!(o instanceof AnnouncementDTO)) {
            return false;
        }

        AnnouncementDTO announcementDTO = (AnnouncementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, announcementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnnouncementDTO{" +
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
