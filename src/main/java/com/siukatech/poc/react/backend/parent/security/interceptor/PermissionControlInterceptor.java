package com.siukatech.poc.react.backend.parent.security.interceptor;

import com.siukatech.poc.react.backend.parent.security.authentication.MyAuthenticationToken;
import com.siukatech.poc.react.backend.parent.security.authority.MyGrantedAuthority;
import com.siukatech.poc.react.backend.parent.security.evaluator.PermissionControlEvaluator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Reference:
 * https://medium.com/@aedemirsen/what-is-spring-boot-request-interceptor-and-how-to-use-it-7fd85f3df7f7
 */
@Slf4j
@Component
public class PermissionControlInterceptor implements HandlerInterceptor {

    private final PermissionControlEvaluator permissionControlEvaluator;

    public PermissionControlInterceptor(PermissionControlEvaluator permissionControlEvaluator) {
        this.permissionControlEvaluator = permissionControlEvaluator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response
            , Object handler) throws Exception {
        log.debug("preHandle - start");
        log.debug("preHandle - request.getRequestURI: [${}], handler: [{}]", request.getRequestURI(), handler);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof MyAuthenticationToken myAuthenticationToken) {
//            myAuthenticationToken.getName();
            if (myAuthenticationToken.getAuthorities() instanceof MyGrantedAuthority myGrantedAuthority) {
                myGrantedAuthority.getAuthority();
                log.debug("preHandle - authentication.name: [{}], myGrantedAuthority.getAuthority: [{}]"
                        , authentication == null ? "" : authentication.getName()
                        , myGrantedAuthority.getAuthority()
                );
            }
        }
        log.debug("preHandle - authentication.name: [{}]"
                , authentication == null ? "NULL" : authentication.getName()
        );

        boolean result = false;
        if (handler instanceof HandlerMethod handlerMethod) {
            result = this.permissionControlEvaluator.evaluate(handlerMethod, authentication);
        }

        log.debug("preHandle - end, result: [{}]", result);
        return result;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response
            , Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        log.debug("postHandle - start");

        log.debug("postHandle - end");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response
            , Object handler, @Nullable Exception ex) throws Exception {
        log.debug("postHandle - start");

        log.debug("postHandle - end");
    }

}
