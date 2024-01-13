package com.example.onlinejudge.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.onlinejudge.mapper.UserMapper;
import com.example.onlinejudge.model.Problem;
import com.example.onlinejudge.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    @Autowired
    UserMapper userMapper;


    /**
     * @param name 用户名
     * @param encodedPwd 加密后的密码, 暂时只使用MD5
     * @return int类型, 0代表用户名重复, -1代表未知错误, >0代表用户ID
     */
    public int addUser(String name, String encodedPwd) throws RuntimeException {
        // 先判断用户名是否重复
        User user = this.getUserByName(name);
        if (user != null) {
            return 0;
        }

        // 若未重复则添加新用户
        user = new User(name, encodedPwd);

        Integer ret;

        try {
             ret = userMapper.insertUser(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
        if (ret == null || ret == 0) {
            return -1;
        }

        // 获取自动生成的ID
        if (user.getId() <= 0) {
            throw new RuntimeException("未知错误, 用户ID:" + user.getId() + "<= 0, 添加用户失败");
        }

        return user.getId();
    }

    public User getUserByID(int id) {
        return userMapper.selectById(id);
    }

    public User getUserByName(String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        return userMapper.selectOne(queryWrapper.eq("name", name));
    }

}
