package com.example.onlinejudge.controller;

import com.example.onlinejudge.controller.webRet.CommonIDRet;
import com.example.onlinejudge.controller.webRet.ProblemRet;
import com.example.onlinejudge.model.Problem;
import com.example.onlinejudge.service.ProblemService;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/prob")
public class ProblemController {
    @Autowired
    private ProblemService problemService;

    @GetMapping("/all")
    @ResponseBody
    public List<Problem> getAllProblems() {
        return problemService.getProblemList();
    }

    @GetMapping("/id/{id}")
    @ResponseBody
    public ProblemRet getProblemByID(@PathVariable(name = "id") int id) {
        // 校参
        if (id <= 0) {
            return new ProblemRet(-1, "题目ID不合法", null);
        }

        Problem problem = problemService.getproblemById(id);
        if (problem == null) {
            return new ProblemRet(-1, "题目不存在", null);
        }

        return new ProblemRet(0, "成功查询到题目", problem);
    }

    @GetMapping("/name/{name}")
    @ResponseBody
    public ProblemRet getProblemByID(@PathVariable(name = "name") String name) {
        // 校参
        if (name.length() == 0) {
            return new ProblemRet(-1, "题目名称不合法", null);
        }

        Problem problem = problemService.getproblemByName(name);
        if (problem == null) {
            return new ProblemRet(-1, "题目不存在", null);
        }

        return new ProblemRet(0, "成功查询到题目", problem);
    }

    @PostMapping
    @ResponseBody
    public CommonIDRet addProblem(@RequestBody String submit) {
        var obj = new JSONObject(submit);
        String name = obj.getString("name");
        String description = obj.getString("description");
        String sampleInput = obj.getString("sampleInput");
        String sampleOutput = obj.getString("sampleOutput");
        Object[] inputs = obj.getJSONArray("inputs").toList().toArray();
        Object[] outputs = obj.getJSONArray("outputs").toList().toArray();
        Object[] timeLimits = obj.getJSONArray("timeLimits").toList().toArray();

        if (name.length() == 0 || description.length() == 0 || sampleOutput.length() == 0) {
            return new CommonIDRet(-1, "名称或描述或样例输出不能为空", -1);
        }

        if (inputs.length != outputs.length || inputs.length != timeLimits.length) {
            return new CommonIDRet(-1, "输入输出和时间限制数组长度不匹配", -1);
        }

        String[] inputsArr = Arrays.stream(inputs).toArray(String[]::new);
        String[] outputsArr = Arrays.stream(outputs).toArray(String[]::new);
        Integer[] tlArr = Arrays.stream(timeLimits).toArray(Integer[]::new);

        int id = 0;
        try {
            id = problemService.addProblem(name, description, sampleInput, sampleOutput, inputsArr, outputsArr, tlArr);
        } catch (RuntimeException e) {
            return new CommonIDRet(-1, "添加问题失败", -1);
        }

        if (id <= 0) {
            return new CommonIDRet(-1, "添加问题失败", -1);
        }
        return new CommonIDRet(0, "添加问题成功", id);
    }
}
