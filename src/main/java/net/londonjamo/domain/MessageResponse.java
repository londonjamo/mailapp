package net.londonjamo.domain;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by jamo on 6/27/15.
 */

    /*
    yet another thin wrapper over jsonobject
     */
public class MessageResponse {
    private boolean successful = true;
    private JsonObject response = new JsonObject();


    public MessageResponse() {
        response = new JsonObject();
    }

    public MessageResponse(JsonObject json) {
        this();
        response = json;
    }

    public MessageResponse(String json) {
        this(new JsonObject(json));
    }

    public String encode() {
        return response.encode();
    }

    public String toString() {
        return this.encode();
    }

    public JsonObject getJson() {
        return response;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

}
