package com.arlania.world.content;

import com.arlania.util.RandomUtility;
import com.arlania.util.Stopwatch;
import com.arlania.world.World;

/*
 * @author Bas
 * www.Arlania.com
 */

public class Reminders {


    private static final int TIME = 450000; //5 minutes
    private static final Stopwatch timer = new Stopwatch().reset();
    public static String currentMessage;

    /*
     * Random Message Data
     */
    private static final String[][] MESSAGE_DATA = {
            //{"@blu@[Server]@blu@ Use the command ::drop (npcname) for drop tables"},
            {"@blu@[Server]@blu@ Don't forget DoubleXP stacks on Fridays!"},
            {"@blu@[Server]@blu@ Did you know you can craft jewelry using gems on gold jewelry?"},
            {"@blu@[Server]@blu@ Did you know there is an <img=32> <img=33> <img=34> mode on this server?"},
            {"@blu@[Server]@blu@ Did you know we have Equipment Upgrades?!"},
            {"@blu@[Server]@blu@ Did you know Rare Candy can be used to get bonus XP or bonus drop rate?!"},
            {"@blu@[Server]@blu@ You may auto-click and cheese as you please!"},
			{"@blu@[Server]@blu@ Be curtious! Dont end and event without asking!"},
    };

    /*
     * Sequence called in world.java
     * Handles the main method
     * Grabs random message and announces it
     */
    public static void sequence() {
        if (timer.elapsed(TIME)) {
            timer.reset();
            currentMessage = MESSAGE_DATA[RandomUtility.inclusiveRandom(MESSAGE_DATA.length - 1)][0];
            World.sendMessage("reminders", currentMessage);
        }
    }

}