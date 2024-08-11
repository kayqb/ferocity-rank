package com.ferocityrank;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class FerocityRankPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(FerocityRankPlugin.class);
		RuneLite.main(args);
	}
}