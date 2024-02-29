package com.siukatech.poc.react.backend.parent.data.listener;


import com.siukatech.poc.react.backend.parent.data.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Component
public class AbstractEntityToPersistListener {

    @PrePersist
    protected void onSavePrePersist(final AbstractEntity abstractEntity) {
        log.debug("onSavePrePersist - abstractEntity.getId: [" + abstractEntity.getId()
                + "], abstractEntity.getVersionNo: [" + abstractEntity.getVersionNo()
                + "]");
        if (abstractEntity.getId() != null && abstractEntity.getVersionNo() == null) {
            // since the version is null, it treats "CREATE"
            String message = "VersionNo cannot null be null if abstractEntity.id#" + abstractEntity.getId() + " exists, onSavePrePersist";
            throw new IllegalArgumentException(message);
        }
    }

    @PostPersist
    protected void onSavePostPersist(final AbstractEntity abstractEntity) {
        log.debug("onSavePostPersist - abstractEntity.getId: [" + abstractEntity.getId()
                + "], abstractEntity.getVersionNo: [" + abstractEntity.getVersionNo()
                + "]");
    }

    @PreUpdate
    protected void onSavePreUpdate(final AbstractEntity abstractEntity) {
        log.debug("onSavePreUpdate - abstractEntity.getId: [" + abstractEntity.getId()
                + "], abstractEntity.getVersionNo: [" + abstractEntity.getVersionNo()
                + "]");
        if (abstractEntity.getVersionNo() == null) {
            throw new IllegalArgumentException("VersionNo cannot null be null for " + abstractEntity.getClass().getName() + ".id#" + abstractEntity.getId() + ", onSavePreUpdate");
        }
    }

    @PostUpdate
    protected void onSavePostUpdate(final AbstractEntity abstractEntity) {
        log.debug("onSavePostUpdate - abstractEntity.getId: [" + abstractEntity.getId()
                + "], abstractEntity.getVersionNo: [" + abstractEntity.getVersionNo()
                + "]");
    }

    @PreRemove
    protected void onSavePreRemove(final AbstractEntity abstractEntity) {
        log.debug("onSavePreRemove - abstractEntity.getId: [" + abstractEntity.getId()
                + "], abstractEntity.getVersionNo: [" + abstractEntity.getVersionNo()
                + "]");
    }

    @PostRemove
    protected void onSavePostRemove(final AbstractEntity abstractEntity) {
        log.debug("onSavePostRemove - abstractEntity.getId: [" + abstractEntity.getId()
                + "], abstractEntity.getVersionNo: [" + abstractEntity.getVersionNo()
                + "]");
    }

    @PostLoad
    protected void onGetPostLoad(final AbstractEntity abstractEntity) {
        log.debug("onGetPostLoad - abstractEntity.getId: [" + abstractEntity.getId()
                + "], abstractEntity.getVersionNo: [" + abstractEntity.getVersionNo()
                + "]");
    }

}
