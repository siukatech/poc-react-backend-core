package com.siukatech.poc.react.backend.parent.business.form;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@MappedSuperclass
// https://stackoverflow.com/a/5455563
//@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractForm {
    @NotNull(message = "Version No cannot be NULL")
    protected Long versionNo;
}
