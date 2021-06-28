package com.trustly.monty.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Game {
    private UUID id;
    private List<Box> boxes;
    private Box selectedBox;
}
