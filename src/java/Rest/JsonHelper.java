package Rest;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mathias on 27/04/16.
 */
public class JsonHelper {

    private StringWriter stringWriter = new StringWriter();
    private JsonWriter jsonWriter = null;

    /**
     * Constructor sets up the json writer with parameters
     */
    public JsonHelper(){
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);

        JsonWriterFactory factory = Json.createWriterFactory(properties);
        jsonWriter = factory.createWriter(stringWriter);
    }

    /**
     *
     * @param jsonArray to be converted to a String
     * @return String formatted as a json array
     */
    public String jsonArrayToString(JsonArray jsonArray) {
        jsonWriter.writeArray(jsonArray);

        String output = stringWriter.toString();

        return output;
    }

    /**
     *
     * @param jsonObject to be converted to a String
     * @return String formatted as a json object
     */
    public String jsonObjectToString(JsonObject jsonObject){
        jsonWriter.writeObject(jsonObject);

        String output = stringWriter.toString();

        return output;
    }
}
