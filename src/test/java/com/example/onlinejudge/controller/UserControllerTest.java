package com.example.onlinejudge.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.onlinejudge.controller.webRet.CommonIDRet;
import com.example.onlinejudge.controller.webRet.FinalCommonRet;
import com.example.onlinejudge.mapper.UserMapper;
import com.example.onlinejudge.model.User;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    UserController userController;
    @Autowired
    UserMapper userMapper;

    @Test
    public void hardTest() throws NoSuchAlgorithmException {
        var map = new HashMap<String, Object>();
        // 用户注册
        // TODO: Add Delete User Interface
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("name", "testUser");
        userMapper.delete(wrapper);
        map.put("name", "testUser");
        map.put("pwd", "123456");
        CommonIDRet ret = userController.login(new JSONObject(map).toString());

        assert ret.getCode() != 0;

        CommonIDRet registerRet = userController.register(map);
        assert registerRet.getCode() == 0 && registerRet.getId() > 0;

        assert userController.login(new JSONObject(map).toString()).getCode() == 0 : "用户登录失败";

        // 幂等性
        assert userMapper.delete(wrapper) == 1;
    }
}
