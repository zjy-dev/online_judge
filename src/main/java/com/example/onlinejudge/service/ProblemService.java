package com.example.onlinejudge.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.onlinejudge.mapper.JudgeMapper;
import com.example.onlinejudge.mapper.JudgeRecordMapper;
import com.example.onlinejudge.mapper.ProblemMapper;
import com.example.onlinejudge.model.Judge;
import com.example.onlinejudge.model.Problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProblemService {
    @Autowired
    private ProblemMapper probMapper;

    @Autowired
    private JudgeMapper judgeMapper;

    @Autowired
    private JudgeRecordMapper judgeRecordMapper;

    public List<Problem> getProblemList() {
        List<Problem> problems = probMapper.selectList(null);
        return problems;
    }

    public Problem getproblemById(int id) {
        return probMapper.selectById(id);
    }
    public Problem getproblemByName(String name) {
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        return probMapper.selectOne(queryWrapper.eq("name", name));
    }

    /**
     * @return int类型, 0代表题目名重复, -1代表未知错误, >0代表题目ID
     */

    public int addProblem(String name, String description, String sampleInput, String sampleOutput, String[] inputs, String[] outputs, Integer[] timeLimits) throws RuntimeException {
        // 先判断题目名是否重复
        Problem problem = this.getproblemByName(name);
        if (problem != null) {
            return 0;
        }

        // 若未重复则添加新题目
        problem = new Problem(name, description, sampleInput, sampleOutput);

        Integer ret;

        try {
            ret = probMapper.insertProblem(problem);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
        if (ret == null || ret == 0) {
            return -1;
        }

        // 获取自动生成的ID
        int ID = problem.getId();
        if (ID <= 0) {
            throw new RuntimeException("未知错误, 用户ID:" + problem.getId() + "<= 0, 添加题目失败");
        }

        // 添加多条评测信息
        for (int i = 0; i < inputs.length; i++) {
            if (judgeMapper.insert(new Judge(ID, inputs[i], outputs[i], timeLimits[i])) == 0) {
                throw new RuntimeException("添加评测信息错误");
            }
        }

        return ID;
    }
}

