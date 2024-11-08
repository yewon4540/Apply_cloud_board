package com.chunjae.awscloudrequestboard.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
//public class ClientIpUtil {
//    // 클라이언트 IP를 가져오는 메서드
//    public static String getClientIp(HttpServletRequest request) throws UnknownHostException {
//
//        // X-Forwarded-For: 프록시나 로드 밸런서를 통과한 원본 클라이언트의 IP를 저장하는 헤더
//        String ip = request.getHeader("X-Forwarded-For");
//
//        // Proxy-Client-IP 및 WL-Proxy-Client-IP: 각각 프록시 서버나 웹로직 서버에 의해 설정된 클라이언트 IP
//        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//
//        // 위의 헤더 값들이 없거나 유효하지 않을 경우, 마지막으로 클라이언트의 실제 IP를 반환
//        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//
//        log.info("ipv4...........{}", InetAddress.getLocalHost().getHostAddress());
//
//        return ip;
//    }
//}


public class ClientIpUtil {
    // 클라이언트 IP를 가져오는 메서드
    public static String getClientIp(HttpServletRequest request) throws UnknownHostException {

        // X-Forwarded-For: 프록시나 로드 밸런서를 통과한 원본 클라이언트의 IP를 저장하는 헤더
        String ip = request.getHeader("X-Forwarded-For");

        // Proxy-Client-IP 및 WL-Proxy-Client-IP: 각각 프록시 서버나 웹로직 서버에 의해 설정된 클라이언트 IP
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        // 위의 헤더 값들이 없거나 유효하지 않을 경우, 마지막으로 클라이언트의 실제 IP를 반환
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // IPv6 주소가 아닌 경우 IPv4 주소만 반환
        if (ip != null && ip.contains(":")) {
            InetAddress inetAddress = InetAddress.getLocalHost();
            ip = inetAddress.getHostAddress(); // IPv4 주소로 변환
        }

        log.info("IPv4 address: {}", ip);

        return ip;
    }
}