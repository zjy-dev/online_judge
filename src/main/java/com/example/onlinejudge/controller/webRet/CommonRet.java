package com.example.onlinejudge.controller.webRet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonRet {
    protected Integer code;
    protected String msg;
}
