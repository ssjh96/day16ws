package sg.edu.nus.iss.day16ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import redis.clients.jedis.annots.Internal;
import sg.edu.nus.iss.day16ws.service.BoardgameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping(path = "/api/boardgame", produces = "application/json")
public class BoardgameRestController 
{
    @Autowired
    private BoardgameService boardgameService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> insertBoardgame(@RequestBody String payload)
    {
        JsonObject response = boardgameService.addBoardgame(payload);

        // .ok() is shortcut for HttpStatus.OK which sets the HTTP status to 200 OK
        // 201 Created: Use .status(HttpStatus.CREATED) for successful resource creation.
        // 404 Not Found: Use .status(HttpStatus.NOT_FOUND) for missing resources.
        // 400 Bad Request: Use .status(HttpStatus.BAD_REQUEST) for invalid inputs.
        // 500 Internal Server Error: Use .status(HttpStatus.INTERNAL_SERVER_ERROR) for server issues.

        // Convert JsonObject to String for proper serialisation
        return ResponseEntity.status(201).body(response.toString());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getBoardgame(@PathVariable("id") String boardgameId) {
        JsonObject bgJsonObj = boardgameService.getBoardgame(boardgameId);

        if (bgJsonObj == null)
        {
            // return 404 Not Found with an error message object
            JsonObject error = Json.createObjectBuilder()
                .add("error", "Boardgame not found")
                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.toString());
        }

        // return 200 OK with boardgame JSON
        return ResponseEntity.status(HttpStatus.OK).body(bgJsonObj.toString());
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<String> updateBoardgame(
        @PathVariable ("id") String boardgameId, 
        @RequestBody String payload,
        @RequestParam(defaultValue = "false") boolean upsert) 
        {
            JsonObject response = boardgameService.updateBoardgame(boardgameId, payload, upsert);

            if (response == null)
            {
                // Return 400 Bad Request if boardgame x exist and upsert = false
                JsonObject error = Json.createObjectBuilder()
                .add("error", "Boardgame not found for update")
                .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.toString());
            }
        
        return ResponseEntity.ok().body(response.toString());
    }

    
    
}
