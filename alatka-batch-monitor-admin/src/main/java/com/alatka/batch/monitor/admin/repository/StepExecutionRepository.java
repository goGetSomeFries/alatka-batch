package com.alatka.batch.monitor.admin.repository;

import com.alatka.batch.monitor.admin.entity.StepExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StepExecutionRepository extends JpaRepository<StepExecution, Long>, JpaSpecificationExecutor<StepExecution> {
}
