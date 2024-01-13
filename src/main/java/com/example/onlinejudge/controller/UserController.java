package com.example.onlinejudge.controller;

import com.example.onlinejudge.controller.webRet.FinalCommonRet;
import com.example.onlinejudge.controller.webRet.UserRet;
import com.example.onlinejudge.model.User;
import com.example.onlinejudge.service.UserService;
import com.example.onlinejudge.controller.webRet.CommonRet;
import com.example.onlinejudge.controller.webRet.CommonIDRet;

import org.apache.tomcat.util.security.MD5Encoder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public CommonIDRet login(@RequestBody String data) throws NoSuchAlgorithmException {
        var obj = new JSONObject(data);
        String name = obj.getString("name");
        String pwd = obj.getString("pwd");

        // 校参
        if (name.length() == 0 || pwd.length() == 0 || name.length() > 50 || pwd.length() > 50) {
            return new CommonIDRet(-1, "用户名或密码格式不合法", -1);
        }


        MessageDigest md = MessageDigest.getInstance("MD5");
        String encodedPwd = MD5Encoder.encode(md.digest(pwd.getBytes()));

        User user;
        try {
            user = userService.getUserByName(name);
        } catch (Exception e) {
            return new CommonIDRet(-1, "查询该用户失败", -1);
        }


        if (user == null) {
            return new CommonIDRet(-1, "用户不存在", -1);
        }
        if (user.getEncodedPwd().equals(encodedPwd)) {
            return new CommonIDRet(0, "登陆成功", user.getId());
        }

        return new CommonIDRet(-1, "密码错误", -1);
    }

    @PostMapping("/register")
    @Transactional
    public CommonIDRet register(@RequestBody Map<String, Object> map) throws NoSuchAlgorithmException {
        String name = (String) map.get("name");
        String pwd = (String) map.get("pwd");

        // 校参
        if (name.length() == 0 || pwd.length() == 0 || name.length() > 50 || pwd.length() > 50) {
            return new CommonIDRet(-1, "用户名或密码格式不合法", -1);
        }

        MessageDigest md = MessageDigest.getInstance("MD5");
        String encodedPwd = MD5Encoder.encode(md.digest(pwd.getBytes()));

        int retOrID = userService.addUser(name, encodedPwd);

        System.out.println(retOrID);

        if (retOrID == 0) {
            return new CommonIDRet(-1, "用户名: " + name + " 重复", -1);
        } else if (retOrID == -1) {
            return new CommonIDRet(-1, "添加用户失败", -1);
        }

        return new CommonIDRet(0, "添加用户成功", retOrID);
    }

    @GetMapping("/id/{id}")
    @ResponseBody
    public UserRet getProblemByName(@PathVariable(name = "id") int id) {
        // 校参
        if (id <= 0) {
            return new UserRet(-1, "用户ID不合法", -1, "");
        }

        User user;
        try {
            user = userService.getUserByID(id);
        } catch (Exception e) {
            return new UserRet(-1, "查询用户失败", -1, "");
        }

        if (user == null) {
            return new UserRet(-1, "用户不存在", -1, "");
        }

        return new UserRet(0, "成功查询到用户", user.getId(), user.getName());
    }
}
