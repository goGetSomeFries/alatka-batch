package com.alatka.batch.param.repository;

import com.alatka.batch.param.entity.BatchParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ParamRepository extends JpaRepository<BatchParam, Long>, JpaSpecificationExecutor<BatchParam> {
}
