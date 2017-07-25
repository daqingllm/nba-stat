package me.hailu.analyser;

import com.google.common.collect.Maps;
import me.hailu.crawler.PlayerCrawler;
import me.hailu.model.Player;
import me.hailu.model.Season;
import me.hailu.model.Stat;

import java.util.Map;

/**
 * Created by liming_liu on 17/7/15.
 */
public class PlayerAnalyser {

    public Player parsePlayer(int playerId) {
        PlayerCrawler playerCrawler = new PlayerCrawler("http://www.stat-nba.com/player/" + playerId + ".html");
        StatBoxAnalyser statBoxAnalyser = new StatBoxAnalyser();
        Player player = new Player();
        player.setId(playerId);
        player.setName(playerCrawler.getName());

        Map<Integer, Stat> regularStatMap = statBoxAnalyser.parseStat(playerCrawler.getStatBoxTot());
        Map<Integer, Stat> playoffStatMap = statBoxAnalyser.parseStat(playerCrawler.getPlayoffStatBoxTot());
        Map<Integer, Season> seasonMap = Maps.newHashMap();
        for (Map.Entry<Integer, Stat> entry : regularStatMap.entrySet()) {
            Season season = new Season();
            season.setSeason(entry.getKey());
            season.setRegular(entry.getValue());
            Stat playoffStat = playoffStatMap.get(entry.getKey());
            if (playoffStat != null) {
                season.setPlayoff(playoffStat);
            }
            seasonMap.put(entry.getKey(), season);
        }
        player.setSeasons(seasonMap);

        return player;
    }
}
