package com.lion.resource.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lion.core.IEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @description: 客户端权限
 * @author: mr.liu
 * @create: 2020-10-03 14:18
 **/
public enum Scope implements IEnum {

    READ(0, "读"), WRITE(1, "写"),UPDATE(2,"更新"),DELETE(3,"删除");

    private final int key;

    private final String desc;

    private Scope(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    @Override
    public Integer getKey() {
        return key;
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public String getDesc(){
        return desc;
    }

    @Override
    public Map<String, Object> jsonValue() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", key);
        map.put("desc", desc);
        map.put("name", getName());
        return map;
    }

    @JsonCreator
    public static Scope getScope(String name){
        for(Scope item : values()){
            if(Objects.equals(item.getName(),name)){
                return item;
            }
        }
        return null;
    }

    public static void main(String agrs[]){
        System.out.println(Scope.READ.getName().toLowerCase());
    }
}
