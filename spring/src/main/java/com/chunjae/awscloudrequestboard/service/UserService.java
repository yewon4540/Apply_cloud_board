package com.chunjae.awscloudrequestboard.service;

import com.chunjae.awscloudrequestboard.dto.UserDTO;

public interface UserService {
    boolean findByIp(String clientIp);

    UserDTO findUserByIp(String clientIp);

}
