package com.trustly.monty.store;

import com.trustly.monty.dto.Game;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Value
@Builder
public class GameDao {
    UUID id;
    List<BoxDao> boxes;
    BoxDao selectedBox;

    public Game toDto() {
        var game = new Game();
        game.setId(this.getId());
        if (this.getSelectedBox() != null) {
           game.setSelectedBox(this.getSelectedBox().toDto());
        }
        game.setBoxes(this.getBoxes().stream().map(BoxDao::toDto).collect(Collectors.toList()));

        return game;
    }
}
