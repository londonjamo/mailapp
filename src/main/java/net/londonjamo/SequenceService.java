package net.londonjamo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jamo on 6/19/15.
 */
public class SequenceService extends AbstractVerticle {

        int sequence = 0;

        @Override
        public void start() throws Exception {
            MessageConsumer<String> consumer = vertx.eventBus().consumer("ID");
            consumer.handler(message -> {
                message.reply(getId());
            });
        }

        public String getId() {
            return System.currentTimeMillis() + "_" + sequence++;
        }
}
