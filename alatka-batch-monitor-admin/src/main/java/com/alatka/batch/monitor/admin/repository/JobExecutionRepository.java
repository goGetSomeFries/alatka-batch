package com.alatka.batch.monitor.admin.repository;

import com.alatka.batch.monitor.admin.entity.JobExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JobExecutionRepository extends JpaRepository<JobExecution, Long>, JpaSpecificationExecutor<JobExecution> {
}
