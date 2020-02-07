package com.sini.doneit.repository;

import com.sini.doneit.model.Followers;
import com.sini.doneit.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    User findByUsernameAndPassword(String username, String password);

    @Query("select u.id from User u where u.username = :username")
    Long getIdByUsername(@Param("username") String username);

    User findByUsername(String username);

    User findByEmail(String email);

    @Query("select u.username from User u where u.username like :string%")
    List<String> getUserStartedWithString(@Param("string")String string);
}
