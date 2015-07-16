package net.londonjamo.domain;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by jamo on 6/27/15.
 */

/*
   id : {
        body: {}
        meta: {}
        responses: []
   }
 */
    /*
    thin wrapper over jsonobject
     */
public class MessageRequest {

    public static final String UNKNOWN = "-1";
    public static final String ID = "id";
    public static final String METADATA = "metadata";
    public static final String DETAILS = "details";
    public static final String RESPONSES = "responses";

    private JsonObject envelope = new JsonObject();
    private JsonObject details = new JsonObject();
    private JsonObject metadata = new JsonObject();
    private JsonArray responses = new JsonArray();

    public MessageRequest() {
        envelope = new JsonObject().put(DETAILS, details).put(METADATA, metadata).put(RESPONSES, responses);
    }

    public MessageRequest(String id) {
        this();
        envelope.getJsonObject(METADATA).put(ID, id);
    }

    public MessageRequest(JsonObject json) {
        this();
        envelope = json;

    }

    public String encode() {
        return envelope.encode();
    }

    public String toString() {
        return this.encode();
    }

    public JsonObject getJson() {
        return envelope;
    }

    public String getId() {
       return  envelope.getJsonObject(METADATA).getString(ID, UNKNOWN);
    }

    public JsonObject getDetails() {
        return envelope.getJsonObject(DETAILS);
    }

    public void setDetails(JsonObject details) {
        envelope.put(DETAILS, details);
    }

    public JsonObject getMetadata() {
        return envelope.getJsonObject(METADATA);
    }

    public void setMetadata(JsonObject metadata) {
        envelope.put(METADATA, metadata);
    }

    public JsonArray getResponses() {
        return envelope.getJsonArray(RESPONSES);
    }

    public void addResponse(JsonObject info) {
        envelope.getJsonArray(RESPONSES).add(info);
    }
}
