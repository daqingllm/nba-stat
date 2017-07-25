package me.hailu;

import com.google.common.collect.Lists;
import me.hailu.controller.StatController;
import me.hailu.model.Player;
import me.hailu.model.PlayerScore;
import me.hailu.model.Season;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by liming_liu on 17/6/17.
 */
public class Main {

    public static void main1(String[] args) throws IOException {
        StatController statController = new StatController();
        Set<Integer> playerIds = statController.findAllPlayerIds();
        System.out.println(playerIds.size());
        Map<Integer, Player> playerMap = statController.findPlayers(playerIds);
        statController.enrichAwards(playerMap);
        System.out.println(playerMap.size());
        statController.writeFile(playerMap);
    }

    public static void main(String[] args) {
        Map<Integer, Player> playerMap = new StatController().readFile();
        System.out.println(playerMap.size());

        List<PlayerScore> scores = Lists.newArrayList();
        for (Player player : playerMap.values()) {
            PlayerScore playerScore = new PlayerScore();
            playerScore.setPlayserId(player.getId());
            playerScore.setPlayerName(player.getName());
            for (int i=2000; i<=2016; i++) {
                Season season = player.getSeasons().get(i);
                if (season == null) {
                    continue;
                }
                playerScore.getSeasonScore().put(i, season.quality());
                playerScore.setScore(playerScore.getScore() + season.quality());
            }
            scores.add(playerScore);
        }

        Collections.sort(scores, Comparator.comparingInt(score -> -1 * score.getScore()));
        System.out.println(scores.size());
        genResult(scores);
    }

    private static void genResult(List<PlayerScore> scores) {
        try (
                FileWriter fileWritter = new FileWriter("rank.txt");
                BufferedWriter bufferedWritter = new BufferedWriter(fileWritter)) {
            for (PlayerScore score : scores) {
                bufferedWritter.write("" + score.getPlayserId());
                bufferedWritter.write("\t");
                bufferedWritter.write(new String(score.getPlayerName().getBytes(), Charset.defaultCharset()));
                bufferedWritter.write("\t");
                bufferedWritter.write("" + score.getScore());
                bufferedWritter.write("\t");
                int season = 0;
                for (int i=2000; i<=2016; i++) {
                    int s = score.getSeasonScore().get(i) == null ? 0 : score.getSeasonScore().get(i);
                    bufferedWritter.write("" + s);
                    if (s > 0) {
                        season++;
                    }
                    bufferedWritter.write("\t");
                }
                bufferedWritter.write("" + 1.0*score.getScore()/season);
                bufferedWritter.newLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
