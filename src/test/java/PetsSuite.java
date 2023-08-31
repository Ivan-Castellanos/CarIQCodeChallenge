import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.cariq.APIPets;
import org.cariq.dao.Pet;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;

public class PetsSuite {
    private static OkHttpClient client;
    private static ObjectMapper mapper;
    private static File jsonSchemaDirectory;
    private static JsonSchema jsonSchema;
    private static TestUtils utils;


    @Test(dataProvider = "requestBody")
    public static void PostPet(String body) throws IOException {
        SoftAssert softAssert = new SoftAssert();
        APIPets apiPets = new APIPets();

        Response response = client.newCall(apiPets.post(body)).execute();

        JsonNode jsonNode = mapper.readTree(response.body().string());
        Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);

        softAssert.assertTrue(errors.isEmpty());
        softAssert.assertEquals(response.header("Content-Type"), "application/json");
        softAssert.assertEquals(response.code(), 201);
        softAssert.assertAll();
    }

    @Test(dataProvider = "requestBody")
    public static void PutPet(String body) throws IOException {
        SoftAssert softAssert = new SoftAssert();
        APIPets apiPets = new APIPets();

        Response response = client.newCall(apiPets.put(body)).execute();

        JsonNode jsonNode = mapper.readTree(response.body().string());
        Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);

        softAssert.assertTrue(errors.isEmpty());
        softAssert.assertEquals(response.header("Content-Type"), "application/json");
        softAssert.assertEquals(response.code(), 200);
        softAssert.assertAll();
    }

    @Test
    public static void GetPetByStatus() throws IOException {
        SoftAssert softAssert = new SoftAssert();
        HashMap<String, String> map = new HashMap<>();
        map.put("status", "sold");

        APIPets apiPets = new APIPets("findByStatus").addParameters(map);
        Response response = client.newCall(apiPets.get()).execute();
        Pet[] pets = mapper.readValue(response.body().string(), Pet[].class);

        softAssert.assertTrue(utils.assertOver(pets));
        softAssert.assertEquals(response.code(), 200);
        softAssert.assertEquals(response.header("Content-Type"), "application/json");
        softAssert.assertAll();
    }

    @Test
    public static void GetPetByStatus_BadParameters() throws IOException {
        SoftAssert softAssert = new SoftAssert();
        HashMap<String, String> map = new HashMap<>();
        map.put("status", "test");

        APIPets apiPets = new APIPets("findByStatus").addParameters(map);
        Response response = client.newCall(apiPets.get()).execute();

        softAssert.assertEquals(response.header("Content-Type"), "application/json");
        softAssert.assertEquals(response.code(), 400);
        softAssert.assertAll();
    }

    @BeforeTest
    public void setUp() throws IOException {
        client = new OkHttpClient();
        utils = new TestUtils();
        jsonSchemaDirectory = new File("/Users/ivancastellanossaba/Dev/CarIQ/src/test/resources/response-schema.json");
        mapper = new ObjectMapper();
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonNode schema = mapper.readTree(jsonSchemaDirectory);
        jsonSchema = factory.getSchema(schema);
    }

    @DataProvider(name = "requestBody")
    public Object[] readJsonAsString() throws IOException {
        return new Object[]{
                new String(Files.readAllBytes(Paths.get("src/test/resources/testData.json")))
        };
    }
}