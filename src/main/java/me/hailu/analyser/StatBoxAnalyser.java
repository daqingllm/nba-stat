package me.hailu.analyser;

import com.google.common.collect.Maps;
import me.hailu.model.Stat;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by liming_liu on 17/7/13.
 */
public class StatBoxAnalyser {

    public Map<Integer, Stat> parseStat(List<List<String>> rawDatas) {
        Map<Integer, Stat> result = Maps.newHashMap();

        int dupSeason = 0;
        for (List<String> data : rawDatas) {
            if (CollectionUtils.isEmpty(data)) {
                break;
            }
            int season = Integer.valueOf(data.get(1));
            if (season < 2000) {
                continue;
            }
            if (season == dupSeason) {
                continue;
            }
            if (data.get(2).equals("TOT")) {
                dupSeason = season;
            }
            if (data.size() == 25) {
                result.put(Integer.valueOf(data.get(1)), parsePlayoffStat(data));
            } else if (data.size() == 26) {
                result.put(Integer. valueOf(data.get(1)), parseRegularStat(data));
            }
        }
        return result;
    }

    private Stat parseRegularStat(List<String> data) {
        Stat stat = new Stat();
        stat.setMatch(Integer.valueOf(data.get(3)));
        stat.setStart(Integer.valueOf(data.get(4)));
        stat.setMinute(Integer.valueOf(data.get(5)));
        stat.setHit(Integer.valueOf(data.get(7)));
        stat.setShoot(Integer.valueOf(data.get(8)));
        stat.setThreePointHit(Integer.valueOf(data.get(10)));
        stat.setThreePointShoot(Integer.valueOf(data.get(11)));
        stat.setFreeThrowHit(Integer.valueOf(data.get(13)));
        stat.setFreeThrowShoot(Integer.valueOf(data.get(14)));
        stat.setReb(Integer.valueOf(data.get(15)));
        stat.setOffReb(Integer.valueOf(data.get(16)));
        stat.setDefReb(Integer.valueOf(data.get(17)));
        stat.setAssist(Integer.valueOf(data.get(18)));
        stat.setSteal(Integer.valueOf(data.get(19)));
        stat.setBlock(Integer.valueOf(data.get(20)));
        stat.setTurnover(Integer.valueOf(data.get(21)));
        stat.setFoul(Integer.valueOf(data.get(22)));
        stat.setScore(Integer.valueOf(data.get(23)));
        stat.setWin(Integer.valueOf(data.get(24)));
        stat.setLoss(Integer.valueOf(data.get(25)));
        return stat;
    }

    private Stat parsePlayoffStat(List<String> data) {
        Stat stat = new Stat();
        stat.setMatch(Integer.valueOf(data.get(3)));
        stat.setMinute(Integer.valueOf(data.get(4)));
        stat.setHit(Integer.valueOf(data.get(6)));
        stat.setShoot(Integer.valueOf(data.get(7)));
        stat.setThreePointHit(Integer.valueOf(data.get(9)));
        stat.setThreePointShoot(Integer.valueOf(data.get(10)));
        stat.setFreeThrowHit(Integer.valueOf(data.get(12)));
        stat.setFreeThrowShoot(Integer.valueOf(data.get(13)));
        stat.setReb(Integer.valueOf(data.get(14)));
        stat.setOffReb(Integer.valueOf(data.get(15)));
        stat.setDefReb(Integer.valueOf(data.get(16)));
        stat.setAssist(Integer.valueOf(data.get(17)));
        stat.setSteal(Integer.valueOf(data.get(18)));
        stat.setBlock(Integer.valueOf(data.get(19)));
        stat.setTurnover(Integer.valueOf(data.get(20)));
        stat.setFoul(Integer.valueOf(data.get(21)));
        stat.setScore(Integer.valueOf(data.get(22)));
        stat.setWin(Integer.valueOf(data.get(23)));
        stat.setLoss(Integer.valueOf(data.get(24)));
        return stat;
    }
}
