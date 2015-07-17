package net.londonjamo.domain.impl;

import net.londonjamo.domain.MessageRequest;
import net.londonjamo.domain.MessageRepository;
import io.vertx.core.json.JsonObject;
import org.mapdb.DBMaker;

import java.util.Map;

/**
 * Created by jamo on 6/19/15.
 */
public class MapDBMessageRepository implements MessageRepository {

    Map<String, String> db;

    public MapDBMessageRepository() {
        db = DBMaker.newTempHashMap();
    }

    @Override
    public void addResponse(String messageRequestId, String sendInfo) {
        MessageRequest messageRequest = getMessage(messageRequestId);
        messageRequest.addResponse(new JsonObject(sendInfo));
        saveMessage(messageRequestId, messageRequest);
    }

    @Override
    public void saveMessage(String key, MessageRequest messageRequest) {
        db.put(key, messageRequest.encode());
    }

    @Override
    public MessageRequest getMessage(String key) {
        String result = db.get(key);
        return new MessageRequest(new JsonObject(result));
    }
}
