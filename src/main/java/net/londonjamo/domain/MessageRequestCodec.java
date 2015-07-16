package net.londonjamo.domain;

import io.netty.util.CharsetUtil;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;
import net.londonjamo.domain.MessageRequest;

/**
 * Created by jamo on 5/18/15.
 */

public class MessageRequestCodec implements MessageCodec<MessageRequest, MessageRequest> {

    @Override
    public void encodeToWire(Buffer buffer, MessageRequest messageRequest) {
        String strJson = messageRequest.toString();
        byte[] encoded = strJson.getBytes(CharsetUtil.UTF_8);
        buffer.appendInt(encoded.length);
        Buffer buff = Buffer.buffer(encoded);
        buffer.appendBuffer(buff);
    }

    @Override
    public MessageRequest decodeFromWire(int pos, Buffer buffer) {
        int length = buffer.getInt(pos);
        pos += 4;
        byte[] encoded = buffer.getBytes(pos, pos + length);
        String str = new String(encoded, CharsetUtil.UTF_8);
        JsonObject json = new JsonObject(str);
        return new MessageRequest(json);
    }

    @Override
    public MessageRequest transform(MessageRequest messageRequest) {
        return new MessageRequest(messageRequest.getJson().copy());
    }

    @Override
    public String name() {
        return "messageRequest";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }

}