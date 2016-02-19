package com.ivon.fbla;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Owner on 2/18/2016.
 */
public class Review {

    public final int stylish;
    public final int professional;
    public final boolean dressCode;
    public final String comment;
    public final String owner;
    public final long timestamp;

    public Review(int stylish, int professional, boolean dressCode, String comment, String owner, long timestamp) {
        this.stylish = stylish;
        this.professional = professional;
        this.dressCode = dressCode;
        this.comment = comment;
        this.owner = owner;
        this.timestamp = timestamp;
    }

    public Map<String, Object> format() {
        Map<String, Object> map = new HashMap<>();
        map.put("stylish", stylish);
        map.put("professional", professional);
        map.put("dressCode", dressCode);
        map.put("comment", comment);
        map.put("owner", owner);
        map.put("timestamp", timestamp);
        return map;
    }

}
