package com.siukatech.poc.react.backend.core.security.evaluator;

import com.siukatech.poc.react.backend.core.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.core.security.authentication.MyAuthenticationToken;
import com.siukatech.poc.react.backend.core.security.authority.MyGrantedAuthority;
import com.siukatech.poc.react.backend.core.security.exception.NoSuchPermissionException;
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
//        List<Annotation> beanTypeAnnotationLv1List = List.of(beanType.getAnnotations());
////        beanTypeAnnotationLv1List.stream().forEach(bta -> {
////            Annotation[] btaAnnotationList = bta.annotationType().getAnnotations();
////            log.debug("evaluate - bta.annotationType().getName: [{}], btaAnnotationList: [{}]"
////                    , bta.annotationType().getName(), btaAnnotationList);
////        });
//        List<Annotation> beanTypeAnnotationLv2List = List.of(beanType.getAnnotations()).stream()
//                .map(bta -> List.of(bta.annotationType().getAnnotations()))
//                .flatMap(list -> list.stream())
//                .collect(Collectors.toList())
//                ;
//        List<Annotation> beanTypeAnnotationAllList = new ArrayList<>();
//        beanTypeAnnotationAllList.addAll(beanTypeAnnotationLv1List);
//        beanTypeAnnotationAllList.addAll(beanTypeAnnotationLv2List);
//        log.debug("evaluate - beanTypeAnnotationLv1List: [{}], beanTypeAnnotationLv2List: [{}]"
//                , beanTypeAnnotationLv1List, beanTypeAnnotationLv2List);
////        boolean hasPublicController = beanTypeAnnotationAllList.stream().filter(a -> a.getClass().equals(PublicController.class)).count() > 0;
//        PublicController publicController = AnnotationUtils.findAnnotation(beanType, PublicController.class);
//        log.debug("evaluate - methodAnnotationList: [{}], publicController: [{}]"
//                , methodAnnotationList, publicController);

        PermissionControl permissionControlAnnotationByUtil = AnnotationUtils.findAnnotation(
                method, PermissionControl.class);
        PermissionControl permissionControlAnnotationByMethod = handlerMethod.getMethodAnnotation(PermissionControl.class);
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        String appResourceId = (permissionControlAnnotationByUtil == null ? null : permissionControlAnnotationByUtil.appResourceId());
        String accessRight = (permissionControlAnnotationByUtil == null ? null : permissionControlAnnotationByUtil.accessRight());
        long authorityCount = -1;
//        boolean isPublic = publicController != null;

        log.debug("evaluate - loginId: [{}], beanName: [{}], methodName: [{}]"
//                        + ", hasPublicController: [{}]"
                , loginId, beanName, methodName
//                , isPublic
        );
        log.debug("evaluate - permissionControlAnnotationByUtil: [{}], permissionControlAnnotationByMethod: [{}]"
                , permissionControlAnnotationByUtil, permissionControlAnnotationByMethod);
//        if (isPublic) {
//            // nothing to do with PublicController
//        }
//        else {
            if (authentication instanceof MyAuthenticationToken myAuthenticationToken) {
                grantedAuthorityList.addAll(myAuthenticationToken.getAuthorities());
                authorityCount = grantedAuthorityList.stream()
                        .filter(grantedAuthority -> grantedAuthority instanceof MyGrantedAuthority)
                        .map(MyGrantedAuthority.class::cast)
                        .peek(mga -> {
                            log.debug("preHandle - permissionControlAnnotationByUtil: [{}]"
                                            + ", appResourceId: [{}], accessRight: [{}]"
                                            + ", mga.getApplicationId: [{}], mga.getUserRoleId: [{}]"
                                            + ", mga.getAppResourceId: [], mga.getAccessRight: [{}]"
                                            + ", mga.getAuthority: [{}]"
                                    , permissionControlAnnotationByUtil
                                    , appResourceId, accessRight
                                    , mga.getApplicationId(), mga.getUserRoleId()
                                    , mga.getAppResourceId(), mga.getAccessRight()
                                    , mga.getAuthority()
                            );
                        })
                        .filter(mga -> mga.getAppResourceId() != null
                                && mga.getAppResourceId().equals(appResourceId)
                                && mga.getAccessRight() != null
                                && mga.getAccessRight().equals(accessRight)
                        )
                        .count();
                log.debug("evaluate - loginId: [{}], permissionControlAnnotationByUtil: [{}], appResourceId: [{}], accessRight: [{}], authorityCount: [{}]"
                        , loginId, permissionControlAnnotationByUtil, appResourceId, accessRight, authorityCount
                );
            }
            if (authorityCount <= 0) {
                String accessDeniedTmpl = "Access denied"
                        + ", myAuthenticationToken.getAuthorities.size: [%s]"
                        + ", myAuthenticationToken.getAuthorities.MyGrantedAuthority.count: [%s]"
                        + ", loginId: [%s], beanName: [%s], methodName: [%s]"
                        + ", permissionControlAnnotationByUtil: [%s]"
                        + ", appResourceId: [%s], accessRight: [%s]"
                        + ", authorityCount: [%d]";
                String accessDeniedMsg = String.format(accessDeniedTmpl
                        , grantedAuthorityList.size()
                        , grantedAuthorityList.stream().filter(ga -> ga instanceof MyGrantedAuthority).count()
                        , loginId, beanName, methodName
                        , permissionControlAnnotationByUtil == null ? "NULL" : permissionControlAnnotationByUtil.toString()
                        , appResourceId, accessRight
                        , authorityCount);
                throw new NoSuchPermissionException(accessDeniedMsg);
            }
//        }
        return true;
    }
}
