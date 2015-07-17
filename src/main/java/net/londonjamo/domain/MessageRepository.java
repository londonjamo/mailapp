package net.londonjamo.domain;

/**
 * Created by jamo on 6/19/15.
 */
public interface MessageRepository {
    void saveMessage(String key, MessageRequest messageRequest);
    MessageRequest getMessage(String key);
    void addResponse(String MessageRequestId, String sendInfo);

}

