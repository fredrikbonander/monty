package com.trustly.monty.manager;

import com.trustly.monty.dto.BoxContent;
import com.trustly.monty.dto.CreateGameRequest;
import com.trustly.monty.dto.Game;
import com.trustly.monty.dto.SelectBoxRequest;
import com.trustly.monty.store.GameStore;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.stream.IntStream;

@SpringBootTest
public class GameManagerTests {
    private GameManager gameManager;

    @BeforeEach
    public void init() {
        var store = new GameStore();
        this.gameManager = new GameManager(store);
    }

    @Test
    public void shouldCreateAGame() {
        var createGameRequest = new CreateGameRequest();
        createGameRequest.setBoxes(20);
        var game = this.gameManager.createGame(createGameRequest);

        Assert.assertEquals(20, game.getBoxes().size());
    }

    @Test
    public void shouldFetchStoredGame() {
        var createGameRequest = new CreateGameRequest();
        createGameRequest.setBoxes(10);
        var game = this.gameManager.createGame(createGameRequest);
        var fetched = this.gameManager.getById(game.getId());
        Assert.assertEquals(game.getId(), fetched.getId());
    }

    @Test
    public void shouldSelectBox() {
        var createGameRequest = new CreateGameRequest();
        createGameRequest.setBoxes(10);
        var game = this.gameManager.createGame(createGameRequest);

        var selectRequest = new SelectBoxRequest();
        selectRequest.setGameId(game.getId());
        selectRequest.setSelectedBoxIndex(7);
        this.gameManager.selectBox(selectRequest);

        var fetched = this.gameManager.getById(game.getId());
        Assert.assertEquals(9, fetched.getBoxes().size());
    }

    @Test
    public void shouldWinMoney() {
        var createGameRequest = new CreateGameRequest();
        createGameRequest.setBoxes(10);
        var game = this.gameManager.createGame(createGameRequest);

        int index = IntStream.range(0, game.getBoxes().size())
                .filter(i -> game.getBoxes().get(i).getContent().equals(BoxContent.MONEY))
                .findFirst()
                .orElse(-1);

        var selectRequest = new SelectBoxRequest();
        selectRequest.setGameId(game.getId());
        selectRequest.setSelectedBoxIndex(index);
        this.gameManager.selectBox(selectRequest);

        var fetched = this.gameManager.getById(game.getId());
        Assert.assertEquals(BoxContent.MONEY, fetched.getSelectedBox().getContent());
    }
    
    @Test
    public void shouldDemonstrateAGame() {
        var roundes = 1000;
        var boxes = 3;
        var wins = 0;
        var winsSwitching = 0;

        var rand = new Random();

        System.out.println("Let the games begin!");
        System.out.println("Playing " + roundes + " rounds with " + boxes + " in each round");

        for (int i = 0; i < roundes; i++) {
            var game = createGameAndSelectRandomBox(boxes, rand);
            var game2 = createGameAndSelectRandomBox(boxes, rand);

            this.gameManager.removeNonWinningBox(game.getId());
            game2 = this.gameManager.removeNonWinningBox(game2.getId());

            if (game.getSelectedBox().getContent().equals(BoxContent.MONEY)) {
                wins++;
            }

            var selectRequest = new SelectBoxRequest();
            selectRequest.setGameId(game2.getId());
            selectRequest.setSelectedBoxIndex(rand.nextInt(game2.getBoxes().size()));
            game2 = this.gameManager.selectBox(selectRequest);

            if (game2.getSelectedBox().getContent().equals(BoxContent.MONEY)) {
                winsSwitching++;
            }
        }

        System.out.println("Game ended with " + wins + " wins.");
        System.out.println("Game with switching ended with " + winsSwitching + " wins.");
    }

    private Game createGameAndSelectRandomBox(int boxes, Random rand) {
        var createGameRequest = new CreateGameRequest();
        createGameRequest.setBoxes(boxes);
        var game = this.gameManager.createGame(createGameRequest);

        var selectRequest = new SelectBoxRequest();
        selectRequest.setGameId(game.getId());
        selectRequest.setSelectedBoxIndex(rand.nextInt(boxes));
        game = this.gameManager.selectBox(selectRequest);
        return game;
    }
}
