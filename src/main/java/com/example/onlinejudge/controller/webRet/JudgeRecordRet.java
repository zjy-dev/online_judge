package com.example.onlinejudge.controller.webRet;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.onlinejudge.model.JudgeRecord;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
public class JudgeRecordRet{
    String userName;

    Integer probID;

    // 用户代码
    String userCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date submitDate;

    // 评测结果
    JudgeCode status;
}
