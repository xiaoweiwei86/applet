package com.chl.applet.controller;

import com.chl.applet.entity.Role;
import com.chl.applet.entity.Team;
import com.chl.applet.entity.User;
import com.chl.applet.service.RoleService;
import com.chl.applet.service.UserService;
import com.chl.applet.util.TeamUtil;

import com.chl.applet.util.MD5Utils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private static final Integer pageSize = 15;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @RequestMapping("/list")
    public String list(Map<String, Object> map, @RequestParam(defaultValue = "1") Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userService.findAll();
        PageInfo<User> page = new PageInfo<>(userList);
        map.put("list", userList);
        map.put("page", page);
        return "user/user_list";
    }

    @RequestMapping("/search")
    public String search(Map<String, Object> map, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam String key, @RequestParam Integer teamId) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userService.search(key.trim(), teamId);
        PageInfo<User> page = new PageInfo<>(userList);
        map.put("list", userList);
        map.put("page", page);
        map.put("flag", 1);
        map.put("key", key);
        map.put("teamId", teamId);
        if (teamId != null) {
            map.put("teamName", TeamUtil.getTeamNameById(teamId));
        }
        return "user/user_list";
    }

    @RequestMapping("/remove")
    public String remove(@RequestParam Integer id) {
        userService.remove(id);
        return "redirect:list";
    }

    @RequestMapping("/to_edit")
    public String toEdit(Map<String, Object> map, @RequestParam Integer id) {
        List<Team> teams = TeamUtil.getTeamList();
        User user = userService.findOne(id);
        List<Role> roles = roleService.findAll();
        map.put("teams", teams);
        map.put("roles", roles);
        map.put("user", user);
        return "user/user_edit";
    }

    @RequestMapping("/edit")
    public String edit(User user, HttpSession session) {
        userService.update(user);
        User one = userService.findOne(user.getId());
        User USER = (User) session.getAttribute("USER");
        if (USER.getId().equals(one.getId())) {
            session.setAttribute("USER", one);
        }
        return "redirect:to_edit?id=" + user.getId();
    }

    @RequestMapping("/self")
    public String self(Map<String, Object> map, @RequestParam Integer id) {
        User user = userService.findOne(id);
        map.put("user", user);
        return "user/user_self";
    }

    @RequestMapping("/to_editPwd")
    public String toEditPwd(Map<String, Object> map, @RequestParam("id") Integer id, HttpSession session) {
        User user = userService.findOne(id);
        User user1 = (User) session.getAttribute("USER");
        if (user1.getRole().getNickName().equals("超级管理员")) {
            map.put("admin", user1);
        }
        map.put("user", user);
        return "user/user_editPwd";
    }

    @RequestMapping("/editPwd")
    public String editPwd(@RequestParam String password1, @RequestParam Integer id) {
        User user = userService.findOne(id);
        if (user != null) {
            user.setPassword(password1);
            userService.update(user);
            return "redirect:to_editPwd?id=" + id;
        } else {
            return "redirect:/to_login";
        }

    }

    @RequestMapping("/isPwd")
    @ResponseBody
    public Boolean isPwd(@RequestParam String password, @RequestParam Integer id) {
        User user = userService.findOne(id);
        return MD5Utils.validate(password, user.getPassword());
    }

}
