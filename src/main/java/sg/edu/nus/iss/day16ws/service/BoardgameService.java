package sg.edu.nus.iss.day16ws.service;

import java.io.StringReader;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.day16ws.repository.ValueRepo;

@Service
public class BoardgameService {

    @Autowired
    private ValueRepo valueRepo;

    public JsonObject addBoardgame(String payload)
    {
        // Parse in the JSON Payload
        // Prepare read string as JSON; payload is a plain JSON string sent from postman
        JsonReader jReader = Json.createReader(new StringReader(payload));

        // .readObject() parses the JSON String into a JsonObject for manupulation in java, so you can .getString("name"), .getInt("year") to extract values
        JsonObject boardgame = jReader.readObject();

        // Generate unique key for the boardgame
        // prefix to help organise redis key by type
        // e.g. user:xxx , boardgame:xxx
        String key = "boardgame:" + UUID.randomUUID().toString();

        // Save the JSON string to Redis 
        valueRepo.createValue(key, boardgame.toString());

        // Create the response JSON Obj
        JsonObject response = Json.createObjectBuilder()
            .add("insert_count", 1)
            .add("id", key)
            .build();

        return response;
    }

    public JsonObject getBoardgame(String boardgameId)
    {
        // Construct the redis key
        String key = "boardgame:" + boardgameId;

        // Retrieve the JSON String from Redis
        String bgJsonString = valueRepo.getValue(key);

        if (bgJsonString == null)
        {
            return null;
        }

        JsonReader jReader = Json.createReader(new StringReader(bgJsonString));

        JsonObject bgJsonObject = jReader.readObject();
        return bgJsonObject;
    }

    public JsonObject updateBoardgame(String boardgameId, String payload, boolean upsert)
    {
        String key = "boardgame:" + boardgameId;

        // check if boardgame exist in Redis
        String existingBoardgame = valueRepo.getValue(key);

        if (existingBoardgame == null && !upsert)
        {
            // return null if boardgame does not exist and upsert is false
            return null; // null response 
        }

        // Parse the payload into a JsonObject
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject boardgame = reader.readObject();

         // Save the payload to Redis only if upsert=true or the boardgame exists
        if (upsert || existingBoardgame != null)
        {
            valueRepo.createValue(key, boardgame.toString());
        }

        // Create the response JSON object
        JsonObject response = Json.createObjectBuilder()
            .add("update_count", 1)
            .add("id", key)
            .build();

        return response;
    }
}
