package com.mycompany.myapp.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.diboot.core.mapper.BaseCrudMapper;
import com.mycompany.myapp.domain.UploadImage;
import com.mycompany.myapp.security.SecurityUtils;
import java.util.*;
import org.apache.ibatis.annotations.Param;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data SQL repository for the UploadImage entity.
 */
@SuppressWarnings("unused")
public interface UploadImageRepository extends BaseCrudMapper<UploadImage> {
    default List<UploadImage> findAll() {
        return this.selectList(null);
    }

    default Optional<UploadImage> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    default List<UploadImage> findByUserIsCurrentUser() {
        List<UploadImage> result = new ArrayList<>();
        SecurityUtils
            .getCurrentUserId()
            .ifPresent(userid -> result.addAll(this.selectList(new QueryWrapper<UploadImage>().eq("user_id", userid))));
        return result;
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove
}
