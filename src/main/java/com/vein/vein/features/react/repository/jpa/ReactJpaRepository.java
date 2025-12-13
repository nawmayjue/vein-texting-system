package com.vein.vein.features.react.repository.jpa;

import com.vein.vein.shared.data.model.React;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactJpaRepository extends JpaRepository<React, Long> {
}
