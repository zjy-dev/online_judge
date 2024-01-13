package com.example.onlinejudge.mapper;

import com.example.onlinejudge.controller.webRet.JudgeCode;
import com.example.onlinejudge.model.JudgeRecord;
import org.apache.tomcat.jni.Time;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class JudgeRecordMapperTests {
    @Autowired
    JudgeRecordMapper judgeRecordMapper;

    @Test
    public void insertTest() {
        assert judgeRecordMapper.insert(new JudgeRecord(3, 6, "#include...", new Date(), JudgeCode.COMPILE_ERROR)) == 1;
    }
}
