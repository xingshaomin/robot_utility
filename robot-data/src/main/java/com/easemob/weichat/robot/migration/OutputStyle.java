package com.easemob.weichat.robot.migration;

import lombok.Getter;

/**
 * Created by dongwentao on 16/9/26.
 */
@Getter
public enum OutputStyle {

    VERTICAL(0,"vertical","用竖杠分割"),
    JSON(1,"json","json格式输出"),
    ;
    private int id;
    private String name;
    private String description;
    private OutputStyle(int id,String name,String description){
        this.id=id;
        this.name=name;
        this.description=description;
    }

    public static OutputStyle findByName(String name){
        for (OutputStyle outputStyle:OutputStyle.values()){
            if (outputStyle.getName().equals(name)){
                return outputStyle;
            }
        }
        return null;
    }
}
