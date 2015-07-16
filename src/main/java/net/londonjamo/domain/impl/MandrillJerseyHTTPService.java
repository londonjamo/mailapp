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

/**
 * Created by jamo on 6/16/15.
 */
public class MandrillJerseyHTTPService implements MailService {

    public static final String MANDRILL_URL = "https://mandrillapp.com:443/api/1.0/messages/send.json";
    public static final String KEY = "AfalKpHeJOo5CmREeZp7Sg";

    Client client;

    public MandrillJerseyHTTPService(Vertx vertx) {
        client = Client.create();
    }

    public MessageResponse send(MessageRequest messageRequest) {
        WebResource webResource = client.resource(MANDRILL_URL);
        System.out.println("DEBUG"+ messageRequest);
        JsonObject mandrillJson = new JsonObject();
        JsonObject mandrillMessage = new JsonObject();
        mandrillJson.put("key", KEY);
        mandrillMessage.put("text", messageRequest.getDetails().getString("text"));
        mandrillMessage.put("subject", messageRequest.getDetails().getString("subject"));
        mandrillMessage.put("from_email", messageRequest.getDetails().getString("from"));
        mandrillMessage.put("from_name", messageRequest.getDetails().getString("from_name", ""));
        mandrillMessage.put("to", new JsonArray().add(new JsonObject().put("email", messageRequest.getDetails().getString("from")).put("type", "to")));
        mandrillJson.put("messageRequest", mandrillMessage);

        ClientResponse cr = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, mandrillJson.encode());
        System.out.println("1" + cr.getStatusInfo().toString());
        String responseString = cr.getEntity(String.class);

        MessageResponse response = new MessageResponse(responseString);
        int status = cr.getStatus();
        System.out.println( "FOOm"+status);
        if (status == 200) {
            response.setSuccessful(true);
        } else {
            response.setSuccessful(false);
        }
        return response;
    }
}
