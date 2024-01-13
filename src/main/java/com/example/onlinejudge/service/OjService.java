package com.example.onlinejudge.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.onlinejudge.mapper.JudgeMapper;
import com.example.onlinejudge.mapper.JudgeRecordMapper;
import com.example.onlinejudge.mapper.ProblemMapper;
import com.example.onlinejudge.mapper.UserMapper;
import com.example.onlinejudge.model.Judge;
import com.example.onlinejudge.controller.webRet.JudgeCode;
import com.example.onlinejudge.controller.webRet.JudgeRet;
import com.example.onlinejudge.model.JudgeRecord;
import com.example.onlinejudge.util.ProcessRet;
import com.example.onlinejudge.util.ProcessRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.BitSet;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Transactional
public class OjService {

    @Autowired
    private JudgeMapper judgeMapper;

    @Autowired
    private JudgeRecordMapper judgeRecordMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProblemMapper problemMapper;

    private BitSet bitSet;
    private Lock lock;
//    private Mut
    public OjService() {
        this.lock = new ReentrantLock();
        this.bitSet = new BitSet(150);
    }

    private void clearBit(int pos) {
        this.lock.lock();
        this.bitSet.set(pos, false);
        this.lock.unlock();
    }

    // 判断解答problemId号问题的code是否正确
    public JudgeRet judge(int userId, int problemId, String code) throws IOException, InterruptedException {
        // 去数据库查询相关的problem信息
        QueryWrapper<Judge> wrapper = new QueryWrapper<>();
        List<Judge> judges = judgeMapper.selectList(wrapper.eq("id", problemId));
        if (judges == null || judges.size() == 0) {
            return new JudgeRet(-1, JudgeCode.UNKNOWN_ERROR, "题目不存在");
        }

        String basePath = "./judge/cpp";
//        TODO: DELETE THIS LINE
//        if (!Files.exists(Path.of(basePath))) {
//            Files.createDirectories(Path.of(basePath));
//            Files.createDirectories(Path.of(basePath + "/ret"));
//        }

        int id = -1;
        while (id == -1) {
            lock.lock();
            for (int i = 0; i < 100; i++) {
                if (!bitSet.get(i)) {
                    bitSet.set(i);
                    id = i;
                }
            }
            lock.unlock();
        }


        // 生成存储code的文件路径
        String fileName = String.format(basePath + "/%d.cpp", id);

        // 存储code
        Files.writeString(Paths.get(fileName), new String(code.getBytes(StandardCharsets.UTF_8)), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        // 编译code
        var processRunner = new ProcessRunner();
        String executableName = String.format(basePath + "/ret/%d", id);
        ProcessRet ret = processRunner.runProcess("g++", fileName, "-o", executableName);

        // 编译失败, 返回编译错误
        if (ret.getExitStatus() != 0) {
            judgeRecordMapper.insert(new JudgeRecord(userId, problemId, code, new Date(), JudgeCode.COMPILE_ERROR));
            this.clearBit(id);
            return new JudgeRet(0, JudgeCode.COMPILE_ERROR, "编译错误");
        }

        for (Judge judge : judges) {
            // 以给定的时间限制和输入多次运行程序
            ret = processRunner.runProcessWithTimelimitAndInput(judge.getTimeLimit(), judge.getInput(), executableName);

            if (ret.getExitStatus() == 0) {
                // 安稳的运行结束了, 判断一下答案是否正确
                if (!ret.getOutput().replaceAll("\r", "").equals(judge.getOutput().replaceAll("\r", ""))) {
                    judgeRecordMapper.insert(new JudgeRecord(userId, problemId, code, new Date(), JudgeCode.COMPILE_ERROR));
                    this.clearBit(id);
                    return new JudgeRet(0, JudgeCode.WRONG_ANSWER, "答案错误");
                }
            } else if (ret.getExitStatus() - 128 == 15) {
                judgeRecordMapper.insert(new JudgeRecord(userId, problemId, code, new Date(), JudgeCode.TIME_LIMIT_EXCEPTION));
                this.clearBit(id);
                return new JudgeRet(0, JudgeCode.TIME_LIMIT_EXCEPTION, "超时");
            } else if (ret.getExitStatus() - 128 == 11) {
                judgeRecordMapper.insert(new JudgeRecord(userId, problemId, code, new Date(), JudgeCode.SEGMENT_FAULT));
                this.clearBit(id);
                return new JudgeRet(0, JudgeCode.SEGMENT_FAULT, "段错误");
            } else if (ret.getExitStatus() - 128 == 8) {
                judgeRecordMapper.insert(new JudgeRecord(userId, problemId, code, new Date(), JudgeCode.ARITHMETIC_EXCEPTION));
                this.clearBit(id);
                return new JudgeRet(0, JudgeCode.ARITHMETIC_EXCEPTION, "算术异常");
            } else {
                this.clearBit(id);
                return new JudgeRet(0, JudgeCode.UNKNOWN_ERROR, "未知错误");
            }
        }

        judgeRecordMapper.insert(new JudgeRecord(userId, problemId, code, new Date(), JudgeCode.ACCEPTED));
        this.clearBit(id);
        return new JudgeRet(0, JudgeCode.ACCEPTED, "通过");
    }


    public List<JudgeRecord> getJudgeRecordsByUserID(int userID) {
        if (userMapper.selectById(userID) == null) {
            return null;
        }
        return this.judgeRecordMapper.selectList(new QueryWrapper<JudgeRecord>().eq("userID", userID));
    }

    public List<JudgeRecord> getJudgeRecordsByUserIDAndProbID(int userID, int probID) {
        if (userMapper.selectById(userID) == null || problemMapper.selectById(probID) == null) {
            return null;
        }
        QueryWrapper<JudgeRecord> wrapper = new QueryWrapper<JudgeRecord>().eq("userID", userID).eq("probID", probID);

        return this.judgeRecordMapper.selectList(wrapper);
    }
}

