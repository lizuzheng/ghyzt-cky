package com.cky.common;

import java.io.Serializable;
import java.util.Map;

import com.cky.model.AutoTextModel;

public class SerializableMap implements Serializable {
	 
    private Map<Integer,AutoTextModel> map;
 
    public Map<Integer,AutoTextModel> getMap() {
        return map;
    }
 
    public void setMap(Map<Integer,AutoTextModel> map) {
        this.map = map;
    }
}