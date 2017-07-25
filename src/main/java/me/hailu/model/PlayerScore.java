package me.hailu.model;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * Created by liming_liu on 17/7/16.
 */
@Data
public class PlayerScore {
    private int playserId;
    private String playerName;
    private Map<Integer, Integer> seasonScore = Maps.newLinkedHashMap();
    private int score;
}
