package com.balihoo.sdk;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;
import java.io.*;
import java.util.Arrays;
import java.util.List;

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
    public BlipResponse ping() throws IOException {
        BlipRequest request = new BlipRequest(API_KEY, SECRET_KEY, ENDPOINT);

        return request.executeCommand(BlipRequest.Command.GET, "/ping", null);
    }

    /**
     * Get a list of brandKeys that the API user is authorized to access.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse getBrandKeys() throws IOException {
        BlipRequest request = new BlipRequest(API_KEY, SECRET_KEY, ENDPOINT);

        return request.executeCommand(BlipRequest.Command.GET, "/brand", null);
    }

    /**
     * Get a list of data sources available for an individual brand.
     * @param brandKey The unique identifier for a single brand.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse getBrandSources(String brandKey) throws IOException {
        String path = String.format("/brand/%s/source", brandKey);
        BlipRequest request = new BlipRequest(API_KEY, SECRET_KEY, ENDPOINT);

        return request.executeCommand(BlipRequest.Command.GET, path, null);
    }

    /**
     * Get a list of data projections available for an individual brand.
     * @param brandKey The unique identifier for a single brand.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse getBrandProjections(String brandKey) throws IOException {
        String path = String.format("/brand/%s/projection", brandKey);
        BlipRequest request = new BlipRequest(API_KEY, SECRET_KEY, ENDPOINT);

        return request.executeCommand(BlipRequest.Command.GET, path, null);
    }

    /**
     * Get a list of locationKeys for all of a brand's locations using the universal projection.
     * @param brandKey The unique identifier for a single brand.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse getLocationKeys(String brandKey) throws IOException {
        return getLocationKeys(brandKey, "universal");
    }

    /**
     * Get a list of locationKeys for all of a brand's locations.
     * @param brandKey The unique identifier for a single brand.
     * @param projection The data projection on which to filter results.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse getLocationKeys(String brandKey, String projection) throws IOException {
        String path = String.format("/brand/%s/location?projection=%s", brandKey, projection);
        BlipRequest request = new BlipRequest(API_KEY, SECRET_KEY, ENDPOINT);

        return request.executeCommand(BlipRequest.Command.GET, path, null);
    }

    /**
     * Get data for an individual location using the universal data projection, not including referenced objects.
     * @param brandKey The unique identifier for a single brand.
     * @param locationKey The unique identifier for a single location within the brand.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse getLocation(String brandKey, String locationKey) throws IOException {
        return getLocation(brandKey, locationKey, "universal", false);
    }

    /**
     * Get data for an individual location, not including referenced objects.
     * @param brandKey The unique identifier for a single brand.
     * @param locationKey The unique identifier for a single location within the brand.
     * @param projection The data projection on which to filter results.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse getLocation(String brandKey, String locationKey, String projection) throws IOException {
        return getLocation(brandKey, locationKey, projection, false);
    }

    /**
     * Get data for an individual location using the universal data projection.
     * @param brandKey The unique identifier for a single brand.
     * @param locationKey The unique identifier for a single location within the brand.
     * @param includeRefs Whether or not to include objects referenced by the location in its data.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse getLocation(String brandKey, String locationKey, Boolean includeRefs) throws IOException {
        return getLocation(brandKey, locationKey, "universal", includeRefs);
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
    public BlipResponse getLocation(String brandKey, String locationKey,
                                    String projection, Boolean includeRefs) throws IOException {
        String path = String.format("/brand/%s/location/%s?projection=%s&includeRefs=%s",
                                    brandKey, locationKey, projection, includeRefs.toString().toLowerCase());
        BlipRequest request = new BlipRequest(API_KEY, SECRET_KEY, ENDPOINT);

        return request.executeCommand(BlipRequest.Command.GET, path, null);
    }

    /**
     * Get data for locations in a single brand that match the specified BLIP query.
     * @param brandKey The unique identifier for a single brand.
     * @param query A stringified JSON query used to filter locations in BLIP.
     * @param view The name of the view to return if known.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse queryLocations(String brandKey, String query, String view) throws IOException {
        String path = String.format("/brand/%s/locationList", brandKey);
        String queryParam = String.format("{\"query\":%s,\"view\":\"%s\"}", query, view);
        BlipRequest request = new BlipRequest(API_KEY, SECRET_KEY, ENDPOINT);

        return request.executeCommand(BlipRequest.Command.POST, path, queryParam);
    }

    /**
     * Get data for locations in a single brand that match the specified BLIP query.
     * @param brandKey The unique identifier for a single brand.
     * @param query A stringified JSON query used to filter locations in BLIP.
     * @param view The name of the view to return if known.
     * @param pageSize The number of results to include in each page of results.
     * @param pageNumber The page index to return.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse queryLocations(String brandKey, String query, String view,
                                       Integer pageSize, Integer pageNumber) throws IOException {
        String path = String.format("/brand/%s/locationList", brandKey);
        String queryParam = String.format("{\"query\":%s,\"view\":\"%s\",\"pageSize\":%s,\"pageNumber\":%s}",
                query, view, pageSize, pageNumber);
        BlipRequest request = new BlipRequest(API_KEY, SECRET_KEY, ENDPOINT);

        return request.executeCommand(BlipRequest.Command.POST, path, queryParam);
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
    public BlipResponse putLocation(String brandKey, String locationKey,
                                    String source, String locationData) throws IOException {
        String path = String.format("/brand/%s/location/%s?source=%s", brandKey, locationKey, source);
        BlipRequest request = new BlipRequest(API_KEY, SECRET_KEY, ENDPOINT);

        return request.executeCommand(BlipRequest.Command.PUT, path, locationData);
    }

    /**
     * Delete a location.
     * @param brandKey The unique identifier for a single brand.
     * @param locationKey The unique identifier for a single location within the brand.
     * @param source The unique identifier for the data source being used to add/update the location.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if response cannot be parsed.
     */
    public BlipResponse deleteLocation(String brandKey, String locationKey, String source) throws IOException {
        String path = String.format("/brand/%s/location/%s?source=%s", brandKey, locationKey, source);
        BlipRequest request = new BlipRequest(API_KEY, SECRET_KEY, ENDPOINT);

        return request.executeCommand(BlipRequest.Command.DELETE, path, null);
    }

    /**
     * Load a bulk location file into BLIP.
     * @param brandKey The unique identifier for a single brand.
     * @param source The unique identifier for the data source.
     * @param filePath The full path to the bulk location file.
     * @param implicitDelete Whether or not to delete locations from BLIP if they're missing from the file.
     * @param expectedRecordCount The number of location records to expect in the file.
     * @param successEmail An optional email address to notify upon success. Can be a comma-delimited list.
     * @param failEmail An optional email address to notify upon failure. Can be a comma-delimited list.
     * @param successCallbackUrl An optional URL to call upon success.
     * @param failCallbackUrl An optional URL to call upon failure.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if the bulk load cannot be initiated.
     */
    public BlipResponse bulkLoad(String brandKey, String source, String filePath, Boolean implicitDelete,
                                 int expectedRecordCount, String successEmail, String failEmail,
                                 String successCallbackUrl, String failCallbackUrl) throws IOException {
        // Use pre-signed auth from BLIP to upload the file to S3
        BlipRequest blipRequest = new BlipRequest(API_KEY, SECRET_KEY, ENDPOINT);
        BlipResponse s3UploadResponse = new S3Request().upload(blipRequest, brandKey, filePath);

        // Return error response if S3 upload fails.
        if (s3UploadResponse.STATUS_CODE != 204) {
            return s3UploadResponse;
        }

        String path = String.format("/brand/%s/bulkLoad?", brandKey);
        path += String.format("s3Path=%s&source=%s&implicitDelete=%s&expectedRecordCount=%s",
                s3UploadResponse.BODY, source, implicitDelete, expectedRecordCount);

        // Validate and add optional params
        if (successEmail != null && !successEmail.isEmpty()) {
            if (isValidEmail(successEmail)) {
                path += "&successEmail=" + successEmail;
            } else {
                return new BlipResponse(400, "Error: successEmail is not valid. " + successEmail);
            }
        }
        if (failEmail != null && !failEmail.isEmpty()) {
            if (isValidEmail(failEmail)) {
                path += "&failEmail=" + failEmail;
            } else {
                return new BlipResponse(400, "Error: failEmail is not valid. " + failEmail);
            }
        }
        if (successCallbackUrl != null && !successCallbackUrl.isEmpty()) {
            if (isValidUrl(successCallbackUrl)) {
                path += "&successCallback=" + successCallbackUrl;
            } else {
                return new BlipResponse(400, "Error: successCallbackUrl is not valid. " + successCallbackUrl);
            }
        }
        if (failCallbackUrl != null && !failCallbackUrl.isEmpty()) {
            if (isValidUrl(failCallbackUrl)) {
                path += "&failCallback=" + failCallbackUrl;
            } else {
                return new BlipResponse(400, "Error: failCallbackUrl is not valid. " + failCallbackUrl);
            }
        }

        // Ask BLIP to load the file from S3 and return its response (success of failure)
        return blipRequest.executeCommand(BlipRequest.Command.GET, path, null);
    }

    /**
     * Validate email address or comma delimited list of email addresses.
     * @param email The email address(es) to validate.
     * @return Boolean validation response.
     */
    private Boolean isValidEmail(String email) {
        List<String> emailList = Arrays.asList(email.split(","));
        for (String e : emailList) {
            Boolean valid = EmailValidator.getInstance().isValid(e);
            if (!valid) { return false; }
        }
        return true; // no invalid emails found
    }

    /**
     * Validate URL
     * @param url The URL to validate.
     * @return Boolean validation response.
     */
    private Boolean isValidUrl(String url) {
        String[] schemes = {"http", "https"}; // Exclude ftp which is in the default schemes
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(url);
    }
}
