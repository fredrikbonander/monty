package com.trustly.monty.manager;


import com.trustly.monty.dto.*;
import com.trustly.monty.store.GameStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
public class GameManager {
    private final GameStore store;

    public GameManager(GameStore store) {
        this.store = store;
    }

    public Game createGame(CreateGameRequest createGameRequest) {
        var boxes = this.generateBoxes(createGameRequest.getBoxes());
        return store.createGame(boxes);
    }

    public Game getById(UUID id) {
        return store.getById(id);
    }

    public Game selectBox(SelectBoxRequest selectRequest) {
        var game = this.getById(selectRequest.getGameId());
        game.setSelectedBox(game.getBoxes().get(selectRequest.getSelectedBoxIndex()));
        game.getBoxes().remove(selectRequest.getSelectedBoxIndex());
        return store.updateGame(game);
    }

    public Game removeNonWinningBox(UUID gameId) {
        var game = this.getById(gameId);
        if (game.getBoxes().get(0).getContent().equals(BoxContent.MONEY)) {
            game.getBoxes().remove(1);
        } else {
            game.getBoxes().remove(0);
        }

        return store.updateGame(game);
    }

    private List<Box> generateBoxes(int boxesCount) {
        var boxes = new ArrayList<Box>();
        var money = new Random().nextInt(boxesCount);
        for (var i = 0; i < boxesCount; i++) {
            var box = new Box();
            var content = i == money ? BoxContent.MONEY : BoxContent.NOTHING;
            box.setContent(content);
            boxes.add(box);
        }

        return boxes;
    }
}
