package com.mycompany.myapp.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.diboot.core.mapper.BaseCrudMapper;
import com.mycompany.myapp.domain.UReportFile;
import java.util.*;
import org.apache.ibatis.annotations.Param;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data SQL repository for the UReportFile entity.
 */
@SuppressWarnings("unused")
public interface UReportFileRepository extends BaseCrudMapper<UReportFile> {
    default List<UReportFile> findAll() {
        return this.selectList(null);
    }

    default Optional<UReportFile> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    default Boolean existsByName(String name) {
        return this.selectCount(new LambdaQueryWrapper<UReportFile>().eq(UReportFile::getName, name)) > 0;
    }

    default Optional<UReportFile> getByName(String name) {
        return Optional.ofNullable(this.selectOne(new LambdaQueryWrapper<UReportFile>().eq(UReportFile::getName, name)));
    }

    default void deleteByName(String name) {
        this.delete(new LambdaQueryWrapper<UReportFile>().eq(UReportFile::getName, name));
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove
}
