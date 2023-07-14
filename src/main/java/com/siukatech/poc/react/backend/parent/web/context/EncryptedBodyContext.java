package com.siukatech.poc.react.backend.parent.web.context;

import com.siukatech.poc.react.backend.parent.business.dto.UserDto;
import com.siukatech.poc.react.backend.parent.data.entity.UserEntity;
import com.siukatech.poc.react.backend.parent.web.model.EncryptedDetail;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Reference:
 * https://shzhangji.com/blog/2022/07/05/store-custom-data-in-spring-mvc-request-context/
 *
 */
@Data
@Component
@RequestScope
public class EncryptedBodyContext {
//    private UserEntity userEntity;
    private UserDto userDto;
    private EncryptedDetail encryptedDetail;
}
