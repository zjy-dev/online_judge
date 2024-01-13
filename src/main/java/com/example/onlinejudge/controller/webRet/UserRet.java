package com.example.onlinejudge.controller.webRet;

import com.example.onlinejudge.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UserRet extends CommonRet{
    private Integer id;
    private String name;

    public UserRet(Integer code, String msg, Integer id, String name) {
        super(code, msg);
        this.id = id;
        this.name = name;
    }
}
