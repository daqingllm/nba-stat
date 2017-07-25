package me.hailu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import me.hailu.analyser.PlayerAnalyser;
import me.hailu.crawler.AwardCrawler;
import me.hailu.crawler.TeamCrawler;
import me.hailu.model.Player;
import me.hailu.model.Season;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by liming_liu on 17/7/15.
 */
public class StatController {

    private static final List<String> teams = Arrays.asList(
            "CHI","CLE","DET","IND","MIL","BKN","BOS","NYK","PHI","TOR",
            "ATL","CHA","MIA","ORL","WAS","DEN","MIN","OKC","POR","UTA",
            "GSW","LAC","LAL","PHO","SAC","DAL","HOU","MEM","NOH","SAS",
            "CHH"
            ,"NJN"
    );

    public Set<Integer> findAllPlayerIds() {
        Set<Integer> players = Sets.newHashSet();
        teams.stream().forEach(team -> {
            TeamCrawler teamCrawler = new TeamCrawler();
            for (int i=2000; i<=2016; i++) {
                List<Integer> teamPlayers = teamCrawler.getTeamPlayers(team, i);
                players.addAll(teamPlayers);
                System.out.println(players.size());
            }
        });
        return players;
    }

    public Map<Integer, Player> findPlayers(Set<Integer> playerIds) {
        PlayerAnalyser playerAnalyser = new PlayerAnalyser();
        Map<Integer, Player> playerMap = Maps.newHashMap();
        for (Integer playerId : playerIds) {
            playerMap.put(playerId, playerAnalyser.parsePlayer(playerId));
        }
        return playerMap;
    }

    public void enrichAwards(Map<Integer, Player> playerMap) {
        enrichChampions(playerMap);
        enrichMvps(playerMap);
        enrichDpoys(playerMap);
        enrichFmvps(playerMap);
        enrichTeam(playerMap);
        enrichDteam(playerMap);
        enrichRecords(playerMap);
    }

    private void enrichChampions(Map<Integer, Player> playerMap) {
        AwardCrawler awardCrawler = new AwardCrawler();
        Map<Integer, List<Integer>> champions = awardCrawler.getChampions();
        for (Map.Entry<Integer, List<Integer>> entry : champions.entrySet()) {
            for (Integer playerId : entry.getValue()) {
                Player player = playerMap.get(playerId);
                if (player == null) {
                    System.out.println("[champion] player not found: " + playerId);
                    continue;
                }
                Season season = player.getSeasons().get(entry.getKey());
                season.setChampion(true);
            }
        }
    }

    private void enrichMvps(Map<Integer, Player> playerMap) {
        AwardCrawler awardCrawler = new AwardCrawler();
        Map<Integer, Integer> mvps = awardCrawler.getMvps();
        for (Map.Entry<Integer, Integer> mvp : mvps.entrySet()) {
            Player player = playerMap.get(mvp.getValue());
            player.getSeasons().get(mvp.getKey()).setMvp(true);
        }
    }

    private void enrichDpoys(Map<Integer, Player> playerMap) {
        AwardCrawler awardCrawler = new AwardCrawler();
        Map<Integer, Integer> mvps = awardCrawler.getDpoys();
        for (Map.Entry<Integer, Integer> mvp : mvps.entrySet()) {
            Player player = playerMap.get(mvp.getValue());
            player.getSeasons().get(mvp.getKey()).setDpoy(true);
        }
    }

    private void enrichFmvps(Map<Integer, Player> playerMap) {
        AwardCrawler awardCrawler = new AwardCrawler();
        Map<Integer, Integer> mvps = awardCrawler.getFmvps();
        for (Map.Entry<Integer, Integer> mvp : mvps.entrySet()) {
            Player player = playerMap.get(mvp.getValue());
            player.getSeasons().get(mvp.getKey()).setFmvp(true);
        }
    }

    private void enrichTeam(Map<Integer, Player> playerMap) {
        AwardCrawler awardCrawler = new AwardCrawler();
        Map<Integer, Map<Integer, List<Integer>>> teamPlayers = awardCrawler.getTeamPlayers();
        for (Map.Entry<Integer, Map<Integer, List<Integer>>> year : teamPlayers.entrySet()) {
            List<Integer> firstTeam = year.getValue().get(1);
            List<Integer> secondTeam = year.getValue().get(2);
            List<Integer> thirdTeam = year.getValue().get(3);
            firstTeam.stream().forEach(playerId -> {
                Player player = playerMap.get(playerId);
                player.getSeasons().get(year.getKey()).setTeam(1);
            });
            secondTeam.stream().forEach(playerId -> {
                Player player = playerMap.get(playerId);
                player.getSeasons().get(year.getKey()).setTeam(2);
            });
            thirdTeam.stream().forEach(playerId -> {
                Player player = playerMap.get(playerId);
                player.getSeasons().get(year.getKey()).setTeam(3);
            });
        }
    }

    private void enrichDteam(Map<Integer, Player> playerMap) {
        AwardCrawler awardCrawler = new AwardCrawler();
        Map<Integer, Map<Integer, List<Integer>>> teamPlayers = awardCrawler.getDteamPlayers();
        for (Map.Entry<Integer, Map<Integer, List<Integer>>> year : teamPlayers.entrySet()) {
            List<Integer> firstTeam = year.getValue().get(1);
            List<Integer> secondTeam = year.getValue().get(2);
            firstTeam.stream().forEach(playerId -> {
                Player player = playerMap.get(playerId);
                player.getSeasons().get(year.getKey()).setDteam(1);
            });
            secondTeam.stream().forEach(playerId -> {
                Player player = playerMap.get(playerId);
                player.getSeasons().get(year.getKey()).setDteam(2);
            });
        }
    }

    private void enrichRecords(Map<Integer, Player> playerMap) {
        AwardCrawler awardCrawler = new AwardCrawler();
        Map<Integer, Map<Integer, Integer>> records = awardCrawler.getRecorders();
        for (Map<Integer, Integer> record : records.values()) {
            for (Map.Entry<Integer, Integer> entry : record.entrySet()) {
                Player play = playerMap.get(entry.getValue());
                Season season = play.getSeasons().get(entry.getKey());
                season.setRecords(season.getRecords() + 1);
            }
        }
    }

    public void writeFile(Map<Integer, Player> playerMap) {
        ObjectMapper mapper = new ObjectMapper();
        try (
                FileWriter fileWritter = new FileWriter("out.txt");
                BufferedWriter bufferedWritter = new BufferedWriter(fileWritter)) {
            for (Player player : playerMap.values()) {
                bufferedWritter.write(mapper.writeValueAsString(player));
                bufferedWritter.newLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Player> readFile() {
        Map<Integer, Player> playerMap = Maps.newHashMap();
        ObjectMapper mapper = new ObjectMapper();
        try (
                FileReader fileReader = new FileReader("out.txt");
                BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String str = null;

            while((str = bufferedReader.readLine()) != null) {
                Player player = mapper.readValue(str, Player.class);
                playerMap.put(player.getId(), player);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playerMap;
    }
}
