package com.chunjae.awscloudrequestboard.interceptor;


import com.chunjae.awscloudrequestboard.dto.UserDTO;
import com.chunjae.awscloudrequestboard.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.chunjae.awscloudrequestboard.util.ClientIpUtil.getClientIp;

@Slf4j
@Component
@RequiredArgsConstructor
public class IpCheckInterceptor implements HandlerInterceptor {
    // 사용자의 IP를 확인하고, 특정 IP가 허용되지 않은 경우 해당 사용자를 메인 페이지로 리다이렉트
    // HandlerInterceptor: Spring의 인터셉터를 구현한 클래스로, 특정 요청 전에 검증이나 처리를 하기 위해 사용

    private final UserService userService;

    @Override
    public boolean preHandle(
            HttpServletRequest request
            , HttpServletResponse response
            , Object handler) throws Exception {
        // 요청이 처리되기 전에 실행되는 메서드
        // true를 반환하면 요청을 계속 진행하고, false를 반환하면 요청을 중단

        HttpSession session = request.getSession();
        String clientIp = (String) session.getAttribute("clientIp");
        String clientRole = (String) session.getAttribute("clientRole");

        // 현재 요청 경로
        String requestURI = request.getRequestURI();

        // 세션에 clientIp 값이 없으면 IP 가져오기
        if (clientIp == null) {
            clientIp = getClientIp(request);
            session.setAttribute("clientIp", clientIp);
        }

        // IP가 허용된 IP인지 확인
        boolean existIp = userService.findByIp(clientIp);
        if (!existIp) {
            log.warn("허용되지 않은 IP: {}", clientIp);

            // 현재 요청이 이미 / 또는 /index인 경우에는 다시 리다이렉트하지 않음
            if (!requestURI.equals("/") && !requestURI.equals("/index")) {
                response.sendRedirect("/"); // 허용되지 않은 사용자라면 메인 페이지로 리다이렉트
            }
            return false;  // 요청을 더 이상 진행하지 않음
        }

        // 세션에 clientRole 값이 없으면 Role 가져오기
        if (clientRole == null) {
            UserDTO user = userService.findUserByIp(clientIp);
            if (user == null) {
                log.warn("해당 IP에 대한 유저 정보가 없습니다: {}", clientIp);
                response.sendRedirect("/"); // 유저 정보가 없는 경우 메인 페이지로 리다이렉트
                return false;
            }
            clientRole = user.getRole();
            session.setAttribute("clientRole", clientRole);
        }

        // 관리자 페이지에 접근 시 관리자 권한 확인
        if (requestURI.equals("/dashboard") && !"admin".equals(clientRole)) {
            log.warn("관리자 권한이 없습니다. Role: {}", clientRole);
            response.sendRedirect("/"); // 메인 페이지로 리다이렉트
            return false;  // 요청을 더 이상 진행하지 않음
        }

        return true; // 허용된 IP와 권한이 있는 경우 요청 계속 진행
    }


}