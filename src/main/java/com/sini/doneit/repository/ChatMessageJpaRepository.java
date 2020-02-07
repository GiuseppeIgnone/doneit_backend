package com.sini.doneit.repository;

import com.sini.doneit.model.MessageChat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatMessageJpaRepository extends JpaRepository<MessageChat, Long> {


    @Query("select m from MessageChat  m where (m.from.id = :from and m.to.id = :to) " +
            "or (m.from.id = :to and m.to = :from) order by m.id")
    List<MessageChat> getConversationByUser(@Param("from") Long from, @Param("to") Long to);
}
