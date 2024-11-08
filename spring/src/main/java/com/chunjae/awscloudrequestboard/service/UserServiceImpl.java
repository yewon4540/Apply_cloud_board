package com.chunjae.awscloudrequestboard.service;

import com.chunjae.awscloudrequestboard.dto.UserDTO;
import com.chunjae.awscloudrequestboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public boolean findByIp(String ip) {
        // IP가 존재하는지 확인하는 로직
        return userRepository.existsByIp(ip);  // boolean 반환하는 메서드
    }

    @Override
    public UserDTO findUserByIp(String clientIp) {
        UserDTO user = modelMapper.map(userRepository.findNameByIp(clientIp), UserDTO.class);
        return user;
    }

}
