package com.siukatech.poc.react.backend.parent.data.listener;


import com.siukatech.poc.react.backend.parent.data.entity.AbstractEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Component
public class AbstractEntityToPersistListener {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PrePersist
    protected void onSavePrePersist(final AbstractEntity abstractEntity) {
        logger.debug("onSavePrePersist - abstractEntity.getId: [" + abstractEntity.getId()
                + "], abstractEntity.getVersionNo: [" + abstractEntity.getVersionNo()
                + "]");
        if (abstractEntity.getId() != null && abstractEntity.getVersionNo() == null) {
            // since the version is null, it treats "CREATE"
            String message = "VersionNo cannot null be null if abstractEntity.id#" + abstractEntity.getId() + " exists, onSavePrePersist";
            throw new IllegalArgumentException(message);
        }
    }

    @PreUpdate
    protected void onSavePreUpdate(final AbstractEntity abstractEntity) {
        logger.debug("onSavePreUpdate - abstractEntity.getId: [" + abstractEntity.getId()
                + "], abstractEntity.getVersionNo: [" + abstractEntity.getVersionNo()
                + "]");
        if (abstractEntity.getVersionNo() == null) {
            throw new IllegalArgumentException("VersionNo cannot null be null for " + abstractEntity.getClass().getName() + ".id#" + abstractEntity.getId() + ", onSavePreUpdate");
        }
    }

}
