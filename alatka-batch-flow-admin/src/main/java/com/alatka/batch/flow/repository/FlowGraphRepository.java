package com.alatka.batch.flow.repository;

import com.alatka.batch.flow.entity.BatchFlowGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowGraphRepository extends JpaRepository<BatchFlowGraph, Long>, JpaSpecificationExecutor<BatchFlowGraph> {

    /**
     * 绕过 @LastModifiedDate
     *
     * @param previousId
     * @param id
     */
    @Modifying
    @Query("UPDATE BatchFlowGraph SET previousId = :previousId WHERE previousId = :id")
    void updatePreviousId(Long previousId, Long id);
}
