package org.baoxdev.hotelbooking_test.repository;

import org.baoxdev.hotelbooking_test.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken , Long> {

    RefreshToken findByToken(String token);

    void deleteRefreshTokenByToken(String token);
}
