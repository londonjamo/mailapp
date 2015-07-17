package net.londonjamo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import net.londonjamo.domain.MessageRequest;

/**
 * Created by jamo on 6/16/15.
 */
public class ApiVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        Router router = Router.router(getVertx());
        router.mountSubRouter("/mail", getRouter(getVertx()));
        router.route("/mailapp/*").handler(StaticHandler.create());

        getVertx().createHttpServer().requestHandler(router::accept).listen(8080);
    }

    public  Router getRouter(Vertx vertx) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create().setBodyLimit(1000L));

        SequenceService sequence = new SequenceService();

        router.get("/check/:id").handler(rc -> {
            String id = rc.request().getParam("id");
            vertx.eventBus().send("GET_MESSAGE", new JsonObject().put("id", id), res -> {
                if (res.succeeded()) {
                    rc.response().end(res.result().body().toString());
                } else {
                    rc.response().setStatusCode(404).setStatusMessage("unable to get mail").end();
                }
            });
        });

        router.post("/send").handler(rc -> {
            String id = "" + sequence.getId();
            JsonObject message = rc.getBodyAsJson();
            System.out.println("MESSAGE IS " + message);
            DeliveryOptions options = new DeliveryOptions();
            options.addHeader("id", id);
            options.addHeader("received", System.currentTimeMillis() + "");

            vertx.eventBus().send("SAVE_QUEUE", message, options, res -> {
                if (res.succeeded()) {
                    MessageRequest m = new MessageRequest(new JsonObject(res.result().body().toString()));
                    vertx.eventBus().send("SEND_QUEUE", m, options, ar -> {
                        if (ar.succeeded()) {
                            rc.response().end(new JsonObject().put("id", id).put("response", ar.result().body()).encode());
                        } else {
                            rc.response().setStatusCode(500).setStatusMessage("unable to send mail").end();
                        }
                    });
                } else { // unable to persist message
                    rc.response().setStatusCode(500).setStatusMessage("internal server error").end();
                }
            });

        });

        return router;
    }
}
