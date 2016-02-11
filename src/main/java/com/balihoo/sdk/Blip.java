package com.balihoo.sdk;

/**
 * The BLIP object and its methods.
 */
public class Blip {

    private String Credentials;
    private String Endpoint;

    /**
     * Blip constructor with default production endpoint.
     * @param apiKey The key used to access the BLIP API.
     * @param secretKey The secret key used to access the BLIP API.
     */
    public Blip(String apiKey, String secretKey) {
        this.Credentials = encodeCredentials(apiKey, secretKey);
        this.Endpoint = "https://blip.dev.balihoo-cloud.com";
    }

    /**
     * Blip constructor.
     * @param apiKey The key used to access the BLIP API.
     * @param secretKey The secret key used to access the BLIP API.
     * @param endpoint The BLIP endpoint to target.
     */
    public Blip(String apiKey, String secretKey, String endpoint) {
        this.Credentials = encodeCredentials(apiKey, secretKey);
        this.Endpoint = endpoint;
    }

    /**
     * Ping the BLIP API.
     * @return BlipResponse object with a status code and body text if applicable.
     */
    public BlipResponse Ping() {
        BlipRequest request = new BlipRequest(this.Credentials, this.Endpoint);

        return request.ExecuteCommand(BlipRequest.Command.Get, "/ping", null);
    }

    /**
     * Get a list of brandKeys that the API user is authorized to access.
     * @return BlipResponse object with a status code and body text if applicable.
     */
    public BlipResponse GetBrandKeys() {
        BlipRequest request = new BlipRequest(this.Credentials, this.Endpoint);

        return request.ExecuteCommand(BlipRequest.Command.Get, "/brand", null);
    }

    /**
     * Get a list of data sources available for an individual brand.
     * @param brandKey The unique identifier for a single brand.
     * @return BlipResponse object with a status code and body text if applicable.
     */
    public BlipResponse GetBrandSources(String brandKey) {
        String path = String.format("/brand/%s/source", brandKey);
        BlipRequest request = new BlipRequest(this.Credentials, this.Endpoint);

        return request.ExecuteCommand(BlipRequest.Command.Get, path, null);
    }

    /**
     * Get a list of data projections available for an individual brand.
     * @param brandKey The unique identifier for a single brand.
     * @return BlipResponse object with a status code and body text if applicable.
     */
    public BlipResponse GetBrandProjections(String brandKey) {
        String path = String.format("/brand/%s/projection", brandKey);
        BlipRequest request = new BlipRequest(this.Credentials, this.Endpoint);

        return request.ExecuteCommand(BlipRequest.Command.Get, path, null);
    }

    /**
     * Get a list of locationKeys for all of a brand's locations using the universal projection.
     * @param brandKey The unique identifier for a single brand.
     * @return BlipResponse object with a status code and body text if applicable.
     */
    public BlipResponse GetLocationKeys(String brandKey) {
        return GetLocationKeys(brandKey, "universal");
    }

    /**
     * Get a list of locationKeys for all of a brand's locations.
     * @param brandKey The unique identifier for a single brand.
     * @param projection The data projection on which to filter results.
     * @return BlipResponse object with a status code and body text if applicable.
     */
    public BlipResponse GetLocationKeys(String brandKey, String projection) {
        String path = String.format("/brand/%s/location?projection=%s", brandKey, projection);
        BlipRequest request = new BlipRequest(this.Credentials, this.Endpoint);

        return request.ExecuteCommand(BlipRequest.Command.Get, path, null);
    }

    /**
     * Get data for an individual location using the universal data projection, not including referenced objects.
     * @param brandKey The unique identifier for a single brand.
     * @param locationKey The unique identifier for a single location within the brand.
     * @return BlipResponse object with a status code and body text if applicable.
     */
    public BlipResponse GetLocation(String brandKey, String locationKey) {
        return GetLocation(brandKey, locationKey, "universal", false);
    }

    /**
     * Get data for an individual location, not including referenced objects.
     * @param brandKey The unique identifier for a single brand.
     * @param locationKey The unique identifier for a single location within the brand.
     * @param projection The data projection on which to filter results.
     * @return BlipResponse object with a status code and body text if applicable.
     */
    public BlipResponse GetLocation(String brandKey, String locationKey, String projection) {
        return GetLocation(brandKey, locationKey, projection, false);
    }

    /**
     * Get data for an individual location using the universal data projection.
     * @param brandKey The unique identifier for a single brand.
     * @param locationKey The unique identifier for a single location within the brand.
     * @param includeRefs Whether or not to include objects referenced by the location in its data.
     * @return BlipResponse object with a status code and body text if applicable.
     */
    public BlipResponse GetLocation(String brandKey, String locationKey, Boolean includeRefs) {
        return GetLocation(brandKey, locationKey, "universal", includeRefs);
    }

    /**
     * Get data for an individual location.
     * @param brandKey The unique identifier for a single brand.
     * @param locationKey The unique identifier for a single location within the brand.
     * @param projection The data projection on which to filter results.
     * @param includeRefs Whether or not to include objects referenced by the location in its data.
     * @return BlipResponse object with a status code and body text if applicable.
     */
    public BlipResponse GetLocation(String brandKey, String locationKey, String projection, Boolean includeRefs) {
        String path = String.format("/brand/%s/location/%s?projection=%s&includeRefs=%s",
                                    brandKey, locationKey, projection, includeRefs.toString().toLowerCase());
        BlipRequest request = new BlipRequest(this.Credentials, this.Endpoint);

        return request.ExecuteCommand(BlipRequest.Command.Get, path, null);
    }

    /**
     * Get data for locations in a single brand that match the specified BLIP query.
     * @param brandKey The unique identifier for a single brand.
     * @param query A stringified JSON query used to filter locations in BLIP.
     * @param view The name of the view to return if known.
     * @return BlipResponse object with a status code and body text if applicable.
     */
    public BlipResponse QueryLocations(String brandKey, String query, String view) {
        String path = String.format("/brand/%s/locationList", brandKey);
        String queryParam = String.format("{\"query\":%s,\"view\":\"%s\"}", query, view);
        BlipRequest request = new BlipRequest(this.Credentials, this.Endpoint);

        return request.ExecuteCommand(BlipRequest.Command.Post, path, queryParam);
    }

    /**
     * Add or update a location.
     * @param brandKey The unique identifier for a single brand.
     * @param locationKey The unique identifier for a single location within the brand.
     * @param source The unique identifier for the data source being used to add/update the location.
     * @param locationData The stringified JSON location document.
     * @return BlipResponse object with a status code and body text if applicable.
     */
    public BlipResponse PutLocation(String brandKey, String locationKey, String source, String locationData) {
        String path = String.format("/brand/%s/location/%s?source=%s", brandKey, locationKey, source);
        BlipRequest request = new BlipRequest(this.Credentials, this.Endpoint);

        return request.ExecuteCommand(BlipRequest.Command.Put, path, locationData);
    }

    /**
     * Delete a location.
     * @param brandKey The unique identifier for a single brand.
     * @param locationKey The unique identifier for a single location within the brand.
     * @param source The unique identifier for the data source being used to add/update the location.
     * @return BlipResponse object with a status code and body text if applicable.
     */
    public BlipResponse DeleteLocation(String brandKey, String locationKey, String source) {
        String path = String.format("/brand/%s/location/%s?source=%s", brandKey, locationKey, source);
        BlipRequest request = new BlipRequest(this.Credentials, this.Endpoint);

        return request.ExecuteCommand(BlipRequest.Command.Delete, path, null);
    }

    /**
     * Base64 encode the API keys.
     * @param apiKey The key used to access the BLIP API.
     * @param secretKey The secret key used to access the BLIP API.
     * @return Base64 encoded credentials.
     */
    private String encodeCredentials(String apiKey, String secretKey) {
        String credentials = String.format("%s:%s", apiKey, secretKey);

        return javax.xml.bind.DatatypeConverter.printBase64Binary(credentials.getBytes());
    }
}
