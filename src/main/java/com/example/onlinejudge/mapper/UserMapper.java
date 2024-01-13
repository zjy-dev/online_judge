package com.example.onlinejudge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.onlinejudge.model.Problem;
import com.example.onlinejudge.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Insert("insert into user (name, encodedPwd) values (#{name}, #{encodedPwd})")
    @Options(useGeneratedKeys = true,keyProperty = "id", keyColumn = "id")
    Integer insertUser(User user);
}
