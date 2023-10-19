package me.kyroclient.util;

import me.kyroclient.KyroClient;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardUtils {
    public static boolean contains(String str)
    {
        Scoreboard scoreboard = KyroClient.mc.theWorld.getScoreboard();
        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        List<Score> scores = scoreboard.getSortedScores(objective).stream().collect(Collectors.toList());

        List<Score> filteredScores = scores.stream().filter(i -> i != null && i.getPlayerName() != null && !i.getPlayerName().startsWith("#")).collect(Collectors.toList());
        for (Score score : filteredScores)
        {
            String formatted = ScorePlayerTeam.formatPlayerName(scoreboard.getPlayersTeam(score.getPlayerName()), score.getPlayerName());
            if (formatted == str)
                return true;
        }

        return false;
    }
}
