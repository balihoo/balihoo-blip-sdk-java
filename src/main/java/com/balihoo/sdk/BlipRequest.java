package com.balihoo.sdk;

import java.io.*;
import java.net.*;
import javax.xml.bind.DatatypeConverter;

/**
 * An HTTP request to the BLIP API.
 */
class BlipRequest {

    private final String CREDENTIALS;
    private final String ENDPOINT;

    protected enum Command {
        GET,
        PUT,
        POST,
        DELETE
    }

    /**
     * The BlipRequest constructor.
     * @param apiKey The key used to access the BLIP API.
     * @param secretKey The secret key used to access the BLIP API.
     * @param endpoint The base URL for the BLIP environment.
     */
    protected BlipRequest(String apiKey, String secretKey, String endpoint) {
        CREDENTIALS = encodeCredentials(apiKey, secretKey);
        ENDPOINT = endpoint;
    }

    /**
     * Executes the specified HTTP command.
     * @param command The HTTP command.
     * @param path The URI path for the API function to be executed.
     * @param content Any content that may need to be supplied to the API.
     * @return A BlipResponse object.
     * @throws IOException if response cannot be parsed.
     */
    protected BlipResponse ExecuteCommand(Command command, String path, String content) throws IOException {
        return buildBlipResponse(ConfigureClient(command, path, content));
    }

    /**
     * Creates and configures the HttpURLConnection client with credentials, headers, etc.
     * @param path The URI path for the API function to be executed.
     * @param command The HTTP command.
     * @param content Any content that may need to be supplied to the API.
     * @return A configured HttpURLConnection client.
     * @throws IOException if response cannot be parsed.
     */
    private HttpURLConnection ConfigureClient(Command command, String path, String content) throws IOException {
        URL url = new URL(String.format("%s%s", ENDPOINT, path));
        HttpURLConnection client = (HttpURLConnection) url.openConnection();

        client.setRequestProperty("Authorization", String.format("Basic %s", CREDENTIALS));
        client.setRequestProperty("Content-Type", "application/json");
        client.setRequestProperty("Accept", "application/json");
        client.setRequestMethod(command.toString());

        if (content != null) {
            client.setDoOutput(true);
            OutputStream os = client.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            try {
                writer.write(content);
                writer.flush();
            } finally {
                writer.close();
                os.close();
            }
        }

        return client;
    }

    /**
     * Convert the HTTP response into a BlipResponse object.
     * @param client An HttpURLConnection client.
     * @return A BlipResponse object.
     * @throws IOException if response cannot be parsed.
     */
    private BlipResponse buildBlipResponse(HttpURLConnection client) throws IOException {
        BufferedReader reader;
        int statusCode = client.getResponseCode();

        if (200 <= statusCode && statusCode <= 299) {
            reader = new BufferedReader(new InputStreamReader((client.getInputStream())));
        } else {
            reader = new BufferedReader(new InputStreamReader((client.getErrorStream())));
        }

        StringBuilder builder = new StringBuilder();
        String text;

        while ((text = reader.readLine()) != null) {
            builder.append(text);
            builder.append(System.lineSeparator());
        }

        String body = builder.toString();
        reader.close();

        BlipResponse blipResponse = new BlipResponse(statusCode, body);
        client.disconnect();

        return blipResponse;
    }

    /**
     * Base64 encode the API keys.
     * @param apiKey The key used to access the BLIP API.
     * @param secretKey The secret key used to access the BLIP API.
     * @return Base64 encoded credentials.
     */
    private String encodeCredentials(String apiKey, String secretKey) {
        String credentials = String.format("%s:%s", apiKey, secretKey);

        return DatatypeConverter.printBase64Binary(credentials.getBytes());
    }
}
