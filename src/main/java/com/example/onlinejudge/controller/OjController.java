package com.example.onlinejudge.controller;

import com.example.onlinejudge.controller.webRet.JudgeRecordRet;
import com.example.onlinejudge.model.JudgeRecord;
import com.example.onlinejudge.service.OjService;
import com.example.onlinejudge.controller.webRet.JudgeCode;
import com.example.onlinejudge.controller.webRet.JudgeRet;
import com.example.onlinejudge.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/oj")
public class OjController {

    @Autowired
    OjService ojService;

    @Autowired
    UserService userService;

    @PostMapping
    @ResponseBody
    public JudgeRet codeSubmit(@RequestBody String submit) throws IOException, InterruptedException {

        var obj = new JSONObject(submit);
        String submitCode = obj.getString("code");
        int problemId = obj.getInt("problemId");
        int userId = obj.getInt("userId");

        // 校参
        if (submitCode.length() == 0 || problemId < 0) {
            return new JudgeRet(-1, JudgeCode.UNKNOWN_ERROR, "非法题号或空代码");
        }

        return ojService.judge(userId, problemId, submitCode);
    }

//    @GetMapping("/{userID}")
//    @ResponseBody
//    public List<JudgeRecordRet> getAllJudgeRecordsByUserID(@PathVariable(name = "userID") int userId) {
//        List<JudgeRecord> judgeRecords = ojService.getJudgeRecordsByUserID(userId);
//
//        if (judgeRecords == null || judgeRecords.size() == 0) {
//            return null;
//        }
//
//        List<JudgeRecordRet> rets = new ArrayList<>(judgeRecords.size());
//
//        String uname = userService.getUserByID(userId).getName();
//        int i = 0;
//        for (var record : judgeRecords) {
//            rets.add(i++, new JudgeRecordRet(uname, record.getProbID(), record.getUserCode(), record.getSubmitDate(), record.getStatus()));
//        }
//
//        return rets;
//    }

    @GetMapping("/record")
    @ResponseBody
    public List<JudgeRecordRet> getAllJudgeRecordsByUserIDAndProbID(@RequestParam(name = "userID") int userID, @RequestParam(name = "probID") int probID) {
        List<JudgeRecord> judgeRecords = ojService.getJudgeRecordsByUserIDAndProbID(userID, probID);
        System.out.println(judgeRecords);
        if (judgeRecords == null || judgeRecords.size() == 0) {
            return null;
        }

        List<JudgeRecordRet> rets = new ArrayList<>(judgeRecords.size());

        String uname = userService.getUserByID(userID).getName();
        int i = 0;
        for (var record : judgeRecords) {
            rets.add(i++, new JudgeRecordRet(uname, record.getProbID(), record.getUserCode(), record.getSubmitDate(), record.getStatus()));
        }

        return rets;
    }
}
