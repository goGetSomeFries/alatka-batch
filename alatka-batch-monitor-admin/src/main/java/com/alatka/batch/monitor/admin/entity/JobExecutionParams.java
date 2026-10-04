package com.alatka.batch.monitor.admin.entity;


import org.springframework.batch.core.JobParameter;

import javax.persistence.*;
import java.io.Serializable;

@IdClass(JobExecutionParams.class)
@Entity
@Table(name = "BATCH_JOB_EXECUTION_PARAMS")
public class JobExecutionParams implements Serializable {

    @Id
    private Long jobExecutionId;
    @Id
    @Column(name = "KEY_NAME")
    private String parameterName;
    @Column(name = "TYPE_CD")
    private String parameterType;
    private String stringVal;
    private String dateVal;
    private String longVal;
    private String doubleVal;
    private String identifying;

    public Long getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(Long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getStringVal() {
        return stringVal;
    }

    public void setStringVal(String stringVal) {
        this.stringVal = stringVal;
    }

    public String getDateVal() {
        return dateVal;
    }

    public void setDateVal(String dateVal) {
        this.dateVal = dateVal;
    }

    public String getLongVal() {
        return longVal;
    }

    public void setLongVal(String longVal) {
        this.longVal = longVal;
    }

    public String getDoubleVal() {
        return doubleVal;
    }

    public void setDoubleVal(String doubleVal) {
        this.doubleVal = doubleVal;
    }

    public String getIdentifying() {
        return identifying;
    }

    public void setIdentifying(String identifying) {
        this.identifying = identifying;
    }

    public String getParameterValue() {
        switch (JobParameter.ParameterType.valueOf(parameterType)) {
            case STRING:
                return stringVal;
            case DATE:
                return dateVal;
            case LONG:
                return longVal;
            case DOUBLE:
                return doubleVal;
            default:
                throw new IllegalArgumentException("can not be here");
        }
    }

    public void setParameterValue(String parameterValue) {
        if (parameterType == null) {
            this.stringVal = parameterValue;
        } else {
            switch (JobParameter.ParameterType.valueOf(parameterType)) {
                case STRING:
                    this.stringVal = parameterValue;
                    break;
                case DATE:
                    this.dateVal = parameterValue;
                    break;
                case LONG:
                    this.longVal = parameterValue;
                    break;
                case DOUBLE:
                    this.doubleVal = parameterValue;
                    break;
                default:
                    throw new IllegalArgumentException("can not be here");
            }
        }
    }
}
