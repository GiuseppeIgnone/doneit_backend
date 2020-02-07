package com.sini.doneit.repository;

import com.sini.doneit.model.Event;
import com.sini.doneit.model.EventPartecipation;

import java.util.List;

import com.sini.doneit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventPartecipationJpaRepository extends JpaRepository<EventPartecipation, Long> {

    @Query("select e.event from EventPartecipation e where e.user = :user order by e.event.id desc")
    List<Event> getMyJoinedEvent(@Param("user") User user);
}
