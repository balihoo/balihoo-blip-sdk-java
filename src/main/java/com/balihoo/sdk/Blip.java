package com.balihoo.sdk;

import java.io.IOException;

/**
 * The BLIP object and its methods.
 */
public class Blip {

    private final String API_KEY;
    private final String SECRET_KEY;
    private final String ENDPOINT;

    /**
     * Blip constructor with default production endpoint.
     * @param apiKey The key used to access the BLIP API.
     * @param secretKey The secret key used to access the BLIP API.
     */
    public Blip(String apiKey, String secretKey) {
        this(apiKey, secretKey, "https://blip.balihoo-cloud.com");
    }

    /**
     * Blip constructor.
     * @param apiKey The key used to access the BLIP API.
     * @param secretKey The secret key used to access the BLIP API.
     * @param endpoint The BLIP endpoint to target.
     */
    public Blip(String apiKey, String secretKey, String endpoint) {
        API_KEY = apiKey;
        SECRET_KEY = secretKey;
        ENDPOINT = endpoint;
    }

    /**
     * Ping the BLIP API.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse Ping() throws IOException {
        BlipRequest request = new BlipRequest(API_KEY, SECRET_KEY, ENDPOINT);

        return request.ExecuteCommand(BlipRequest.Command.GET, "/ping", null);
    }

    /**
     * Get a list of brandKeys that the API user is authorized to access.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse GetBrandKeys() throws IOException {
        BlipRequest request = new BlipRequest(API_KEY, SECRET_KEY, ENDPOINT);

        return request.ExecuteCommand(BlipRequest.Command.GET, "/brand", null);
    }

    /**
     * Get a list of data sources available for an individual brand.
     * @param brandKey The unique identifier for a single brand.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse GetBrandSources(String brandKey) throws IOException {
        String path = String.format("/brand/%s/source", brandKey);
        BlipRequest request = new BlipRequest(API_KEY, SECRET_KEY, ENDPOINT);

        return request.ExecuteCommand(BlipRequest.Command.GET, path, null);
    }

    /**
     * Get a list of data projections available for an individual brand.
     * @param brandKey The unique identifier for a single brand.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse GetBrandProjections(String brandKey) throws IOException {
        String path = String.format("/brand/%s/projection", brandKey);
        BlipRequest request = new BlipRequest(API_KEY, SECRET_KEY, ENDPOINT);

        return request.ExecuteCommand(BlipRequest.Command.GET, path, null);
    }

    /**
     * Get a list of locationKeys for all of a brand's locations using the universal projection.
     * @param brandKey The unique identifier for a single brand.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse GetLocationKeys(String brandKey) throws IOException {
        return GetLocationKeys(brandKey, "universal");
    }

    /**
     * Get a list of locationKeys for all of a brand's locations.
     * @param brandKey The unique identifier for a single brand.
     * @param projection The data projection on which to filter results.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse GetLocationKeys(String brandKey, String projection) throws IOException {
        String path = String.format("/brand/%s/location?projection=%s", brandKey, projection);
        BlipRequest request = new BlipRequest(API_KEY, SECRET_KEY, ENDPOINT);

        return request.ExecuteCommand(BlipRequest.Command.GET, path, null);
    }

    /**
     * Get data for an individual location using the universal data projection, not including referenced objects.
     * @param brandKey The unique identifier for a single brand.
     * @param locationKey The unique identifier for a single location within the brand.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse GetLocation(String brandKey, String locationKey) throws IOException {
        return GetLocation(brandKey, locationKey, "universal", false);
    }

    /**
     * Get data for an individual location, not including referenced objects.
     * @param brandKey The unique identifier for a single brand.
     * @param locationKey The unique identifier for a single location within the brand.
     * @param projection The data projection on which to filter results.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse GetLocation(String brandKey, String locationKey, String projection) throws IOException {
        return GetLocation(brandKey, locationKey, projection, false);
    }

    /**
     * Get data for an individual location using the universal data projection.
     * @param brandKey The unique identifier for a single brand.
     * @param locationKey The unique identifier for a single location within the brand.
     * @param includeRefs Whether or not to include objects referenced by the location in its data.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse GetLocation(String brandKey, String locationKey, Boolean includeRefs) throws IOException {
        return GetLocation(brandKey, locationKey, "universal", includeRefs);
    }

    /**
     * Get data for an individual location.
     * @param brandKey The unique identifier for a single brand.
     * @param locationKey The unique identifier for a single location within the brand.
     * @param projection The data projection on which to filter results.
     * @param includeRefs Whether or not to include objects referenced by the location in its data.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse GetLocation(String brandKey, String locationKey,
                                    String projection, Boolean includeRefs) throws IOException {
        String path = String.format("/brand/%s/location/%s?projection=%s&includeRefs=%s",
                                    brandKey, locationKey, projection, includeRefs.toString().toLowerCase());
        BlipRequest request = new BlipRequest(API_KEY, SECRET_KEY, ENDPOINT);

        return request.ExecuteCommand(BlipRequest.Command.GET, path, null);
    }

    /**
     * Get data for locations in a single brand that match the specified BLIP query.
     * @param brandKey The unique identifier for a single brand.
     * @param query A stringified JSON query used to filter locations in BLIP.
     * @param view The name of the view to return if known.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse QueryLocations(String brandKey, String query, String view) throws IOException {
        String path = String.format("/brand/%s/locationList", brandKey);
        String queryParam = String.format("{\"query\":%s,\"view\":\"%s\"}", query, view);
        BlipRequest request = new BlipRequest(API_KEY, SECRET_KEY, ENDPOINT);

        return request.ExecuteCommand(BlipRequest.Command.POST, path, queryParam);
    }

    /**
     * Add or update a location.
     * @param brandKey The unique identifier for a single brand.
     * @param locationKey The unique identifier for a single location within the brand.
     * @param source The unique identifier for the data source being used to add/update the location.
     * @param locationData The stringified JSON location document.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse PutLocation(String brandKey, String locationKey,
                                    String source, String locationData) throws IOException {
        String path = String.format("/brand/%s/location/%s?source=%s", brandKey, locationKey, source);
        BlipRequest request = new BlipRequest(API_KEY, SECRET_KEY, ENDPOINT);

        return request.ExecuteCommand(BlipRequest.Command.PUT, path, locationData);
    }

    /**
     * Delete a location.
     * @param brandKey The unique identifier for a single brand.
     * @param locationKey The unique identifier for a single location within the brand.
     * @param source The unique identifier for the data source being used to add/update the location.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse DeleteLocation(String brandKey, String locationKey, String source) throws IOException {
        String path = String.format("/brand/%s/location/%s?source=%s", brandKey, locationKey, source);
        BlipRequest request = new BlipRequest(API_KEY, SECRET_KEY, ENDPOINT);

        return request.ExecuteCommand(BlipRequest.Command.DELETE, path, null);
    }
}
