package net.londonjamo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import net.londonjamo.domain.MessageRequest;
import net.londonjamo.domain.MessageRequestCodec;
import net.londonjamo.domain.MessageResponse;
import net.londonjamo.domain.MessageResponseCodec;

/**
 * Created by jamo on 6/16/15.
 */
public class MainVerticle extends AbstractVerticle {

        @Override
        public void start() throws Exception {
            getVertx().deployVerticle(new PersistorService());
            getVertx().deployVerticle(new MailManager(), new DeploymentOptions().setWorker(true));
            getVertx().deployVerticle(new ApiVerticle());
            getVertx().eventBus().registerDefaultCodec(MessageRequest.class, new MessageRequestCodec());
            getVertx().eventBus().registerDefaultCodec(MessageResponse.class, new MessageResponseCodec());
        }
}
