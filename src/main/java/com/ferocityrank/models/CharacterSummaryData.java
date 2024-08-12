package com.ferocityrank.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CharacterSummaryData {
    private final int achievements;
    private final int combatTasks;
    private final int collections;
}
