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
        saveMesage(messageRequestId, messageRequest);
    }

    @Override
    public void saveMesage(String key, MessageRequest messageRequest) {
        System.out.println("saving messageRequest " + messageRequest);

        db.put(key, messageRequest.encode());

        System.out.println(db.values());
        System.out.println(db.keySet());
    }

    @Override
    public MessageRequest getMessage(String key) {
        String result = db.get(key);
        System.out.println(result);
        return new MessageRequest(new JsonObject(result));
    }
}
