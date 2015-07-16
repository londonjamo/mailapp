package net.londonjamo.domain;

/**
 * Created by jamo on 6/16/15.
 */
public interface MailService {
    MessageResponse send(MessageRequest messageRequest);
}
