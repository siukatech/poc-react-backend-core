package com.siukatech.poc.react.backend.parent.web.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@MappedSuperclass
public abstract class AbstractForm {
    @NotNull(message = "Version No cannot be NULL")
    protected Long versionNo;
}
