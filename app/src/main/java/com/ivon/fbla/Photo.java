package com.ivon.fbla;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Owner on 2/18/2016.
 */
public class Photo {

    public final String id;
    public final String owner;
    public final String image;
    public final long timestamp;

    public Photo(String id, String owner, String image, long timestamp) {
        this.id = id;
        this.owner = owner;
        this.image = image;
        this.timestamp = timestamp;
    }

    public Map<String, Object> format() {
        Map<String, Object> map = new HashMap<>();
        map.put("owner", owner);
        map.put("image", image);
        map.put("timestamp", timestamp);
        return map;
    }

}
