package org.cariq.dp;

import org.testng.annotations.DataProvider;
import static org.cariq.TestUtils.buildMap;
import static org.cariq.TestUtils.dataFetcher;

import java.io.IOException;


public class DataProviders {

    @DataProvider(name = "getPetData")
    public Object[][] getPetData() {
        return new Object[][]{
                {buildMap("status=sold"), 200, "findByStatus"},
                {buildMap("status=test"), 400, "findByStatus"}
        };
    }

    @DataProvider(name = "postPetData")
    public Object[][] postPetData() throws IOException {
        return new Object[][]{
                {dataFetcher("pets/BasePet"), 201, "./src/test/resources/response-schema.json"}
        };
    }

    @DataProvider(name = "putPetData")
    public Object[][] putPetData() throws IOException {
        return new Object[][]{
                {dataFetcher("pets/BasePet"), 200, "./src/test/resources/response-schema.json"}
        };
    }
}
