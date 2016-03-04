package com.balihoo.sdk;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPOutputStream;

class S3Request {

    /**
     * Upload a bulk location file to S3
     * @param blipRequest A credentialed BlipRequest object.
     * @param brandKey The unique identifier for a single brand.
     * @param filePath The path to the file to be uploaded.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if file cannot be processed.
     */
    protected BlipResponse upload(BlipRequest blipRequest, String brandKey, String filePath) throws IOException {
        // Compress file contents
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(new BufferedOutputStream(byteStream));
        FileInputStream in = new FileInputStream(filePath);
        try {
            byte[] buffer = new byte[1024];
            int len;

            while ((len = in.read(buffer)) > 0) {
                gzip.write(buffer, 0, len);
            }
        } finally {
            in.close();
            gzip.finish();
            gzip.flush();
            gzip.close();
        }

        // Get authorization to upload file from BLIP
        byte[] content = byteStream.toByteArray();
        String fileMD5 = generateMD5Hash(content);
        String path = String.format("/brand/%s/authorizeUpload?fileMD5=%s", brandKey, fileMD5);
        BlipResponse authResponse = blipRequest.executeCommand(BlipRequest.Command.GET, path, null);

        // Return error response if auth fails.
        if (authResponse.STATUS_CODE != 200) {
            return authResponse;
        }

        // Upload file to S3
        String mimeType = Files.probeContentType(Paths.get(filePath));
        JsonObject auth = new JsonParser().parse(authResponse.BODY).getAsJsonObject();
        JsonObject formData = auth.get("data").getAsJsonObject();
        String s3Bucket = auth.get("s3Bucket").getAsString();
        String s3Path = String.format("s3://%s/%s", s3Bucket, formData.get("key").getAsString());
        BlipResponse uploadResponse = postFile(s3Bucket, formData, mimeType, content);

        // Return error response if upload fails.
        if (uploadResponse.STATUS_CODE != 204) {
            return uploadResponse;
        }

        return new BlipResponse(uploadResponse.STATUS_CODE, s3Path);
    }

    /**
     * Create Multipart form-data HTTP Post to upload file to S3
     * @param s3Bucket The Amazon S3 bucket name.
     * @param formData JSON object containing the pre-signed URL data.
     * @param mimeType The MIME type of the file to be uploaded.
     * @param compressedFile The gzipped file contents.
     * @return BlipResponse object with a status code and body text if applicable.
     * @throws IOException if file cannot be processed.
     */
    private BlipResponse postFile(String s3Bucket, JsonObject formData, String mimeType, byte[] compressedFile) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost(String.format("https://s3.amazonaws.com/%s", s3Bucket));
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        // Add multipart/form-data params
        builder.addTextBody("acl", formData.get("acl").getAsString());
        builder.addTextBody("bucket", formData.get("bucket").getAsString());
        builder.addTextBody("key", formData.get("key").getAsString());
        builder.addTextBody("content-md5", formData.get("content-md5").getAsString());
        builder.addTextBody("policy", formData.get("policy").getAsString());
        builder.addTextBody("signature", formData.get("signature").getAsString());
        builder.addTextBody("AWSAccessKeyId", formData.get("AWSAccessKeyId").getAsString());
        builder.addTextBody("content-type", mimeType);
        builder.addBinaryBody("file", compressedFile); // file has to be the last param added
        HttpEntity multipart = builder.build();
        uploadFile.setEntity(multipart);

        CloseableHttpResponse response = httpClient.execute(uploadFile);
        int statusCode = response.getStatusLine().getStatusCode();
        String body = "";

        // Get response body if the request is not successful
        if (statusCode != 204) {
            HttpEntity responseEntity = response.getEntity();
            body = EntityUtils.toString(responseEntity, "UTF-8");
        }

        return new BlipResponse(statusCode, body);
    }

    /**
     * Generate an MD5 checksum.
     * @param content The content to evaluate.
     * @return The checksum as a String.
     */
    private String generateMD5Hash(byte[] content) {
        String fileMD5 = "";
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (md != null) {
            md.update(content);
            fileMD5 = DatatypeConverter.printHexBinary(md.digest()).toLowerCase();
        }

        return fileMD5;
    }
}
