package com.example.onlinejudge.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.onlinejudge.mapper.UserMapper;
import com.example.onlinejudge.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class UserServiceTests {
    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Test
    public void userServiceTest() {
        // TODO: Add Delete User Service
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("name", "testUser");
        userMapper.delete(wrapper);
        assert userService.getUserByName("testUser") == null;
        assert userService.addUser("testUser", "e10adc3949ba59abbe56e057f20f883e") > 0;
        assert userService.addUser("testUser", "e10adc3949ba59abbe56e057f20f883e") == 0;

        // 幂等性
        assert userMapper.delete(wrapper) == 1;
    }

}
