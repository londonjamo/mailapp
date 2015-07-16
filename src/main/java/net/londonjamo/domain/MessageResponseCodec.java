package net.londonjamo.domain;

import io.netty.util.CharsetUtil;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

/**
 * Created by jamo on 5/18/15.
 */

public class MessageResponseCodec implements MessageCodec<MessageResponse, MessageResponse> {

    @Override
    public void encodeToWire(Buffer buffer, MessageResponse messageResponse) {
        String strJson = messageResponse.toString();
        byte[] encoded = strJson.getBytes(CharsetUtil.UTF_8);
        buffer.appendInt(encoded.length);
        Buffer buff = Buffer.buffer(encoded);
        buffer.appendBuffer(buff);
    }

    @Override
    public MessageResponse decodeFromWire(int pos, Buffer buffer) {
        int length = buffer.getInt(pos);
        pos += 4;
        byte[] encoded = buffer.getBytes(pos, pos + length);
        String str = new String(encoded, CharsetUtil.UTF_8);
        JsonObject json = new JsonObject(str);
        return new MessageResponse(json);
    }

    @Override
    public MessageResponse transform(MessageResponse messageResponse) {
        return new MessageResponse(messageResponse.getJson().copy());
    }

    @Override
    public String name() {
        return "messageResponse";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }

}