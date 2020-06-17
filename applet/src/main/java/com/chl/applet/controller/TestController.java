package com.chl.applet.controller;


import com.alibaba.fastjson.JSON;
import com.chl.applet.entity.ResultData;
import com.chl.applet.entity.User;
import com.chl.applet.service.RoleService;
import com.chl.applet.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class TestController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public TestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/u/list")
    public List<User> findAll() {
        List<User> list = userService.findAll();
        return list;
    }

    @PostMapping("/u/{ids}")
    public ResultData findByName(@PathVariable("ids") String ids) {
        String[] strings = ids.split(",");
        Map<String, Object> map = new HashMap<>();
        for (String s : strings) {
            List<User> list = userService.findByRoleId(Integer.parseInt(s));
            if (!list.isEmpty()) {
                map.put(roleService.findById(Integer.parseInt(s)).getNickName(), list);
            } else {
                log.info("=================没有获取到数据==================");
            }
        }

        if (map.isEmpty()) {
            return new ResultData(404, "未查询到数据", null);
        } else {
            log.info("===============成功======================");
            return new ResultData(200, "成功", map);
        }

    }

    @GetMapping("/mul")
    public String mul() {
        String str = "";
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= i; j++) {
                String mul = "";
                if (i * j < 10) {
                    mul = "0" + i * j;
                } else {
                    mul = "" + i * j;
                }
                if (j == i) {
                    str += j + "*" + i + "=" + mul + "\n";
                } else {
                    str += j + "*" + i + "=" + mul + " ";
                }
            }
        }
        System.out.println(str);
        return str;
    }

}
