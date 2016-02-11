package test;

import com.balihoo.sdk.BlipResponse;
import org.junit.Test;
import static org.junit.Assert.*;

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