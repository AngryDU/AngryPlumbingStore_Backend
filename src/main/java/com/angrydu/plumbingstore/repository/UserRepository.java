package com.angrydu.plumbingstore.repository;

import com.angrydu.plumbingstore.entity.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Qualifier(value = "UserRepository")
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query(value = "select u from User u where lower(u.email) = lower(:email)")
    Optional<User> findByEmail(@Param("email") String email);

    @Query(value = "select u from User u where lower(u.email) = lower(:email) and u.isActive = true")
    Optional<User> findByEmailActive(@Param("email") String email);
}
