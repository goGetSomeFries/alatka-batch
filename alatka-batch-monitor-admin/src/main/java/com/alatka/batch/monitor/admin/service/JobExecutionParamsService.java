package com.alatka.batch.monitor.admin.service;

import com.alatka.batch.monitor.admin.entity.JobExecutionParams;
import com.alatka.batch.monitor.admin.model.JobExecutionParamsRes;
import com.alatka.batch.monitor.admin.repository.JobExecutionParamsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class JobExecutionParamsService {

    private JobExecutionParamsRepository jobExecutionParamsRepository;

    public List<JobExecutionParamsRes> queryList(Long jobExecutionId) {
        JobExecutionParams condition = new JobExecutionParams();
        condition.setJobExecutionId(jobExecutionId);

        return this.jobExecutionParamsRepository.findAll(Example.of(condition)).stream()
                .sorted(Comparator.comparing(JobExecutionParams::getParameterName))
                .map(entity -> {
                    JobExecutionParamsRes res = new JobExecutionParamsRes();
                    BeanUtils.copyProperties(entity, res);
                    return res;
                }).collect(Collectors.toList());
    }

    @Autowired
    public void setJobExecutionParamsRepository(JobExecutionParamsRepository jobExecutionParamsRepository) {
        this.jobExecutionParamsRepository = jobExecutionParamsRepository;
    }
}
