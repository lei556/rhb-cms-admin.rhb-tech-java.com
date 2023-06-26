package com.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author leishuiping
 * @createDate 2023-06-21
 */

@TableName(value ="book")
public class BaseStat implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}