package net.mirolls.autohypixelrank.store;

public class RankStore {
    private static String playerRank = "DEFAULT";

    public static String getPlayerRank() {
        return playerRank;
    }

    public static void setPlayerRank(String rank) {
        playerRank = rank;
    }
}
