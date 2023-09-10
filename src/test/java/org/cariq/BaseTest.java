package org.cariq;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;

public class BaseTest {
    protected OkHttpClient client;
    protected ObjectMapper mapper;

    /*@BeforeSuite
    public void testSetUp() throws IOException {
        client = new OkHttpClient();
        utils = new org.cariq.TestUtils();
        mapper = new ObjectMapper();
    }*/
}
