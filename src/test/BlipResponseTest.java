package test;

import org.junit.Test;
import static org.junit.Assert.*;
import main.java.com.balihoo.sdk.BlipResponse;

public class BlipResponseTest {

    @Test
    public void TestBlipResponseSuccessfullyConvertsHttpResponse() {
        int statusCode = 200;
        String message = "Successful Test";
        BlipResponse blipResponse = new BlipResponse(statusCode, message);

        assertEquals(blipResponse.StatusCode, statusCode);
        assertEquals(blipResponse.Body, message);
    }
}