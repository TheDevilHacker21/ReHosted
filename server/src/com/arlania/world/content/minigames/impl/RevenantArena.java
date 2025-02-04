package com.arlania.world.content.minigames.impl;

import com.arlania.model.Position;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.entity.impl.npc.NPC;

import java.util.ArrayList;

/**
 * @author Patrick McDonnell
 * Wrote this quickly!!
 * Handles the Revenant Arena
 */
public class RevenantArena {

    private static Position[] positions = {
            new Position(3099, 3942, 0),
            new Position(3101, 3942, 0),
            new Position(3100, 3941, 0),
            new Position(3099, 3940, 0),
            new Position(3101, 3940, 0),

            new Position(3110, 3942, 0),
            new Position(3112, 3942, 0),
            new Position(3111, 3941, 0),
            new Position(3110, 3940, 0),
            new Position(3112, 3940, 0),

            new Position(3104, 3934, 0),
            new Position(3106, 3934, 0),
            new Position(3105, 3933, 0),
            new Position(3104, 3932, 0),
            new Position(3106, 3932, 0),

            new Position(3099, 3927, 0),
            new Position(3101, 3927, 0),
            new Position(3100, 3926, 0),
            new Position(3099, 3925, 0),
            new Position(3101, 3925, 0),

            new Position(3110, 3927, 0),
            new Position(3112, 3927, 0),
            new Position(3111, 3926, 0),
            new Position(3110, 3925, 0),
            new Position(3112, 3925, 0),
    };

    private static int[] revenants = {
            6691, 6691, 6691, 6691, 6691,
            6725, 6725, 6725, 6725, 6725,
            6701, 6701, 6701, 6701, 6701,
            6716, 6716, 6716, 6716, 6716,
            6715, 6715, 6715, 6715, 6715
    };

    public static void start() {
        for (int i = 0; i < 25; i++)
            spawn(i);

    }

    public static void spawn(int type) {
        NPC spawn = NPC.of(revenants[type], positions[type]);
        World.register(spawn);
    }
}
