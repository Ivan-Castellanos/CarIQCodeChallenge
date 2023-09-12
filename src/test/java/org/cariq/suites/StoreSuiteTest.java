package org.cariq.suites;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.cariq.APIStore;
import org.cariq.BaseTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;

import static org.cariq.TestUtils.commonAssert;

public class StoreSuiteTest extends BaseTest {
    private OkHttpClient client;

    @Test
    public void getStoreByID(/*String directory*/) throws IOException {
        SoftAssert softAssert = new SoftAssert();
        APIStore apiStore = new APIStore("order/1");

        Response response = client.newCall(apiStore.get()).execute();

        commonAssert(response, softAssert, 200);
        softAssert.assertAll();
    }

    @BeforeTest
    private void storeTestSetUp() {
        client = new OkHttpClient();
        mapper = new ObjectMapper();
    }
}
