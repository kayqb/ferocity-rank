package com.ferocityrank;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.ferocityrank.models.FerocityRankData;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;

import java.awt.*;

public class FerocityRankPanel extends PluginPanel {
    private final FerocityRankPlugin plugin;

    private JLabel questPointsLabel = new JLabel(htmlLabel("Quest Points: ", "0"));
    private JLabel totalLevelLabel = new JLabel(htmlLabel("Total Level: ", "0"));
    private JLabel combatLevelLabel = new JLabel(htmlLabel("Combat Level: ", "0"));
    private JLabel achievementsCountLabel = new JLabel(htmlLabel("Diary Points: ", "0"));
    private JLabel combatTasksCountLabel = new JLabel(htmlLabel("Combat Tasks: ", "0"));
    private JLabel collectionLogCountLabel = new JLabel(htmlLabel("Collections Logged: ", "0"));

    private final JButton calculateButton = new JButton("Calculate Rank");
    private final JButton resetButton = new JButton("Reset");

    @Inject
    public FerocityRankPanel(FerocityRankPlugin plugin) {
        this.plugin = plugin;

        JPanel panel = new JPanel();
        panel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        panel.setBorder(new EmptyBorder(10, 25, 10, 25));
        panel.setLayout(new GridLayout(0, 1));

        calculateButton.addActionListener(e -> plugin.calculateAndDisplayRank());
        resetButton.addActionListener(e -> plugin.resetValues());

        add(questPointsLabel);
        add(totalLevelLabel);
        add(combatLevelLabel);
        add(achievementsCountLabel);
        add(combatTasksCountLabel);
        add(collectionLogCountLabel);
        add(Box.createVerticalStrut(150));
        add(calculateButton);
        add(resetButton);
    }

    public void updateRank(FerocityRankData rankData) {
        questPointsLabel.setText(htmlLabel("Quest Points: ", String.valueOf(rankData.getQuestPoints())));
        totalLevelLabel.setText(htmlLabel("Total Level: ", String.valueOf(rankData.getTotalLevel())));
        combatLevelLabel.setText(htmlLabel("Combat Level: ", String.valueOf(rankData.getCombatLevel())));
        achievementsCountLabel.setText(htmlLabel("Diary Points: ", String.valueOf(rankData.getAchievementsCount())));
        combatTasksCountLabel.setText(htmlLabel("Combat Tasks: ", String.valueOf(rankData.getCombatTasksCount())));
        collectionLogCountLabel.setText(htmlLabel("Collections Logged: ", String.valueOf(rankData.getCollectionLogCount())));
    }

    private static String htmlLabel(String key, String value) {
        return "<html><body style = 'color:#a5a5a5'>" + key + "<span style = 'color:white'>" + value + "</span></body></html>";
    }
}
