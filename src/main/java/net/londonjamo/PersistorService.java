package net.londonjamo;

import net.londonjamo.domain.MessageRequest;
import net.londonjamo.domain.MessageRepository;
import net.londonjamo.domain.MessageResponse;
import net.londonjamo.domain.impl.MapDBMessageRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

/**
 * Created by jamo on 6/19/15.
 */
public class PersistorService extends AbstractVerticle {
    MessageRepository repo;

    @Override
    public void start() {
        EventBus eb = getVertx().eventBus();
        repo = new MapDBMessageRepository();

        MessageConsumer<JsonObject> saveListener = eb.consumer("SAVE_QUEUE");
        saveListener.handler(message -> {
            MessageRequest m = new MessageRequest(message.headers().get("id"));
            m.setDetails(message.body());
            m.getMetadata().put("user",m.getDetails().getString("from"));
            m.getMetadata().put("received",message.headers().get("received"));
            persist(m.getId(),m);
            message.reply(m.encode());
        });

        MessageConsumer<MessageResponse> responseListener = eb.consumer("RESPONSE_QUEUE");
        responseListener.handler(message -> {
            String id = message.headers().get("id");
            updateMessageRequestWithResponse(id,message.body().getJson());
        });

        MessageConsumer<JsonObject> getListener = eb.consumer("GET_MESSAGE");
        getListener.handler(message -> {
            String id = message.body().getString("id");
            MessageRequest dbEntry = getMessage(id);
            message.reply(dbEntry);
        });

    }

    public void persist(String key, MessageRequest messageRequest) {
        repo.saveMessage(key, messageRequest);
    }

    public void updateMessageRequestWithResponse(String messageRequestId, JsonObject response) {
        repo.addResponse(messageRequestId,response.encode());
    }

    public void updateMetaData(String messageId, String key, JsonObject value) {
        System.out.println("updating " + messageId +":" + key +":"+ value);
//        Message message = repo.getMessage(messageId);
//        message.getJsonObject("metadata").put(key,value);
//        repo.saveMesage(messageId, message);
    }

    public MessageRequest getMessage(String messageId) {
        return repo.getMessage(messageId);
    }

}
