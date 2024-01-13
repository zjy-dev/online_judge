package com.example.onlinejudge.controller.webRet;

import com.example.onlinejudge.model.Problem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ProblemRet extends CommonRet{
    private Problem problem;

    public ProblemRet(Integer code, String msg, Problem problem) {
        super(code, msg);
        this.problem = problem;
    }
}
