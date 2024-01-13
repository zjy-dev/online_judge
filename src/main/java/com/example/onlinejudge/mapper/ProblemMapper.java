package com.example.onlinejudge.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.onlinejudge.model.Problem;
import com.example.onlinejudge.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface ProblemMapper extends BaseMapper<Problem> {
    @Insert("insert into problem (name, description, sampleInput, sampleOutput) values (#{name}, #{description}, #{sampleInput}, #{sampleOutput})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Integer insertProblem(Problem problem);
}
