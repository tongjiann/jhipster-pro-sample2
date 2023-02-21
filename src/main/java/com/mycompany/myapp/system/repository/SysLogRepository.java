package com.mycompany.myapp.system.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.diboot.core.mapper.BaseCrudMapper;
import com.mycompany.myapp.system.domain.SysLog;
import java.util.*;
import org.apache.ibatis.annotations.Param;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data SQL repository for the SysLog entity.
 */
@SuppressWarnings("unused")
public interface SysLogRepository extends BaseCrudMapper<SysLog> {
    default List<SysLog> findAll() {
        return this.selectList(null);
    }

    default Optional<SysLog> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove
}
