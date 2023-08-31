package org.cariq;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Map;

public class APIPets {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String BASE_URL = "http://localhost:8080/pet";
    private HttpUrl.Builder builder;


    public APIPets(String directory) {
        builder = HttpUrl.parse(BASE_URL).newBuilder();
        builder.addPathSegment(directory);
    }

    public APIPets() {
        builder = HttpUrl.parse(BASE_URL).newBuilder();
    }

    public APIPets addParameters(Map<String, String> parameters) {
        for (var set : parameters.entrySet()) {
            builder.addQueryParameter(set.getKey(), set.getValue());
        }
        return this;
    }

    public Request get() {
        return new Request.Builder()
                .url(builder.build())
                .get()
                .build();
    }

    public Request post(String body) {
        return new Request.Builder()
                .url(builder.build())
                .post(RequestBody.create(body, JSON))
                .build();
    }

    public Request put(String body) {
        return new Request.Builder()
                .url(builder.build())
                .put(RequestBody.create(body, JSON))
                .build();
    }
}
