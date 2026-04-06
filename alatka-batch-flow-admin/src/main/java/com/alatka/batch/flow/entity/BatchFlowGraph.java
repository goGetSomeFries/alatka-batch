package com.alatka.batch.flow.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ALK_BATCH_FLOW_GRAPH")
@EntityListeners(AuditingEntityListener.class)
public class BatchFlowGraph {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "D_ID")
    private Long id;

    @CreatedDate
    @Column(name = "D_CREATE_AT", updatable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "D_UPDATE_AT", insertable = false)
    private LocalDateTime updateAt;

    @Column(name = "D_ORIGIN_DATA", columnDefinition = "BLOB")
    private byte[] originData;

    @Column(name = "D_DEPLOY_DATA", columnDefinition = "BLOB")
    private byte[] deployData;

    @Column(name = "D_PREVIOUS_ID")
    private Long previousId;

    @Column(name = "D_STATUS")
    private String status;

    @Column(name = "D_CURRENT")
    private Boolean current;

    @Column(name = "F_ID")
    private Long flowId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public byte[] getOriginData() {
        return originData;
    }

    public void setOriginData(byte[] originData) {
        this.originData = originData;
    }

    public byte[] getDeployData() {
        return deployData;
    }

    public void setDeployData(byte[] deployData) {
        this.deployData = deployData;
    }

    public Long getPreviousId() {
        return previousId;
    }

    public void setPreviousId(Long previousId) {
        this.previousId = previousId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

}
