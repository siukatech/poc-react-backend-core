package com.siukatech.poc.react.backend.core.web.advice.mapper;

import com.siukatech.poc.react.backend.core.business.mapper.AbstractMapper;
import com.siukatech.poc.react.backend.core.web.advice.model.ProblemDetailExt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.http.ProblemDetail;

@Mapper
public interface ProblemDetailExtMapper extends AbstractMapper {

    @Mapping(target = "correlationId", ignore = true)
    ProblemDetailExt convertProblemDetailToExtWithoutCorrelationId(ProblemDetail problemDetail);
}
