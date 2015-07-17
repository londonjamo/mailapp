package net.londonjamo;

import io.vertx.core.eventbus.DeliveryOptions;
import net.londonjamo.domain.MailService;
import net.londonjamo.domain.MessageRequest;
import net.londonjamo.domain.MessageResponse;
import net.londonjamo.domain.impl.MailGunJerseyHTTPService;
import net.londonjamo.domain.impl.MandrillJerseyHTTPService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jamo on 6/19/15.
 */
public class MailManager extends AbstractVerticle {

    @Override
    public void start() throws Exception {

        EventBus eb = vertx.eventBus();

        ArrayList<MailService> services = new ArrayList<>();
        services.add(new MailGunJerseyHTTPService(vertx));
        services.add(new MandrillJerseyHTTPService(vertx));

        MessageConsumer<MessageRequest> newMessageListener = eb.consumer("SEND_QUEUE");

        newMessageListener.handler(message -> {
            MessageResponse response = sendMail(services, message.body());
            message.reply(response.encode());
        });

    }

    private MessageResponse sendMail(ArrayList<MailService> services, MessageRequest messageRequest) {
        MailService flip = services.get(1);
        MailService flop = services.get(0);

        MessageResponse r = flip.send(messageRequest);
        DeliveryOptions options = new DeliveryOptions().addHeader("id", messageRequest.getId());
        vertx.eventBus().send("RESPONSE_QUEUE", r, options);

        if (!r.isSuccessful()) {
            Collections.reverse(services);
            r = flop.send(messageRequest);
            vertx.eventBus().send("RESPONSE_QUEUE", r,options);
        }

        // TODO should really put on DLQ if still unsuccessful
        return r;
    }

}
