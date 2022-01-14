package com.codebase.socialnetwork.repository;

import com.codebase.socialnetwork.domain.Message;
import com.codebase.socialnetwork.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends Repository<Long, Message> {
    List<Message> getNumberOfSentMessages(Long idUser, LocalDateTime startDate, LocalDateTime endDate);

    List<Message> getNumberOfReceivedMessages(Long idUser, LocalDateTime startDate, LocalDateTime endDate);

    List<Message> getMessagesFromFriend(User user, User friend, LocalDateTime startDate, LocalDateTime endDate);
}
