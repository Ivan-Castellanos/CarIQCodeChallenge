package org.cariq;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Map;

public class AbstractAPIPets<T extends AbstractAPIPets> {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String BASE_URL = "http://localhost:8080";
    private HttpUrl.Builder builder;


    public AbstractAPIPets(String directory) {
        builder = HttpUrl.parse(BASE_URL).newBuilder();
        builder.addPathSegments(directory);
    }

    public AbstractAPIPets() {
        builder = HttpUrl.parse(BASE_URL).newBuilder();
    }

    public T addParameters(Map<String, String> parameters) {
        for (var set : parameters.entrySet()) {
            builder.addQueryParameter(set.getKey(), set.getValue());
        }
        return (T) this;
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

    public Request delete() {
        return new Request.Builder()
                .url(builder.build())
                .delete()
                .build();
    }
}
