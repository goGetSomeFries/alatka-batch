package com.alatka.batch.flow.repository;

import com.alatka.batch.flow.entity.BatchFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowRepository extends JpaRepository<BatchFlow, Long>, JpaSpecificationExecutor<BatchFlow> {
}
