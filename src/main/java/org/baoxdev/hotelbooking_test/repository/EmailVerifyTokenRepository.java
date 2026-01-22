package org.baoxdev.hotelbooking_test.repository;

import org.baoxdev.hotelbooking_test.model.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmailVerifyTokenRepository extends JpaRepository<EmailVerificationToken , String> {

    EmailVerificationToken findByEmailVerifyToken(String emailVerifyToken);
}
