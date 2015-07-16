package net.londonjamo.domain;

import io.vertx.core.AsyncResultHandler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mail.MailMessage;

/**
 * Created by jamo on 6/16/15.
 */
public interface MailServiceAsync
{
    void send(JsonObject message, AsyncResultHandler<JsonObject> handler);
}
