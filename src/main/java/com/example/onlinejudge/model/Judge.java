package com.example.onlinejudge.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class Judge {
    public Judge(Integer probID, String input, String output, Integer timeLimit) {
        this.probID = probID;
        this.input = input;
        this.output = output;
        this.timeLimit = timeLimit;
    }

    // 题目ID
    @TableField("id")
    private Integer probID;

    private String input;
    private String output;
    @TableField("timeLimit")
    private Integer timeLimit;
}
