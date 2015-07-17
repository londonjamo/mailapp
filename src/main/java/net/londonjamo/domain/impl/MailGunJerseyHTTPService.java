package net.londonjamo.domain.impl;

import net.londonjamo.domain.MailService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import io.vertx.core.Vertx;
import net.londonjamo.domain.MessageRequest;
import net.londonjamo.domain.MessageResponse;

import javax.ws.rs.core.MediaType;

/**
 * Created by jamo on 6/16/15.
 */
public class MailGunJerseyHTTPService implements MailService {
    public static final String KEY = "key-b10fcab698aa1b2429fab8c3a2835e2c";
    public static final String MAILGUN_URL = "https://api.mailgun.net/v3/sandboxf4163ef4f52a472faddab35cb19c1883.mailgun.org/messages";

    Client client;

    public MailGunJerseyHTTPService(Vertx vertx) {
        client = Client.create();
    }

    public MessageResponse send(MessageRequest messageRequest) {
        client.addFilter(new HTTPBasicAuthFilter("api", KEY));
        WebResource webResource = client.resource(MAILGUN_URL);

        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("from", messageRequest.getDetails().getString("from"));
        formData.add("to", messageRequest.getDetails().getString("to"));
        formData.add("subject", messageRequest.getDetails().getString("subject"));
        formData.add("text", messageRequest.getDetails().getString("text"));

        ClientResponse cr = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
        MessageResponse response = new MessageResponse(cr.getEntity(String.class));
        int status = cr.getStatus();
        if (status == 200) {
            response.setSuccessful(true);
        } else {
            response.setSuccessful(false);
        }
        return response;
    }

}
