package me.hailu.model;

import lombok.Data;

import java.util.Map;

/**
 * Created by liming_liu on 17/7/4.
 */
@Data
public class Player {

    private int id;
    private String name;
    private Map<Integer, Season> seasons;

    public int totalScore() {
        int score = 0;
        for (Season season : seasons.values()) {
            score += season.quality();
        }
        return score;
    }
}
