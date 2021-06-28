package com.trustly.monty.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class SelectBoxRequest {
    private UUID gameId;
    private int selectedBoxIndex;
}
