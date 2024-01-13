package com.example.onlinejudge.controller.webRet;

public enum JudgeCode {
    ACCEPTED,                       // 通过
    COMPILE_ERROR,                  // 编译错误
    TIME_LIMIT_EXCEPTION,           // 运行超时
    SEGMENT_FAULT,                  // 段错误
    ARITHMETIC_EXCEPTION,           // 算术异常
    WRONG_ANSWER,                   // 答案错误
    UNKNOWN_ERROR                   // 未知错误
}
