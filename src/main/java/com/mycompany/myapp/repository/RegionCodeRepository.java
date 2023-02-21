package com.mycompany.myapp.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.diboot.core.mapper.BaseCrudMapper;
import com.mycompany.myapp.domain.RegionCode;
import java.util.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data SQL repository for the RegionCode entity.
 */
@SuppressWarnings("unused")
public interface RegionCodeRepository extends BaseCrudMapper<RegionCode> {
    default List<RegionCode> findAll() {
        return this.selectList(null);
    }

    default Optional<RegionCode> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    default IPage<RegionCode> findAllByParentIsNull(IPage<RegionCode> pageable) {
        return this.selectPage(pageable, new QueryWrapper<RegionCode>().isNull("parent_id"));
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove
}
