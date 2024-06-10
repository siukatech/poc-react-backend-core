package com.siukatech.poc.react.backend.parent.business.form;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@MappedSuperclass
// https://stackoverflow.com/a/5455563
//@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractForm<T> {

    protected T id;

    @NotNull(message = "Version No cannot be NULL")
    protected Long versionNo;
}
