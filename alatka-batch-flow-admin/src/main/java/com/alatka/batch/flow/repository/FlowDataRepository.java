package com.alatka.batch.flow.repository;

import com.alatka.batch.flow.entity.BatchFlowData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowDataRepository extends JpaRepository<BatchFlowData, Long>, JpaSpecificationExecutor<BatchFlowData> {
}
