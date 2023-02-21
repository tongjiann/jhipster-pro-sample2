package com.mycompany.myapp.system.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.diboot.core.mapper.BaseCrudMapper;
import com.mycompany.myapp.system.domain.Announcement;
import java.util.*;
import org.apache.ibatis.annotations.Param;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data SQL repository for the Announcement entity.
 */
@SuppressWarnings("unused")
public interface AnnouncementRepository extends BaseCrudMapper<Announcement> {
    default List<Announcement> findAll() {
        return this.selectList(null);
    }

    default Optional<Announcement> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove
}
