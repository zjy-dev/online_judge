package com.example.onlinejudge.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.onlinejudge.controller.webRet.JudgeCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeRecord {
    @TableField("userID")
    Integer userID;

    @TableField("probID")
    Integer probID;

    // 用户代码
    @TableField("userCode")
    String userCode;

    @TableField("submitDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date submitDate;

    // 评测结果
    JudgeCode status;
}
