package com.alatka.batch.flow.repository;

import com.alatka.batch.flow.entity.BatchFlowGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowGraphRepository extends JpaRepository<BatchFlowGraph, Long>, JpaSpecificationExecutor<BatchFlowGraph> {
}
