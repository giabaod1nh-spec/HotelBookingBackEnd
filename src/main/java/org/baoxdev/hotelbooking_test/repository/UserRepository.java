package org.baoxdev.hotelbooking_test.repository;

import org.baoxdev.hotelbooking_test.model.entity.Hotel;
import org.baoxdev.hotelbooking_test.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , String> , JpaSpecificationExecutor<Hotel> {
    Optional<User> findUserByUserId(String userId);

    boolean existsUserByUserName(String userName);

    boolean existsUserByEmail(String email);

    Optional<User> findUserByUserName(String userName);

    Page<User> findAll(Pageable pageable);
}
