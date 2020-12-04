package com.hbn.outvoted.world.gen;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.config.OutvotedConfig;
import com.hbn.outvoted.entities.InfernoEntity;
import com.hbn.outvoted.entities.KrakenEntity;
import com.hbn.outvoted.init.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Outvoted.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChangeEntitySpawns {
    /**
     * Checks entities in an area to force limit spawn count
     * Probably awful practice, but this is a quick and dirty way to force 1 mob
     */
    @SubscribeEvent
    public static void checkMobs(LivingSpawnEvent.CheckSpawn event) { //
        double area = 6.0; // Value for x, y, and z expansion to check for entities; a variable in case it causes lag or something
        Entity e = event.getEntity();
        if (OutvotedConfig.COMMON.spawnkraken.get()) {
            if (e instanceof KrakenEntity) {
                if (event.getSpawnReason() == SpawnReason.NATURAL) {
                    List<Entity> entities = event.getWorld().getEntitiesWithinAABBExcludingEntity(event.getEntity(), event.getEntity().getBoundingBox().expand(area, area, area).expand(-area, -area, -area));
                    if (!entities.isEmpty()) {
                        event.setResult(Event.Result.DENY);
                    }
                }
            }
        }
        if (OutvotedConfig.COMMON.spawninferno.get()) {
            if (e instanceof InfernoEntity) {
                if (event.getSpawnReason() == SpawnReason.NATURAL && event.getWorld().getDifficulty() != Difficulty.HARD) {
                    List<Entity> entities = event.getWorld().getEntitiesWithinAABBExcludingEntity(event.getEntity(), event.getEntity().getBoundingBox().expand(area, area, area).expand(-area, -area, -area));
                    if (entities.stream().anyMatch(entity -> entity instanceof InfernoEntity)) {
                        event.setResult(Event.Result.DENY);
                    }
                }
            }
        }
    }

    /**
     * Adds Blazes around Infernos and adds Infernos to Mob Spawners
     */
    @SubscribeEvent
    public static void changeMobs(LivingSpawnEvent.SpecialSpawn event) {
        Entity e = event.getEntity();
        if (OutvotedConfig.COMMON.spawninferno.get()) {
            if (e instanceof InfernoEntity) {
                if (event.getSpawnReason() == SpawnReason.NATURAL) {
                    World world = event.getEntity().getEntityWorld();
                    int max = 3;
                    switch (world.getDifficulty()) {
                        case NORMAL:
                            max = 4;
                            break;
                        case HARD:
                            max = 5;
                            break;
                    }
                    int min = max - 1;
                    int rand = new Random().nextInt(max - min) + min;
                    for (int i = 1; i <= rand; i++) {
                        BlazeEntity blaze = EntityType.BLAZE.create(world);
                        blaze.setPositionAndRotation(e.getPosXRandom(2.0D), e.getPosY(), e.getPosZRandom(2.0D), e.rotationYaw, e.rotationPitch);
                        while (!world.isAirBlock(blaze.getPosition())) { // Should prevent spawning in blocks
                            blaze.setPositionAndRotation(e.getPosXRandom(2.0D), e.getPosY(), e.getPosZRandom(2.0D), e.rotationYaw, e.rotationPitch);
                        }
                        world.addEntity(blaze);
                    }
                }
            }
            if (e instanceof BlazeEntity) {
                if (event.getSpawnReason() == SpawnReason.SPAWNER) {
                    if (Math.random() > 0.8) {
                        World world = event.getEntity().getEntityWorld();

                        InfernoEntity inferno = ModEntityTypes.INFERNO.get().create(world);
                        inferno.setPositionAndRotation(e.getPosXRandom(1.0D), e.getPosY(), e.getPosZRandom(2.0D), e.rotationYaw, e.rotationPitch);

                        world.addEntity(inferno);
                    }
                }
            }
        }
    }
}
