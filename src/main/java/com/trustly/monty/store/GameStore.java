package com.trustly.monty.store;

import com.trustly.monty.dto.Box;
import com.trustly.monty.dto.Game;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GameStore {
    private final Map<UUID, GameDao> games = new HashMap<>();

    public Game createGame(List<Box> boxes) {
        var boxList = this.mapBoxes(boxes);
        var gameDao = new GameDao(UUID.randomUUID(), boxList, null);
        this.games.putIfAbsent(gameDao.getId(), gameDao);
        return gameDao.toDto();
    }

    public Game getById(UUID id) {
        var gameDao = games.get(id);
        if (gameDao == null) {
            return null;
        }

        return gameDao.toDto();
    }

    public Game updateGame(Game game) {
        var boxList = this.mapBoxes(game.getBoxes());
        var gameDao = new GameDao(game.getId(), boxList, this.mapBox(game.getSelectedBox()));
        games.put(gameDao.getId(), gameDao);
        return gameDao.toDto();
    }

    private List<BoxDao> mapBoxes(List<Box> boxes) {
        return boxes.stream().map(this::mapBox).collect(Collectors.toList());
    }

    private BoxDao mapBox(Box box) {
        return new BoxDao(box.getContent());
    }
}
