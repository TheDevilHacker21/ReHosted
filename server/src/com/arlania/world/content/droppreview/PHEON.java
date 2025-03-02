package com.arlania.world.content.droppreview;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.world.entity.impl.player.Player;

public class PHEON {

    public static int config;
    public static int item;
    public static int index = 0;

    public static final int[] PHEON = {15241, 15126, 19335, 11235, 11989};


    /*
     * Get rare drop(s) for that boss location
     */
    public static void getDrop(Player player) {

    }

    public static void startPreview(Player player) {
        TaskManager.submit(new Task(1, player, false) {
            @Override
            public void execute() {
                sendInterface(player);
                this.stop();
            }
        });
    }

    /*
     * Sends the next item from array
     */
    public static void nextItem(Player player) {
        if (index == PHEON.length) {
            index = 0;
            player.getPacketSender().sendItemOnInterface(47900, PHEON[index++], 1);
        } else {
            player.getPacketSender().sendItemOnInterface(47900, PHEON[index++], 1);
        }

    }

    /*
     * Sends the previous item from the array
     */
    public static void previousItem(Player player) {
        if (index == -1) {
            index = PHEON.length - 1;
            player.getPacketSender().sendItemOnInterface(47900, PHEON[index--], 1);
        } else {
            player.getPacketSender().sendItemOnInterface(47900, PHEON[index--], 1);
        }
    }

    /*
     * Sends the interface with the item on interface method
     */
    public static void sendInterface(Player player) {
        TaskManager.submit(new Task(2, player, false) {
            @Override
            public void execute() {
                player.sendParallellInterfaceVisibility(47989, true);
                player.getPacketSender().sendString(47904, "Rare Drops:");
                player.getPacketSender().sendItemOnInterface(47900, 995, 1);
                this.stop();
            }
        });

    }

    /*
     * Handles the closing of the walkable interface
     */

    public static void closeInterface(Player player) {
        player.sendParallellInterfaceVisibility(47989, false);
        player.getPacketSender().sendMessage("Good luck with your drops!");

    }

}