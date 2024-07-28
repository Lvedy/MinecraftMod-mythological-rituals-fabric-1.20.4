package lvedy.mr;

import lvedy.mr.registry.*;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MythologicalRituals implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "mr";
    public static final Logger LOGGER = LoggerFactory.getLogger("mythological-rituals");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModEntity.main_registerEntity();
		ModItems.main_registerItem();
		ModBlock.main_registerBlock();
		ModItemGroup.main_registerItemGroup();
		ModStatusEffect.main_registerStatusEffect();
		ModEvent.main_registerEvent();
		ModSound.main_registerSound();
		LOGGER.info("Hello Fabric world!");
	}
}