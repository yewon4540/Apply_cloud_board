package com.chunjae.awscloudrequestboard.repository;

import com.chunjae.awscloudrequestboard.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByIp(String ip);

    Optional<User> findNameByIp(String ip);  // IP로 User를 조회하는 메서드
}
