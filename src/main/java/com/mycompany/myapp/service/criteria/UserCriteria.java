package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.User} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.UserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /my-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private String jhiCommonSearchKeywords;

    private Boolean useOr = false;

    private LongFilter id;

    private StringFilter login;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private StringFilter mobile;

    private ZonedDateTimeFilter birthday;

    private BooleanFilter activated;

    private StringFilter langKey;

    private StringFilter imageUrl;

    private StringFilter activationKey;

    private StringFilter resetKey;

    private InstantFilter resetDate;

    private LongFilter departmentId;

    private StringFilter departmentName;

    private LongFilter positionId;

    private StringFilter positionName;

    private LongFilter authoritiesId;

    private StringFilter authoritiesName;

    public UserCriteria() {}

    public UserCriteria(UserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.login = other.login == null ? null : other.login.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.mobile = other.mobile == null ? null : other.mobile.copy();
        this.birthday = other.birthday == null ? null : other.birthday.copy();
        this.activated = other.activated == null ? null : other.activated.copy();
        this.langKey = other.langKey == null ? null : other.langKey.copy();
        this.imageUrl = other.imageUrl == null ? null : other.imageUrl.copy();
        this.activationKey = other.activationKey == null ? null : other.activationKey.copy();
        this.resetKey = other.resetKey == null ? null : other.resetKey.copy();
        this.resetDate = other.resetDate == null ? null : other.resetDate.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
        this.departmentName = other.departmentName == null ? null : other.departmentName.copy();
        this.positionId = other.positionId == null ? null : other.positionId.copy();
        this.positionName = other.positionName == null ? null : other.positionName.copy();
        this.authoritiesId = other.authoritiesId == null ? null : other.authoritiesId.copy();
        this.authoritiesName = other.authoritiesName == null ? null : other.authoritiesName.copy();
    }

    @Override
    public UserCriteria copy() {
        return new UserCriteria(this);
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

    public StringFilter getLogin() {
        return login;
    }

    public StringFilter login() {
        if (login == null) {
            login = new StringFilter();
        }
        return login;
    }

    public void setLogin(StringFilter login) {
        this.login = login;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getMobile() {
        return mobile;
    }

    public StringFilter mobile() {
        if (mobile == null) {
            mobile = new StringFilter();
        }
        return mobile;
    }

    public void setMobile(StringFilter mobile) {
        this.mobile = mobile;
    }

    public ZonedDateTimeFilter getBirthday() {
        return birthday;
    }

    public ZonedDateTimeFilter birthday() {
        if (birthday == null) {
            birthday = new ZonedDateTimeFilter();
        }
        return birthday;
    }

    public void setBirthday(ZonedDateTimeFilter birthday) {
        this.birthday = birthday;
    }

    public BooleanFilter getActivated() {
        return activated;
    }

    public BooleanFilter activated() {
        if (activated == null) {
            activated = new BooleanFilter();
        }
        return activated;
    }

    public void setActivated(BooleanFilter activated) {
        this.activated = activated;
    }

    public StringFilter getLangKey() {
        return langKey;
    }

    public StringFilter langKey() {
        if (langKey == null) {
            langKey = new StringFilter();
        }
        return langKey;
    }

    public void setLangKey(StringFilter langKey) {
        this.langKey = langKey;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public StringFilter imageUrl() {
        if (imageUrl == null) {
            imageUrl = new StringFilter();
        }
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
    }

    public StringFilter getActivationKey() {
        return activationKey;
    }

    public StringFilter activationKey() {
        if (activationKey == null) {
            activationKey = new StringFilter();
        }
        return activationKey;
    }

    public void setActivationKey(StringFilter activationKey) {
        this.activationKey = activationKey;
    }

    public StringFilter getResetKey() {
        return resetKey;
    }

    public StringFilter resetKey() {
        if (resetKey == null) {
            resetKey = new StringFilter();
        }
        return resetKey;
    }

    public void setResetKey(StringFilter resetKey) {
        this.resetKey = resetKey;
    }

    public InstantFilter getResetDate() {
        return resetDate;
    }

    public InstantFilter resetDate() {
        if (resetDate == null) {
            resetDate = new InstantFilter();
        }
        return resetDate;
    }

    public void setResetDate(InstantFilter resetDate) {
        this.resetDate = resetDate;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public LongFilter departmentId() {
        if (departmentId == null) {
            departmentId = new LongFilter();
        }
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
    }

    public StringFilter getDepartmentName() {
        return departmentName;
    }

    public StringFilter departmentName() {
        if (departmentName == null) {
            departmentName = new StringFilter();
        }
        return departmentName;
    }

    public void setDepartmentName(StringFilter departmentName) {
        this.departmentName = departmentName;
    }

    public LongFilter getPositionId() {
        return positionId;
    }

    public LongFilter positionId() {
        if (positionId == null) {
            positionId = new LongFilter();
        }
        return positionId;
    }

    public void setPositionId(LongFilter positionId) {
        this.positionId = positionId;
    }

    public StringFilter getPositionName() {
        return positionName;
    }

    public StringFilter positionName() {
        if (positionName == null) {
            positionName = new StringFilter();
        }
        return positionName;
    }

    public void setPositionName(StringFilter positionName) {
        this.positionName = positionName;
    }

    public LongFilter getAuthoritiesId() {
        return authoritiesId;
    }

    public LongFilter authoritiesId() {
        if (authoritiesId == null) {
            authoritiesId = new LongFilter();
        }
        return authoritiesId;
    }

    public void setAuthoritiesId(LongFilter authoritiesId) {
        this.authoritiesId = authoritiesId;
    }

    public StringFilter getAuthoritiesName() {
        return authoritiesName;
    }

    public StringFilter authoritiesName() {
        if (authoritiesName == null) {
            authoritiesName = new StringFilter();
        }
        return authoritiesName;
    }

    public void setAuthoritiesName(StringFilter authoritiesName) {
        this.authoritiesName = authoritiesName;
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
        final UserCriteria that = (UserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(login, that.login) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(mobile, that.mobile) &&
            Objects.equals(birthday, that.birthday) &&
            Objects.equals(activated, that.activated) &&
            Objects.equals(langKey, that.langKey) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(activationKey, that.activationKey) &&
            Objects.equals(resetKey, that.resetKey) &&
            Objects.equals(resetDate, that.resetDate) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(departmentName, that.departmentName) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(positionName, that.positionName) &&
            Objects.equals(authoritiesId, that.authoritiesId) &&
            Objects.equals(authoritiesName, that.authoritiesName)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            login,
            firstName,
            lastName,
            email,
            mobile,
            birthday,
            activated,
            langKey,
            imageUrl,
            activationKey,
            resetKey,
            resetDate,
            departmentId,
            departmentName,
            positionId,
            positionName,
            authoritiesId,
            authoritiesName
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MyUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (login != null ? "login=" + login + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (mobile != null ? "mobile=" + mobile + ", " : "") +
                (birthday != null ? "birthday=" + birthday + ", " : "") +
                (activated != null ? "activated=" + activated + ", " : "") +
                (langKey != null ? "langKey=" + langKey + ", " : "") +
                (imageUrl != null ? "imageUrl=" + imageUrl + ", " : "") +
                (activationKey != null ? "activationKey=" + activationKey + ", " : "") +
                (resetKey != null ? "resetKey=" + resetKey + ", " : "") +
                (resetDate != null ? "resetDate=" + resetDate + ", " : "") +
                (departmentId != null ? "departmentId=" + departmentId + ", " : "") +
                (departmentName != null ? "departmentName=" + departmentName + ", " : "") +
                (positionId != null ? "positionId=" + positionId + ", " : "") +
                (positionName != null ? "positionName=" + positionName + ", " : "") +
                (authoritiesId != null ? "authoritiesId=" + authoritiesId + ", " : "") +
                (authoritiesName != null ? "authoritiesName=" + authoritiesName + ", " : "") +
            "}";
    }
}
