package com.siukatech.poc.react.backend.parent.business.form.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthReq(@JsonProperty("response_type") String responseType
        , @JsonProperty("client_id") String clientId
        , @JsonProperty("scope") String scope
        , @JsonProperty("redirect_uri") String redirectUri
) {
}
