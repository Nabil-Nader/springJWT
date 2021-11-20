package com.fullDemo2.services;

import com.fullDemo2.Entity.MyUser;
import com.fullDemo2.Entity.RefreshToken;
import com.fullDemo2.exception.ExceptionHandling;
import com.fullDemo2.exception.TokenRefreshException;
import com.fullDemo2.repo.MyUserRepo;
import com.fullDemo2.repo.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.fullDemo2.constant.SecurityConstant.REFRESH_TOKEN;

@Service
@Transactional
public class RefreshTokenService {


    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private MyUserRepo userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(REFRESH_TOKEN));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException();
        }

        return token;
    }

    public void savToken(RefreshToken token,String old){
        refreshTokenRepository.deleteByToken(old);

        refreshTokenRepository.save(token);

    }




}
