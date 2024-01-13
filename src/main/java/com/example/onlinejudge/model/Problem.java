package com.example.onlinejudge.model;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class Problem {
    private Integer id;

    private String name;

    private String description;

    @TableField("sampleInput")
    private String sampleInput;

    @TableField("sampleOutput")
    private String sampleOutput;

    public Problem(String name, String description, String sampleInput, String sampleOutput) {
        this.id = 0;
        this.name = name;
        this.description = description;
        this.sampleInput = sampleInput;
        this.sampleOutput = sampleOutput;
    }


}
