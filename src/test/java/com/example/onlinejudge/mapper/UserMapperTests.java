package com.example.onlinejudge.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.onlinejudge.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

@SpringBootTest
public class UserMapperTests {
    @Autowired
    UserMapper userMapper;
    @Test
    public void hardTest() {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("name", "testUser");
        userMapper.delete(wrapper);
        User user = null;
        try {
            user = new User("testUser", "e10adc3949ba59abbe56e057f20f883e");
            assert userMapper.insertUser(user) == 1;
            userMapper.insertUser(user);
        } catch (Exception e) {
            assert e instanceof DuplicateKeyException;
            assert userMapper.delete(wrapper) == 1;
        }

        // 幂等性
        userMapper.delete(wrapper);
    }
}
