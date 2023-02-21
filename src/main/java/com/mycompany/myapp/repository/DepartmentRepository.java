package com.mycompany.myapp.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.diboot.core.mapper.BaseCrudMapper;
import com.mycompany.myapp.domain.Department;
import java.util.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data SQL repository for the Department entity.
 */
public interface DepartmentRepository extends BaseCrudMapper<Department> {
    default List<Department> findAll() {
        return this.selectList(null);
    }

    default Optional<Department> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    default IPage<Department> findAllByParentIsNull(IPage<Department> pageable) {
        return this.selectPage(pageable, new QueryWrapper<Department>().isNull("parent_id"));
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove
}
