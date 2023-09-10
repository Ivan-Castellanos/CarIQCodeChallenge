package org.cariq.suites;

import com.beust.ah.A;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.cariq.APIPets;
import org.cariq.BaseTest;
import org.cariq.TestUtils;
import org.cariq.dp.DataProviders;
import org.cariq.dto.pet.Pet;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import static org.cariq.TestUtils.*;


import java.io.File;
import java.io.IOException;
import java.util.Map;

public class PetsSuiteTest extends BaseTest {
    private OkHttpClient client;
    private ObjectMapper mapper;

    @Test(
            dataProvider = "postPetData",
            dataProviderClass = DataProviders.class,
            groups = "smoke"
    )
    public void postPet(String body, int code, String schemaDirectory) throws IOException {
        SoftAssert softAssert = new SoftAssert();
        APIPets apiPets = new APIPets();

        Response response = client.newCall(apiPets.post(body)).execute();

        commonAssertWithBody(response,softAssert,code,schemaDirectory);
        softAssert.assertAll();
    }

    @Test(dataProvider = "putPetData", dataProviderClass = DataProviders.class, groups = "regression")
    public void putPet(String body, int code, String schemaDirectory) throws IOException {
        SoftAssert softAssert = new SoftAssert();
        APIPets apiPets = new APIPets();

        Response response = client.newCall(apiPets.put(body)).execute();

        commonAssertWithBody(response,softAssert,code, schemaDirectory);
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
        commonAssertWithBody(response,softAssert,201,"./src/test/resources/response-schema.json", body);
        softAssert.assertAll();
        Assert.assertTrue(pet.getId()>0);
        System.out.println(pet.getId());
        // -----------------------------
        softAssert = new SoftAssert();
        apiPets = new APIPets(String.valueOf(pet.getId()));

        response = client.newCall(apiPets.get()).execute();
        commonAssert(response,softAssert,200);
        softAssert.assertAll();

        // -----------------------------
        softAssert = new SoftAssert();
        apiPets = new APIPets("9222968140497181000");

        response = client.newCall(apiPets.delete()).execute();
        commonAssert(response,softAssert,204);
        softAssert.assertAll();
    }

    @BeforeTest(groups = {"smoke", "regression"
    })
    public void testSetUp() {
        client = new OkHttpClient();
        mapper = new ObjectMapper();
    }
}