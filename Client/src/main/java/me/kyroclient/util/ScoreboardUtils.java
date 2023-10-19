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
    public static boolean contains(ScoreObjective objective, String str)
    {
        Scoreboard board = objective.getScoreboard();
        Collection<Score> score1 = board.getScores();
        List<Score> scores = score1.stream().filter(e -> e != null && e.getPlayerName() != null && !e.getPlayerName().startsWith("#")).collect(Collectors.toList());

        for (Score score : scores)
        {
            ScorePlayerTeam team = board.getPlayersTeam(score.getPlayerName());
            String s2 = ScorePlayerTeam.formatPlayerName(team, score.getPlayerName());

            if (s2.contains(str))
                return true;
        }

        return false;
    }
}
