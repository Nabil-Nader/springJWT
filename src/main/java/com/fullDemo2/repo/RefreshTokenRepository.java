package com.fullDemo2.repo;

import com.fullDemo2.Entity.MyUser;
import com.fullDemo2.Entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Override
    Optional<RefreshToken> findById(Long id);

    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);

    int deleteByUser(MyUser myUser);
}
