package com.trustly.monty.resources;

import com.trustly.monty.dto.CreateGameRequest;
import com.trustly.monty.dto.Game;
import com.trustly.monty.manager.GameManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameResource {
    private final GameManager gameManager;

    public GameResource(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @PostMapping("/game")
    public Game createGame(@RequestBody() CreateGameRequest createGameRequest) {
        return this.gameManager.createGame(createGameRequest);
    }
}
