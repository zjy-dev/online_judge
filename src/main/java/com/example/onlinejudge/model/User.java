package com.example.onlinejudge.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class User {
    private Integer id;
    private String name;

    @TableField("encodedPwd")
    private String encodedPwd;

    public User(String name, String encodedPwd) {
        this.id = null;
        this.name = name;
        this.encodedPwd = encodedPwd;
    }
}

