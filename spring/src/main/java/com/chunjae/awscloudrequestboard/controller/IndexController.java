package com.chunjae.awscloudrequestboard.controller;

import com.chunjae.awscloudrequestboard.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.UnknownHostException;

import static com.chunjae.awscloudrequestboard.util.ClientIpUtil.getClientIp;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserService userService;

    @GetMapping({"/index", "/"})
    public String main(
            Model model
            , HttpServletRequest request
            , HttpSession session) throws UnknownHostException {
        // 클라이언트의 IP 주소를 가져오기
        String clientIp = getClientIp(request);

        // 로그에 IP 주소 출력
        log.info("접속한 사용자 IP: {}", clientIp);

        // 세션에 IP 주소 저장
        session.setAttribute("clientIp", clientIp);

        boolean existIp = userService.findByIp(clientIp);
        log.info("clientIP......indexController.....{}", existIp);

        // 관리자 여부 확인
        if (existIp){
            String clientRole = userService.findUserByIp(clientIp).getRole();
            session.setAttribute("clientRole", clientRole);
        }

        model.addAttribute("existIp", existIp);
        model.addAttribute("view", "main/main");
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("view","admin/dashboard");
        return "index";
    }

}
