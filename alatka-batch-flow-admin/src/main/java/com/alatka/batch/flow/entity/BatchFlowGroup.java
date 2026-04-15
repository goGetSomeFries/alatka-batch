package com.alatka.batch.flow.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "ALK_BATCH_FLOW_GROUP")
@EntityListeners(AuditingEntityListener.class)
public class BatchFlowGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "G_ID")
    private Long id;

    @CreatedDate
    @Column(name = "G_CREATE_AT", updatable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "G_UPDATE_AT", insertable = false)
    private LocalDateTime updateAt;

    @Column(name = "G_KEY")
    private String key;

    @Column(name = "G_NAME")
    private String name;

    @Column(name = "G_ENABLED")
    private Boolean enabled;

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
