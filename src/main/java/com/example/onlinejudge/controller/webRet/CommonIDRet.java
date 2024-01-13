package com.example.onlinejudge.controller.webRet;

import lombok.Data;

@Data
public class CommonIDRet extends CommonRet{
    private Integer id;

    public CommonIDRet(Integer code, String msg, Integer id) {
        super(code, msg);
        this.id = id;
    }

}
