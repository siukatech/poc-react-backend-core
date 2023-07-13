package com.siukatech.poc.react.backend.parent.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenReq(@JsonProperty("client_id") String clientId
        , @JsonProperty("client_secret") String clientSecret
        , @JsonProperty("redirect_uri") String redirectUri
        , @JsonProperty("grant_type") String grantType
        , String code
) {
}
