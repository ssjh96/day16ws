package sg.edu.nus.iss.day16ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.JsonObject;
import redis.clients.jedis.annots.Internal;
import sg.edu.nus.iss.day16ws.service.BoardgameService;

@RestController
@RequestMapping(path = "/api/boardgame", produces = "application/json")
public class BoardgameRestController 
{
    @Autowired
    private BoardgameService boardgameService;

    @PostMapping(consumes = "application.json")
    public ResponseEntity<JsonObject> insertBoardgame(@RequestBody String payload)
    {
        JsonObject response = boardgameService.addBoardgame(payload);

        // .ok() is shortcut for HttpStatus.OK which sets the HTTP status to 200 OK
        // 201 Created: Use .status(HttpStatus.CREATED) for successful resource creation.
        // 404 Not Found: Use .status(HttpStatus.NOT_FOUND) for missing resources.
        // 400 Bad Request: Use .status(HttpStatus.BAD_REQUEST) for invalid inputs.
        // 500 Internal Server Error: Use .status(HttpStatus.INTERNAL_SERVER_ERROR) for server issues.
        
        return ResponseEntity.status(201).body(response);
    }
    
}
