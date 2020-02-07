package com.sini.doneit.repository;

import com.sini.doneit.model.PersonalCard;
import com.sini.doneit.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonalCardJpaRepository extends JpaRepository<PersonalCard, Long> {

    PersonalCard findByUserId(Long users);

    @Query("select p.base64StringImage from PersonalCard p where p.user.id in (:users)")
    List<String> getUsersImage(@Param("users") List<Long> users);
}
