package com.sini.doneit.repository;

import com.sini.doneit.model.Proposal;
import com.sini.doneit.model.Todo;
import java.util.List;

import com.sini.doneit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProposalJpaRepository extends JpaRepository<Proposal,Long>{


    @Query("select p.todo from Proposal p where p.user = :user and p.state = 'in progress'")
    List<Todo> getJoinedTodo(@Param("user") User user);

}
