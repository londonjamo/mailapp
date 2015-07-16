package net.londonjamo.domain;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by jamo on 6/27/15.
 */
public class MessageRequestTest {

    @Test
    public void test1() {
        MessageRequest cut = new MessageRequest();
        assertNotNull(cut);
    }

    @Test
    public void test2() {
        MessageRequest cut = new MessageRequest("foobar");
        assertEquals("foobar", cut.getId());
    }
}
