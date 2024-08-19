package com.siukatech.poc.react.backend.parent.security.evaluator;

import com.siukatech.poc.react.backend.parent.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.parent.security.authentication.MyAuthenticationToken;
import com.siukatech.poc.react.backend.parent.security.authority.MyGrantedAuthority;
import com.siukatech.poc.react.backend.parent.security.exception.NoSuchPermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PermissionControlEvaluator {
    public boolean evaluate(HandlerMethod handlerMethod, Authentication authentication) throws NoSuchPermissionException {
        String loginId = authentication.getName();
        Class<?> beanType = handlerMethod.getBeanType();
        Method method = handlerMethod.getMethod();
        String beanName = beanType.getName();
        String methodName = method.getName();
        List<Annotation> methodAnnotationList = List.of(method.getAnnotations());
        PermissionControl permissionControlAnnotationByUtil = AnnotationUtils.findAnnotation(
                method, PermissionControl.class);
        PermissionControl permissionControlAnnotationByMethod = handlerMethod.getMethodAnnotation(PermissionControl.class);
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        String resourceMid = (permissionControlAnnotationByUtil == null ? null : permissionControlAnnotationByUtil.resourceMid());
        String accessRight = (permissionControlAnnotationByUtil == null ? null : permissionControlAnnotationByUtil.accessRight());
        long authorityCount = -1;

        log.debug("evaluate - loginId: [{}], beanName: [{}], methodName: [{}]"
                , loginId, beanName, methodName);
        log.debug("evaluate - permissionControlAnnotationByUtil: [{}], permissionControlAnnotationByMethod: [{}]"
                , permissionControlAnnotationByUtil, permissionControlAnnotationByMethod);
        if (authentication instanceof MyAuthenticationToken myAuthenticationToken) {
            grantedAuthorityList.addAll(myAuthenticationToken.getAuthorities());
            authorityCount = grantedAuthorityList.stream()
                    .map(MyGrantedAuthority.class::cast)
                    .peek(mga -> {
                        log.debug("preHandle - permissionControlAnnotationByUtil: [{}]"
                                        + ", resourceMid: [{}], accessRight: [{}]"
                                        + ", mga.getAppMid: [{}], mga.getUserRoleMid: [{}]"
                                        + ", mga.getResourceMid: [], mga.getAccessRight: [{}]"
                                        + ", mga.getAuthority: [{}]"
                                , permissionControlAnnotationByUtil
                                , resourceMid, accessRight
                                , mga.getAppMid(), mga.getUserRoleMid()
                                , mga.getResourceMid(), mga.getAccessRight()
                                , mga.getAuthority()
                        );
                    })
                    .filter(mga -> mga.getResourceMid() != null
                            && mga.getResourceMid().equals(resourceMid)
                            && mga.getAccessRight() != null
                            && mga.getAccessRight().equals(accessRight)
                    )
                    .count();
            log.debug("evaluate - loginId: [{}], permissionControlAnnotationByUtil: [{}], resourceMid: [{}], accessRight: [{}], authorityCount: [{}]"
                    , loginId, permissionControlAnnotationByUtil, resourceMid, accessRight, authorityCount
            );
        }
        if (authorityCount <= 0) {
            String accessDeniedTmpl = "Access denied"
                    + ", myAuthenticationToken.getAuthorities.size: [%s]"
                    + ", loginId: [%s], beanName: [%s], methodName: [%s]"
                    + ", permissionControlAnnotationByUtil: [%s]"
                    + ", resourceMid: [%s], accessRight: [%s]"
                    + ", authorityCount: [%d]";
            String accessDeniedMsg = String.format(accessDeniedTmpl
                    , grantedAuthorityList.size()
                    , loginId, beanName, methodName
                    , permissionControlAnnotationByUtil == null ? "NULL" : permissionControlAnnotationByUtil.toString()
                    , resourceMid, accessRight
                    , authorityCount);
            throw new NoSuchPermissionException(accessDeniedMsg);
        }
        return true;
    }
}
