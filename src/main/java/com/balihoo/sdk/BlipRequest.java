package main.java.com.balihoo.sdk;

import java.io.*;
import java.net.*;

/**
 * An HTTP request to the BLIP API.
 */
class BlipRequest {

    private String Credentials;
    private String Endpoint;

    protected enum Command {
        Get,
        Put,
        Post,
        Delete
    }

    /**
     * The BlipRequest constructor.
     * @param credentials The Base64-encoded BLIP credentials.
     * @param endpoint The base URL for the BLIP environment.
     */
    protected BlipRequest(String credentials, String endpoint) {
        this.Credentials = credentials;
        this.Endpoint = endpoint;
    }

    /**
     * Executes the specified HTTP command.
     * @param command The HTTP command.
     * @param path The URI path for the API function to be executed.
     * @param content Any content that may need to be supplied to the API.
     * @return A BlipResponse object.
     */
    protected BlipResponse ExecuteCommand(Command command, String path, String content) {
        System.out.print(content);
        switch (command) {
            case Get:
                return buildBlipResponse(ConfigureClient("GET", path, content));
            case Put:
                return buildBlipResponse(ConfigureClient("PUT", path, content));
            case Post:
                return buildBlipResponse(ConfigureClient("POST", path, content));
            case Delete:
                return buildBlipResponse(ConfigureClient("DELETE", path, content));
            default:
                throw new RuntimeException(String.format("Invalid Command value: %s", command));
        }
    }

    /**
     * Creates and configures the HttpURLConnection client with credentials, headers, etc.
     * @param path The URI path for the API function to be executed.
     * @param command The HTTP command.
     * @param content Any content that may need to be supplied to the API.
     * @return A configured HttpURLConnection client.
     */
    private HttpURLConnection ConfigureClient(String command, String path, String content) {
        try {
            URL url = new URL(String.format("%s%s", this.Endpoint, path));
            HttpURLConnection client = (HttpURLConnection) url.openConnection();

            client.setRequestProperty("Authorization", String.format("Basic %s", this.Credentials));
            client.setRequestProperty("Content-Type", "application/json");
            client.setRequestProperty("Accept", "application/json");
            client.setRequestMethod(command);

            if (content != null) {
                client.setDoOutput(true);
                OutputStream os = client.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(content);
                writer.flush();
                writer.close();
                os.close();
            }

            return client;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert the HTTP response into a BlipResponse object.
     * @param client An HttpURLConnection client.
     * @return A BlipResponse object.
     */
    private BlipResponse buildBlipResponse(HttpURLConnection client) {
        try {
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
            }

            String body = builder.toString();
            reader.close();

            BlipResponse blipResponse = new BlipResponse(statusCode, body);
            client.disconnect();

            return blipResponse;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
