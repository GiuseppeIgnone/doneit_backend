package com.sini.doneit.repository;

import com.sini.doneit.model.Followers;
import com.sini.doneit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface FollowingJpaRepository extends JpaRepository<Followers, Long> {

    @Query("select f.from from Followers f where f.to.id = :id")
    List<User> getUserFollowers(@Param("id") Long id);

    @Query("select f.to from Followers f where f.from.id = :id")
    List<User> getUserFollowing(@Param("id") Long id);
}
