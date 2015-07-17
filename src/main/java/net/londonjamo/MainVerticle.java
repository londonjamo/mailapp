package net.londonjamo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import net.londonjamo.domain.MessageRequest;
import net.londonjamo.domain.MessageRequestCodec;
import net.londonjamo.domain.MessageResponse;
import net.londonjamo.domain.MessageResponseCodec;

/**
 * Created by jamo on 6/16/15.
 */
public class MainVerticle extends AbstractVerticle {
    public static String CONFIG_FILE = "mailapp.json"; // needs to be on classpath

    @Override
    public void start() throws Exception {
        DeploymentOptions options = new DeploymentOptions();

        if (vertx.fileSystem().existsBlocking(CONFIG_FILE)) {
            System.out.println(CONFIG_FILE);
            String fileContents = vertx.fileSystem().readFileBlocking(CONFIG_FILE).toString();
            options.setConfig(new JsonObject(fileContents));
        }

        getVertx().deployVerticle(new PersistorService());
        getVertx().deployVerticle(new ApiVerticle());
        getVertx().deployVerticle(new MailManager(), new DeploymentOptions(options).setWorker(true));
        getVertx().eventBus().registerDefaultCodec(MessageRequest.class, new MessageRequestCodec());
        getVertx().eventBus().registerDefaultCodec(MessageResponse.class, new MessageResponseCodec());
    }

}
