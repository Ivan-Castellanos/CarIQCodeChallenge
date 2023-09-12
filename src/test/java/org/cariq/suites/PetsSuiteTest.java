package org.cariq.suites;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.cariq.APIPets;
import org.cariq.BaseTest;
import org.cariq.dp.DataProviders;
import org.cariq.dto.pet.Pet;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.util.Map;

import static org.cariq.TestUtils.*;

public class PetsSuiteTest extends BaseTest {
    private OkHttpClient client;
    private ObjectMapper mapper;

    @Test(dataProvider = "postPetData", dataProviderClass = DataProviders.class, groups = "smoke")
    public void postPet(String body, int code, String schemaDirectory) throws IOException {
        SoftAssert softAssert = new SoftAssert();
        APIPets apiPets = new APIPets();

        Response response = client.newCall(apiPets.post(body)).execute();

        commonAssertWithBody(response, softAssert, code, schemaDirectory);
        softAssert.assertAll();
    }

    @Test(dataProvider = "putPetData", dataProviderClass = DataProviders.class, groups = "regression")
    public void putPet(String body, int code, String schemaDirectory, String id) throws IOException {
        SoftAssert softAssert = new SoftAssert();
        APIPets apiPets = new APIPets(id);


        Response response = client.newCall(apiPets.put(body)).execute();
        String responseBody = response.body().string();
        Pet pet = mapper.readValue(responseBody, Pet.class);

        commonAssertWithBody(response, softAssert, code, schemaDirectory, responseBody);
        Assert.assertEquals(pet.getName(), "Max");
        softAssert.assertAll();
    }

    @Test(dataProvider = "getPetData", dataProviderClass = DataProviders.class)
    public void getPetByStatus(Map<String, String> map, int code, String directory) throws IOException {
        SoftAssert softAssert = new SoftAssert();
        APIPets apiPets = new APIPets(directory).addParameters(map);

        Response response = client.newCall(apiPets.get()).execute();
        Pet[] pets = mapper.readValue(response.body().string(), Pet[].class);

        commonAssert(response, softAssert, code);
        softAssert.assertTrue(apiPets.arePetsSold(pets));
        softAssert.assertAll();
    }

    @Test
    public void longTest() throws IOException {
        SoftAssert softAssert = new SoftAssert();
        APIPets apiPets = new APIPets();

        Response response = client.newCall(apiPets.post(dataFetcher("pets/BasePet"))).execute();
        String body = new String(response.body().string());
        Pet pet = mapper.readValue(body, Pet.class);
        commonAssertWithBody(response, softAssert, 201, "./src/test/resources/response-schema.json", body);
        softAssert.assertAll();
        Assert.assertTrue(pet.getId() > 0);

        // -----------------------------
        softAssert = new SoftAssert();
        apiPets = new APIPets(String.valueOf(pet.getId()));

        response = client.newCall(apiPets.get()).execute();
        commonAssert(response, softAssert, 200);
        softAssert.assertAll();

        // -----------------------------
        softAssert = new SoftAssert();
        apiPets = new APIPets("9222968140497181000");

        response = client.newCall(apiPets.delete()).execute();
        commonAssert(response, softAssert, 204);
        softAssert.assertAll();
    }

    @BeforeTest(groups = {"smoke", "regression"})
    public void testSetUp() {
        client = new OkHttpClient();
        mapper = new ObjectMapper();
    }
}