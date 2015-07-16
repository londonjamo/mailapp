package net.londonjamo.domain.impl.async;

import net.londonjamo.domain.MailServiceAsync;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;
import io.vertx.core.http.CaseInsensitiveHeaders;

/**
 * Created by jamo on 6/16/15.
 */
public class MailGunSMTPService implements MailServiceAsync {

    MailClient mailClient;
    Vertx vertx;

    public static final String user = "test@sandboxf4163ef4f52a472faddab35cb19c1883.mailgun.org";
    public static final String pw = "testtest";

    public MailGunSMTPService(Vertx vertx) {
        MailConfig config = new MailConfig();
        config.setHostname("smtp.mailgun.org");
        config.setPort(587);
        //config.setStarttls(StartTLSOptions.REQUIRED);
        config.setUsername(user);
        config.setPassword(pw);
        this.vertx = vertx;
        mailClient = MailClient.createNonShared(vertx, config);
    }


    @Override
    public void send(JsonObject message, AsyncResultHandler<JsonObject> handler) {
        MailMessage mailMessage = new MailMessage(message.getJsonObject("body"));

        CaseInsensitiveHeaders headers = new CaseInsensitiveHeaders();
        headers.set("X-Mailgun-Tag", "firstrun");

        mailClient.sendMail(mailMessage, result -> {

            JsonObject update = new JsonObject();
            JsonObject sendInfo = new JsonObject();
            update.put("id", message.getJsonObject("metadata").getString("id"));
            sendInfo.put("sent-by", "mailgun");
            sendInfo.put("send-result", result.result().toJson());
            update.put("send-info", sendInfo);
            update.put("key", "send-info");

            vertx.eventBus().publish("UPDATE_MESSAGE", update);
            System.out.println(result.result().toString());

            //result.cause().printStackTrace();
            handler.handle(new AsyncResult<JsonObject>() {
                @Override
                public JsonObject result() {
                    return message;
                }

                @Override
                public Throwable cause() {
                    return result.cause();
                }

                @Override
                public boolean succeeded() {
                    return result.succeeded();
                }

                @Override
                public boolean failed() {
                    return result.failed();
                }
            });
        });
    }

}
