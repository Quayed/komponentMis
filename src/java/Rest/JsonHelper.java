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


    public JsonHelper(){
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);

        JsonWriterFactory factory = Json.createWriterFactory(properties);
        jsonWriter = factory.createWriter(stringWriter);
    }

    public String jsonArrayToString(JsonArray jsonArray) {
        jsonWriter.writeArray(jsonArray);

        String output = stringWriter.toString();

        return output;
    }

    public String jsonObjectToString(JsonObject jsonObject){
        jsonWriter.writeObject(jsonObject);

        String output = stringWriter.toString();

        return output;
    }
}
