package com.arlania.world.content;

import com.arlania.GameServer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class PlayerLogs {

    /**
     * Log file path
     **/
    private static final String FILE_PATH = "/home/quinn/Paescape" + File.separator + "Saves/Logs/";

    /**
     * Fetches system time and formats it appropriately
     *
     * @return Formatted time
     */
    private static String getTime() {
        Date getDate = new Date();
        String timeFormat = "M/d/yy hh:mma";
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        return "[" + sdf.format(getDate) + "]\t";
    }

    /**
     * Writes formatted string to text file
     *
     * @param file     - File to write data to
     * @param ORE_DATA - Data to written
     * @throws IOException
     */
    public static void log(String file, String writable) {
        GameServer.getLoader().getEngine().submit(() -> {
            try {
                FileWriter fw = new FileWriter(FILE_PATH + file + ".txt", true);
                if (fw != null) {
                    fw.write(getTime() + writable + "\t");
                    fw.write(System.lineSeparator());
                    fw.close();
                }
            } catch (Exception e) {
                GameServer.getLogger().log(Level.SEVERE, "ruh roh", e);
            }
        });
    }

    public static void npcSpawnLog(String file, String writable) {
        GameServer.getLoader().getEngine().submit(() -> {
            try {
                FileWriter fw = new FileWriter(FILE_PATH + file + ".txt", true);
                if (fw != null) {
                    fw.write(writable);
                    fw.write(System.lineSeparator());
                    fw.close();
                }
            } catch (Exception e) {
                GameServer.getLogger().log(Level.SEVERE, "ruh roh", e);
            }
        });
    }

    public static void DevLog(String file, String writable) {
        GameServer.getLoader().getEngine().submit(() -> {
            try {
                FileWriter fw = new FileWriter(FILE_PATH + file + ".txt", true);
                if (fw != null) {
                    fw.write(writable + "\t");
                    fw.write(System.lineSeparator());
                    fw.close();
                }
            } catch (Exception e) {
                GameServer.getLogger().log(Level.SEVERE, "ruh roh", e);
            }
        });
    }

    public static void eraseFile(String name) {
        try {
            File file = new File(FILE_PATH + name + ".txt");
            file.delete();
            log(name,
                    "\t <----------------- File automatically cleaned ----------------->");
        } catch (Exception e) {
            GameServer.getLogger().log(Level.SEVERE, "ruh roh", e);
        }
    }

}
