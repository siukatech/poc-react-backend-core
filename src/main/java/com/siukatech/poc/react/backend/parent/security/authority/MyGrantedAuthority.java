package com.siukatech.poc.react.backend.parent.security.authority;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@Builder
public class MyGrantedAuthority implements GrantedAuthority {

    private String userRoleMid;
    private String appMid;
    private String resourceMid;
    private String accessRight;

    @Override
    public String getAuthority() {
        return userRoleMid + ":" + resourceMid + ":" + accessRight;
    }

}
