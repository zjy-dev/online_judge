package com.example.onlinejudge;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.onlinejudge.controller.OjController;
import com.example.onlinejudge.controller.ProblemController;
import com.example.onlinejudge.controller.UserController;
import com.example.onlinejudge.controller.webRet.CommonIDRet;
import com.example.onlinejudge.controller.webRet.FinalCommonRet;
import com.example.onlinejudge.mapper.UserMapper;
import com.example.onlinejudge.model.Judge;
import com.example.onlinejudge.model.Problem;
import com.example.onlinejudge.controller.webRet.JudgeCode;
import com.example.onlinejudge.model.User;
import com.example.onlinejudge.service.OjService;
import com.example.onlinejudge.service.UserService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class OnlineJudgeApplicationTests {

    @Autowired
    ProblemController probController;
    @Autowired
    OjController ojController;
    @Autowired
    UserController userController;

    @Test
    public void basicProblemTest() throws IOException, InterruptedException {
        var map = new HashMap<String, Object>();
        map.put("name", "A+B");
        map.put("description", "给定A和B为两个整数, 输出A+B的结果, 结果不会超过C++中int的表示范围");
        map.put("sampleInput", "6 8");
        map.put("sampleOutput", "14");
        map.put("inputs", new String[]{"1 2", "65 89", "114514 114514"});
        map.put("outputs", new String[]{"3", "154", "229028"});
        map.put("timeLimits", new Integer[]{1000, 1000, 2000});
        Problem problem = probController.getProblemByID("A+B").getProblem();
        if (problem == null) {
            CommonIDRet ret = probController.addProblem(new JSONObject(map).toString());
            assert !(ret.getCode() < 0 || ret.getId() < 0) : "添加题目失败";
            problem = probController.getProblemByID(ret.getId()).getProblem();
        }

        assert problem != null;
        int ID = problem.getId();
        assert ID > 0 : "题目ID异常";

        // 评测
        map.clear();
        map.put("code",
                "#include <bits/stdc++.h>\n" +
                        "using namespace std;\n" +
                        "\n" +
                        "\n" +
                        "int main() {\n" +
                        "    int a, b;\n" +
                        "    cin >> a >> b;\n" +
                        "    cout << a + b << endl;\n" +
                        "}");
        map.put("problemId", String.valueOf(ID));
        map.put("userId", String.valueOf(1));

        JSONObject obj = new JSONObject(map);

        var ret = ojController.codeSubmit(obj.toString());

        assert ret.getJudgeCode() == JudgeCode.ACCEPTED : "结果应该通过";
    }

    @Test
    public void selectJudgeRecordTest() {
        System.out.println(ojController.getAllJudgeRecordsByUserIDAndProbID(3, 6));
    }
}
