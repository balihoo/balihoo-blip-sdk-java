package com.balihoo.sdk;

/**
 * The response returned when a BLIP API call is made.
 */
public class BlipResponse {

    public final int STATUS_CODE;
    public final String BODY;

    /**
     * The BlipResponse constructor.
     * @param statusCode The numeric HTTP status code that is returned.
     * @param body The body text that is returned in the HTTP response.
     */
    public BlipResponse(int statusCode, String body) {
        STATUS_CODE = statusCode;
        BODY = body;
    }
}
