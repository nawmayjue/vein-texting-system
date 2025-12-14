package com.vein.vein.features.react.service;

import com.vein.vein.features.react.dto.CreateReactRequest;
import com.vein.vein.features.react.dto.ReactResponse;
import com.vein.vein.features.react.dto.UpdateReactRequest;

import java.util.List;

public interface ReactService {
    ReactResponse createReact(CreateReactRequest createReactRequest, String username);
    List<ReactResponse> retrieveAllReact();
    ReactResponse retrieveReactById(Long id);
    void deleteReactById(Long id);
    void updateBlogId(UpdateReactRequest updateReactRequest, Long id);
}
