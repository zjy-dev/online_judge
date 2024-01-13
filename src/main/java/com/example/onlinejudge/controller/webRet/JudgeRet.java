package com.example.onlinejudge.controller.webRet;


import lombok.Data;

@Data
public class JudgeRet extends CommonRet{
    private JudgeCode judgeCode;
    public JudgeRet(Integer code, JudgeCode judgeCode, String msg) {
        super(code, msg);
        this.judgeCode = judgeCode;
    }

    @Override
    public String toString() {
        return "JudgeRet{" +
                "judgeCode=" + judgeCode +
                ", msg='" + msg + '\'' +
                '}';
    }


}
