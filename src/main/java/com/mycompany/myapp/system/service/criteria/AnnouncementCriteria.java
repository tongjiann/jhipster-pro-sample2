package com.mycompany.myapp.system.service.criteria;

import com.mycompany.myapp.domain.enumeration.AnnoBusinessType;
import com.mycompany.myapp.domain.enumeration.AnnoCategory;
import com.mycompany.myapp.domain.enumeration.AnnoOpenType;
import com.mycompany.myapp.domain.enumeration.AnnoSendStatus;
import com.mycompany.myapp.domain.enumeration.PriorityLevel;
import com.mycompany.myapp.domain.enumeration.ReceiverType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.system.domain.Announcement} entity. This class is used
 * in {@link com.mycompany.myapp.system.web.rest.AnnouncementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /announcements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AnnouncementCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    private Boolean useOr = false;

    /**
     * Class for filtering PriorityLevel
     */
    public static class PriorityLevelFilter extends Filter<PriorityLevel> {

        public PriorityLevelFilter() {}

        public PriorityLevelFilter(PriorityLevelFilter filter) {
            super(filter);
        }

        @Override
        public PriorityLevelFilter copy() {
            return new PriorityLevelFilter(this);
        }
    }

    /**
     * Class for filtering AnnoCategory
     */
    public static class AnnoCategoryFilter extends Filter<AnnoCategory> {

        public AnnoCategoryFilter() {}

        public AnnoCategoryFilter(AnnoCategoryFilter filter) {
            super(filter);
        }

        @Override
        public AnnoCategoryFilter copy() {
            return new AnnoCategoryFilter(this);
        }
    }

    /**
     * Class for filtering ReceiverType
     */
    public static class ReceiverTypeFilter extends Filter<ReceiverType> {

        public ReceiverTypeFilter() {}

        public ReceiverTypeFilter(ReceiverTypeFilter filter) {
            super(filter);
        }

        @Override
        public ReceiverTypeFilter copy() {
            return new ReceiverTypeFilter(this);
        }
    }

    /**
     * Class for filtering AnnoSendStatus
     */
    public static class AnnoSendStatusFilter extends Filter<AnnoSendStatus> {

        public AnnoSendStatusFilter() {}

        public AnnoSendStatusFilter(AnnoSendStatusFilter filter) {
            super(filter);
        }

        @Override
        public AnnoSendStatusFilter copy() {
            return new AnnoSendStatusFilter(this);
        }
    }

    /**
     * Class for filtering AnnoBusinessType
     */
    public static class AnnoBusinessTypeFilter extends Filter<AnnoBusinessType> {

        public AnnoBusinessTypeFilter() {}

        public AnnoBusinessTypeFilter(AnnoBusinessTypeFilter filter) {
            super(filter);
        }

        @Override
        public AnnoBusinessTypeFilter copy() {
            return new AnnoBusinessTypeFilter(this);
        }
    }

    /**
     * Class for filtering AnnoOpenType
     */
    public static class AnnoOpenTypeFilter extends Filter<AnnoOpenType> {

        public AnnoOpenTypeFilter() {}

        public AnnoOpenTypeFilter(AnnoOpenTypeFilter filter) {
            super(filter);
        }

        @Override
        public AnnoOpenTypeFilter copy() {
            return new AnnoOpenTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter titile;

    private ZonedDateTimeFilter startTime;

    private ZonedDateTimeFilter endTime;

    private LongFilter senderId;

    private PriorityLevelFilter priority;

    private AnnoCategoryFilter category;

    private ReceiverTypeFilter receiverType;

    private AnnoSendStatusFilter sendStatus;

    private ZonedDateTimeFilter sendTime;

    private ZonedDateTimeFilter cancelTime;

    private AnnoBusinessTypeFilter businessType;

    private LongFilter businessId;

    private AnnoOpenTypeFilter openType;

    private StringFilter openPage;

    private Filter<Long[]> receiverIds;

    private LocalDateFilter removedAt;

    private LongFilter createdBy;

    private InstantFilter createdDate;

    private LongFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    public AnnouncementCriteria() {}

    public AnnouncementCriteria(AnnouncementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.titile = other.titile == null ? null : other.titile.copy();
        this.startTime = other.startTime == null ? null : other.startTime.copy();
        this.endTime = other.endTime == null ? null : other.endTime.copy();
        this.senderId = other.senderId == null ? null : other.senderId.copy();
        this.priority = other.priority == null ? null : other.priority.copy();
        this.category = other.category == null ? null : other.category.copy();
        this.receiverType = other.receiverType == null ? null : other.receiverType.copy();
        this.sendStatus = other.sendStatus == null ? null : other.sendStatus.copy();
        this.sendTime = other.sendTime == null ? null : other.sendTime.copy();
        this.cancelTime = other.cancelTime == null ? null : other.cancelTime.copy();
        this.businessType = other.businessType == null ? null : other.businessType.copy();
        this.businessId = other.businessId == null ? null : other.businessId.copy();
        this.openType = other.openType == null ? null : other.openType.copy();
        this.openPage = other.openPage == null ? null : other.openPage.copy();
        this.receiverIds = other.receiverIds == null ? null : other.receiverIds.copy();
        this.removedAt = other.removedAt == null ? null : other.removedAt.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
    }

    @Override
    public AnnouncementCriteria copy() {
        return new AnnouncementCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitile() {
        return titile;
    }

    public StringFilter titile() {
        if (titile == null) {
            titile = new StringFilter();
        }
        return titile;
    }

    public void setTitile(StringFilter titile) {
        this.titile = titile;
    }

    public ZonedDateTimeFilter getStartTime() {
        return startTime;
    }

    public ZonedDateTimeFilter startTime() {
        if (startTime == null) {
            startTime = new ZonedDateTimeFilter();
        }
        return startTime;
    }

    public void setStartTime(ZonedDateTimeFilter startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTimeFilter getEndTime() {
        return endTime;
    }

    public ZonedDateTimeFilter endTime() {
        if (endTime == null) {
            endTime = new ZonedDateTimeFilter();
        }
        return endTime;
    }

    public void setEndTime(ZonedDateTimeFilter endTime) {
        this.endTime = endTime;
    }

    public LongFilter getSenderId() {
        return senderId;
    }

    public LongFilter senderId() {
        if (senderId == null) {
            senderId = new LongFilter();
        }
        return senderId;
    }

    public void setSenderId(LongFilter senderId) {
        this.senderId = senderId;
    }

    public PriorityLevelFilter getPriority() {
        return priority;
    }

    public PriorityLevelFilter priority() {
        if (priority == null) {
            priority = new PriorityLevelFilter();
        }
        return priority;
    }

    public void setPriority(PriorityLevelFilter priority) {
        this.priority = priority;
    }

    public AnnoCategoryFilter getCategory() {
        return category;
    }

    public AnnoCategoryFilter category() {
        if (category == null) {
            category = new AnnoCategoryFilter();
        }
        return category;
    }

    public void setCategory(AnnoCategoryFilter category) {
        this.category = category;
    }

    public ReceiverTypeFilter getReceiverType() {
        return receiverType;
    }

    public ReceiverTypeFilter receiverType() {
        if (receiverType == null) {
            receiverType = new ReceiverTypeFilter();
        }
        return receiverType;
    }

    public void setReceiverType(ReceiverTypeFilter receiverType) {
        this.receiverType = receiverType;
    }

    public AnnoSendStatusFilter getSendStatus() {
        return sendStatus;
    }

    public AnnoSendStatusFilter sendStatus() {
        if (sendStatus == null) {
            sendStatus = new AnnoSendStatusFilter();
        }
        return sendStatus;
    }

    public void setSendStatus(AnnoSendStatusFilter sendStatus) {
        this.sendStatus = sendStatus;
    }

    public ZonedDateTimeFilter getSendTime() {
        return sendTime;
    }

    public ZonedDateTimeFilter sendTime() {
        if (sendTime == null) {
            sendTime = new ZonedDateTimeFilter();
        }
        return sendTime;
    }

    public void setSendTime(ZonedDateTimeFilter sendTime) {
        this.sendTime = sendTime;
    }

    public ZonedDateTimeFilter getCancelTime() {
        return cancelTime;
    }

    public ZonedDateTimeFilter cancelTime() {
        if (cancelTime == null) {
            cancelTime = new ZonedDateTimeFilter();
        }
        return cancelTime;
    }

    public void setCancelTime(ZonedDateTimeFilter cancelTime) {
        this.cancelTime = cancelTime;
    }

    public AnnoBusinessTypeFilter getBusinessType() {
        return businessType;
    }

    public AnnoBusinessTypeFilter businessType() {
        if (businessType == null) {
            businessType = new AnnoBusinessTypeFilter();
        }
        return businessType;
    }

    public void setBusinessType(AnnoBusinessTypeFilter businessType) {
        this.businessType = businessType;
    }

    public LongFilter getBusinessId() {
        return businessId;
    }

    public LongFilter businessId() {
        if (businessId == null) {
            businessId = new LongFilter();
        }
        return businessId;
    }

    public void setBusinessId(LongFilter businessId) {
        this.businessId = businessId;
    }

    public AnnoOpenTypeFilter getOpenType() {
        return openType;
    }

    public AnnoOpenTypeFilter openType() {
        if (openType == null) {
            openType = new AnnoOpenTypeFilter();
        }
        return openType;
    }

    public void setOpenType(AnnoOpenTypeFilter openType) {
        this.openType = openType;
    }

    public StringFilter getOpenPage() {
        return openPage;
    }

    public StringFilter openPage() {
        if (openPage == null) {
            openPage = new StringFilter();
        }
        return openPage;
    }

    public void setOpenPage(StringFilter openPage) {
        this.openPage = openPage;
    }

    public Filter<Long[]> getReceiverIds() {
        return receiverIds;
    }

    public Filter<Long[]> receiverIds() {
        if (receiverIds == null) {
            receiverIds = new Filter<Long[]>();
        }
        return receiverIds;
    }

    public void setReceiverIds(Filter<Long[]> receiverIds) {
        this.receiverIds = receiverIds;
    }

    public LocalDateFilter getRemovedAt() {
        return removedAt;
    }

    public LocalDateFilter removedAt() {
        if (removedAt == null) {
            removedAt = new LocalDateFilter();
        }
        return removedAt;
    }

    public void setRemovedAt(LocalDateFilter removedAt) {
        this.removedAt = removedAt;
    }

    public LongFilter getCreatedBy() {
        return createdBy;
    }

    public LongFilter createdBy() {
        if (createdBy == null) {
            createdBy = new LongFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(LongFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            createdDate = new InstantFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public LongFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public LongFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new LongFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(LongFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new InstantFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getJhiCommonSearchKeywords() {
        return jhiCommonSearchKeywords;
    }

    public void setJhiCommonSearchKeywords(String jhiCommonSearchKeywords) {
        this.jhiCommonSearchKeywords = jhiCommonSearchKeywords;
    }

    public Boolean getUseOr() {
        return useOr;
    }

    public void setUseOr(Boolean useOr) {
        this.useOr = useOr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AnnouncementCriteria that = (AnnouncementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titile, that.titile) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(senderId, that.senderId) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(category, that.category) &&
            Objects.equals(receiverType, that.receiverType) &&
            Objects.equals(sendStatus, that.sendStatus) &&
            Objects.equals(sendTime, that.sendTime) &&
            Objects.equals(cancelTime, that.cancelTime) &&
            Objects.equals(businessType, that.businessType) &&
            Objects.equals(businessId, that.businessId) &&
            Objects.equals(openType, that.openType) &&
            Objects.equals(openPage, that.openPage) &&
            Objects.equals(receiverIds, that.receiverIds) &&
            Objects.equals(removedAt, that.removedAt) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            titile,
            startTime,
            endTime,
            senderId,
            priority,
            category,
            receiverType,
            sendStatus,
            sendTime,
            cancelTime,
            businessType,
            businessId,
            openType,
            openPage,
            receiverIds,
            removedAt,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnnouncementCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (titile != null ? "titile=" + titile + ", " : "") +
                (startTime != null ? "startTime=" + startTime + ", " : "") +
                (endTime != null ? "endTime=" + endTime + ", " : "") +
                (senderId != null ? "senderId=" + senderId + ", " : "") +
                (priority != null ? "priority=" + priority + ", " : "") +
                (category != null ? "category=" + category + ", " : "") +
                (receiverType != null ? "receiverType=" + receiverType + ", " : "") +
                (sendStatus != null ? "sendStatus=" + sendStatus + ", " : "") +
                (sendTime != null ? "sendTime=" + sendTime + ", " : "") +
                (cancelTime != null ? "cancelTime=" + cancelTime + ", " : "") +
                (businessType != null ? "businessType=" + businessType + ", " : "") +
                (businessId != null ? "businessId=" + businessId + ", " : "") +
                (openType != null ? "openType=" + openType + ", " : "") +
                (openPage != null ? "openPage=" + openPage + ", " : "") +
                (receiverIds != null ? "receiverIds=" + receiverIds + ", " : "") +
                (removedAt != null ? "removedAt=" + removedAt + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
                (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
                (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
                "useOr=" + useOr +
            "}";
    }
}
