package com.balihoo.sdk;

import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.*;

class S3Request {

    protected BlipResponse UploadFile(String s3Path, JsonObject formData, String mimeType, byte[] compressedFile) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost(s3Path);
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

        // Get response body if the request is not successful
        String body = "";
        if (statusCode < 200 || statusCode > 299) {
            HttpEntity responseEntity = response.getEntity();
            body = EntityUtils.toString(responseEntity, "UTF-8");
        }

        return new BlipResponse(statusCode, body);
    }
}
