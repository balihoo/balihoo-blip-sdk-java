package main.java.com.balihoo.sdk;

/**
 * The response returned when a BLIP API call is made.
 */
public class BlipResponse {

    public int StatusCode;
    public String Body;

    /**
     * The BlipResponse constructor.
     * @param statusCode The numeric HTTP status code that is returned.
     * @param body The body text that is returned in the HTTP response.
     */
    public BlipResponse(int statusCode, String body) {
        this.StatusCode = statusCode;
        this.Body = body;
    }
}
