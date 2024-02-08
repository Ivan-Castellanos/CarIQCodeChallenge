package org.cariq;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import okhttp3.Response;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TestUtils {
    public static String dataFetcher(String directory) throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/test/resources/input/" + directory + ".json")));
    }

    public static void commonAssert(Response response, SoftAssert softAssert, int code) {
        softAssert.assertEquals(response.code(), code);
        softAssert.assertEquals(response.header("Content-Type"), "application/json");
    }

    public static Map buildMap(String parameter) {
        Map<String, String> paramsMap = new HashMap<>();
        String[] strings = parameter.split("&");
        for (String string :
                strings) {
            String[] entry = string.split("=");
            paramsMap.put(entry[0], entry[1]);
        }
        return paramsMap;
    }

    public static void commonAssertWithBody(Response response, SoftAssert softAssert, int code, String schemaDirectory) throws IOException {
        File jsonSchemaDirectory = new File(schemaDirectory);
        ObjectMapper mapper = new ObjectMapper();
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonNode schema = mapper.readTree(jsonSchemaDirectory);
        JsonSchema jsonSchema = factory.getSchema(schema);

        commonAssert(response, softAssert, code);

        JsonNode jsonNode = mapper.readTree(response.body().string());
        Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
        softAssert.assertTrue(errors.isEmpty());
    }

    public static void commonAssertWithBody(Response response, SoftAssert softAssert, int code, String schemaDirectory, String body) throws IOException {
        File jsonSchemaDirectory = new File(schemaDirectory);
        ObjectMapper mapper = new ObjectMapper();
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonNode schema = mapper.readTree(jsonSchemaDirectory);
        JsonSchema jsonSchema = factory.getSchema(schema);

        commonAssert(response, softAssert, code);

        JsonNode jsonNode = mapper.readTree(body);
        Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
        softAssert.assertTrue(errors.isEmpty());
    }
}
