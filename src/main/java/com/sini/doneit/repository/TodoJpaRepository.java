package com.sini.doneit.repository;

import com.sini.doneit.model.Todo;
import com.sini.doneit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TodoJpaRepository extends JpaRepository<Todo, Long> {

    @Query("select t from Todo t where (t.expirationDate >= :currentDate or t.expirationDate is null) and t.state like 'published' order by t.id desc")
    List<Todo> findAllActiveTodo(@Param("currentDate") Date currentDate);

    List<Todo> findByUserAndState(User user, String state);

    @Query("select t from Todo t where t.user.username like :username and (t.state like 'published' or t.state like 'completed') order by t.id desc")
    List<Todo> findProfileTodoList(@Param("username") String username);


}
