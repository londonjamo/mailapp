package net.londonjamo.domain.impl;

import net.londonjamo.domain.MailService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import net.londonjamo.domain.MessageRequest;
import net.londonjamo.domain.MessageResponse;

import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * Created by jamo on 6/16/15.
 */
public class MandrillJerseyHTTPService implements MailService {
    private String mandrillUrl;
    private String mandrillKey;

    Client client;

    public MandrillJerseyHTTPService(Vertx vertx) {
        mandrillKey = vertx.getOrCreateContext().config().getString("MANDRILL_KEY");
        mandrillUrl = vertx.getOrCreateContext().config().getString("MANDRILL_URL");

        client = Client.create();
    }

    public MandrillJerseyHTTPService(JsonObject options) {
        mandrillKey = options.getString("MANDRILL_KEY");
        mandrillUrl = options.getString("MANDRILL_URL");

        client = Client.create();
    }

    public MessageResponse send(MessageRequest messageRequest) {
        WebResource webResource = client.resource(mandrillUrl);
        JsonObject mandrillJson = new JsonObject();
        JsonObject mandrillMessage = new JsonObject();
        mandrillJson.put("key", mandrillKey);
        mandrillMessage.put("text", messageRequest.getDetails().getString("text"));
        mandrillMessage.put("subject", messageRequest.getDetails().getString("subject"));
        mandrillMessage.put("from_email", messageRequest.getDetails().getString("from"));
        mandrillMessage.put("from_name", messageRequest.getDetails().getString("from_name", ""));
        mandrillMessage.put("to", new JsonArray().add(new JsonObject().put("email", messageRequest.getDetails().getString("from")).put("type", "to")));
        mandrillJson.put("message", mandrillMessage);

        ClientResponse cr = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, mandrillJson.encode());
        String responseString = cr.getEntity(String.class);

        MessageResponse response = new MessageResponse(new JsonArray(responseString).getJsonObject(0));

        int status = cr.getStatus();
        if (status == 200) {
            response.setSuccessful(true);
        } else {
            response.setSuccessful(false);
        }
        return response;
    }
}
