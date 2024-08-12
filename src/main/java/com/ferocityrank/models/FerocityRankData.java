package com.ferocityrank.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class FerocityRankData {
    private final int questPoints;
    private final int totalLevel;
    private final int combatLevel;

    private final int achievementsCount;
    private final int combatTasksCount;
    private final int collectionLogCount;
}
