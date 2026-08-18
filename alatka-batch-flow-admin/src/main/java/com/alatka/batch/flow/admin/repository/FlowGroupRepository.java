package com.alatka.batch.flow.admin.repository;

import com.alatka.batch.flow.admin.entity.BatchFlowGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowGroupRepository extends JpaRepository<BatchFlowGroup, Long>, JpaSpecificationExecutor<BatchFlowGroup> {
}
