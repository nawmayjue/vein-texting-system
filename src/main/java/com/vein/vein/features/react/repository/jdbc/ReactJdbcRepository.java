package com.vein.vein.features.react.repository.jdbc;

import com.vein.vein.features.react.dto.ReactResponse;
import com.vein.vein.shared.data.model.React;

import java.util.List;

public interface ReactJdbcRepository {
    List<ReactResponse> findAll();
    ReactResponse findById(Long id);
    void updateReactBlogId(Long blogId, Long id);

}
