package com.ferocityrank;

import com.ferocityrank.models.CharacterSummaryData;
import com.ferocityrank.models.FerocityRankData;
import com.google.inject.Provides;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
        name = "Ferocity Rank Plugin",
        description = "Displays total level and quest points in the side panel.",
        tags = {"rank", "total level", "quest points"}
)
public class FerocityRankPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private ClientThread clientThread;

    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private FerocityRankConfig config;

    private FerocityRankPanel panel;

    private NavigationButton navButton;

    @Provides
    FerocityRankConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(FerocityRankConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        panel = injector.getInstance(FerocityRankPanel.class);

        final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "sidebar.png");

        navButton = NavigationButton.builder()
                .tooltip("Ferocity Rank")
                .icon(icon)
                .priority(5)
                .panel(panel)
                .build();

        clientToolbar.addNavigation(navButton);
        log.debug("Ferocity Rank Plugin started!");
    }

    @Override
    protected void shutDown() throws Exception {
        clientToolbar.removeNavigation(navButton);
        log.debug("Ferocity Rank Plugin stopped!");
    }

    public void calculateAndDisplayRank() {
        clientThread.invokeLater(() -> {
                    final int totalLevel = client.getTotalLevel();
                    final int questPoints = client.getVarpValue(VarPlayer.QUEST_POINTS);
                    final int combatLevel = client.getLocalPlayer().getCombatLevel();


                    final var characterSummaryData = scrapeCharacterSummaryTab();
                    FerocityRankData rankData = new FerocityRankData(
                            questPoints,
                            totalLevel,
                            combatLevel,
                            characterSummaryData.getAchievements(),
                            characterSummaryData.getCombatTasks(),
                            characterSummaryData.getCollections()
                    );

                    panel.updateRank(rankData);
                }
        );
    }

    public void resetValues() {
        final var blankRankData = new FerocityRankData(0,0,0,0,0,0);
        panel.updateRank(blankRankData);
    }

    private CharacterSummaryData scrapeCharacterSummaryTab() {

//        final var widget = Objects.requireNonNull(client.getWidget(712, 2)).getChild(11);
        final var widget = client.getWidget(ComponentID.CHARACTER_SUMMARY_CONTAINER);
        if (widget == null) {
            return new CharacterSummaryData(0,0,0);
        }

        final var achievementsCount = extractAndSanitiseWidgetText(widget, 62);
        final var combatTasksCount = extractAndSanitiseWidgetText(widget, 75);
        final var collectionsCount = extractAndSanitiseWidgetText(widget, 88);

        return new CharacterSummaryData(achievementsCount, combatTasksCount, collectionsCount);
    }

    private int extractAndSanitiseWidgetText(Widget widget, int childIndex) {
        return Optional.ofNullable(widget.getChild(childIndex))
                .map(Widget::getText)
                .map(this::sanitiseWidgetString)
                .orElse(0);
    }

    private int sanitiseWidgetString(String input) throws IllegalArgumentException {
        String patternString = "<col=[^>]*>(\\d+)(?=/)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            throw new IllegalArgumentException("Input string does not match the expected pattern");
        }
    }
}
