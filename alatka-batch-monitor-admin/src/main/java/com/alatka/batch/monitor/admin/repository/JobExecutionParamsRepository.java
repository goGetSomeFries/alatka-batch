package com.alatka.batch.monitor.admin.repository;

import com.alatka.batch.monitor.admin.entity.JobExecutionParams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JobExecutionParamsRepository extends JpaRepository<JobExecutionParams, Long>, JpaSpecificationExecutor<JobExecutionParams> {
}
