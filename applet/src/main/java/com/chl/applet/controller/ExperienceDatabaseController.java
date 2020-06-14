package com.chl.applet.controller;

import com.chl.applet.entity.Comment;
import com.chl.applet.entity.ExperienceDatabase;
import com.chl.applet.entity.User;
import com.chl.applet.service.CommentService;
import com.chl.applet.service.ExperienceDatabaseService;
import com.chl.applet.service.MailService;
import com.chl.applet.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ed")
public class ExperienceDatabaseController {
    @Value("${web-url}")
    private String webUrl;
    private final ExperienceDatabaseService experienceDatabaseService;
    private final UserService userService;
    private final CommentService commentService;
    private final MailService mailService;
    private static final Integer pageSize = 20;
    private static final String orderBy = "entry_date desc";

    @Autowired
    public ExperienceDatabaseController(ExperienceDatabaseService experienceDatabaseService, UserService userService, CommentService commentService, MailService mailService) {
        this.experienceDatabaseService = experienceDatabaseService;
        this.userService = userService;
        this.commentService = commentService;
        this.mailService = mailService;
    }

    @RequestMapping("list")
    public String list(Map<String, Object> map, @RequestParam(defaultValue = "1") Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize, orderBy);
        List<ExperienceDatabase> list = experienceDatabaseService.findAll();
        PageInfo<ExperienceDatabase> page = new PageInfo<>(list);
        map.put("list", list);
        map.put("page", page);
        return "jysjk/jysjk_list";
    }

    @RequestMapping("search")
    public String search(Map<String, Object> map,HttpSession session, @RequestParam String keywords, @RequestParam String startTime, @RequestParam String endTime, @RequestParam(defaultValue = "0") String flag,@RequestParam(defaultValue = "1") Integer pageNum) {
        User user = (User) session.getAttribute("USER");
        PageHelper.startPage(pageNum, pageSize, orderBy);
        if(flag.equals("0")){
            List<ExperienceDatabase> list = experienceDatabaseService.findByKeywords(keywords, startTime, endTime,null,null);
            PageInfo<ExperienceDatabase> page = new PageInfo<>(list);
            map.put("list", list);
            map.put("page", page);
        }else {
            List<ExperienceDatabase> list = experienceDatabaseService.findByKeywords(keywords, startTime, endTime,null,user.getId());
            PageInfo<ExperienceDatabase> page = new PageInfo<>(list);
            map.put("list", list);
            map.put("page", page);
            map.put("flag",flag);
        }
        map.put("isSearch", 1);
        map.put("keywords", keywords);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        if(flag.equals("0")){
            return "jysjk/jysjk_list";
        }else {
            return "jysjk/jysjk_list_myself";
        }

    }

    @RequestMapping("searchChecked")
    public String searchChecked(Map<String, Object> map, HttpSession session, @RequestParam String keywords, @RequestParam String startTime, @RequestParam String endTime, @RequestParam(defaultValue = "1") Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize, orderBy);
        List<ExperienceDatabase> checked = experienceDatabaseService.findChecked(getTeamId(session),keywords,startTime,endTime);
        PageInfo<ExperienceDatabase> page = new PageInfo<>(checked);
        map.put("list", checked);
        map.put("page", page);
        map.put("flag", 1);
        map.put("keywords", keywords);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        return "jysjk/jysjk_list_checked";
    }


    /**
     * 待审核列表
     *
     * @param map
     * @param pageNum
     * @return
     */
    @RequestMapping("nocheck")
    public String noCheck(Map<String, Object> map, HttpSession session, @RequestParam(defaultValue = "1") Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize, orderBy);
        List<ExperienceDatabase> list = experienceDatabaseService.findByTeamId(getTeamId(session), 2);
        PageInfo<ExperienceDatabase> page = new PageInfo<>(list);
        map.put("list", list);
        map.put("page", page);
        return "jysjk/jysjk_list_nocheck";
    }


    /**
     * 已审核完成列表
     *
     * @param map
     * @param pageNum
     * @return
     */
    @RequestMapping("checked")
    public String checked(Map<String, Object> map, HttpSession session, @RequestParam(defaultValue = "1") Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize, orderBy);
        List<ExperienceDatabase> list = experienceDatabaseService.findByTeamId(getTeamId(session), 1);
        PageInfo<ExperienceDatabase> page = new PageInfo<>(list);
        map.put("list", list);
        map.put("page", page);
        return "jysjk/jysjk_list_checked";
    }

    /**
     * 判断当前登录账户是否为管理员，如果是，将teamId为空 返回teamId 值
     *
     * @param session
     * @return
     */
    public Integer getTeamId(HttpSession session) {
        Integer teamId = null;
        User user = (User) session.getAttribute("USER");
        if (!user.getRole().getNickName().equals("超级管理员")) {
            teamId = user.getTeamId();
        }

        return teamId;
    }

    @RequestMapping("myself")
    public String myself(Map<String, Object> map, @RequestParam Integer userId, @RequestParam(defaultValue = "1") Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize, orderBy);
        List<ExperienceDatabase> list = experienceDatabaseService.findByUserId(userId);
        PageInfo<ExperienceDatabase> page = new PageInfo<>(list);
        map.put("list", list);
        map.put("page", page);
        return "jysjk/jysjk_list_myself";
    }

    @RequestMapping("to_add")
    public String toAdd() {
        return "jysjk/jysjk_add";
    }


    @RequestMapping("add")
    public String add(HttpSession session, ExperienceDatabase experienceDatabase) throws MessagingException {
        User user = (User) session.getAttribute("USER");
        experienceDatabase.setEntryPerson(user.getRealName());
        experienceDatabase.setUserId(user.getId());
        experienceDatabaseService.insert(experienceDatabase);
        return "redirect:myself?userId=" + user.getId();
    }

    @RequestMapping("to_edit")
    public String toEdit(Map<String, Object> map, Integer id) {
        ExperienceDatabase experienceDatabase = experienceDatabaseService.findOneById(id);
        List<Comment> comments = commentService.findByEdId(id);
        map.put("experienceDatabase", experienceDatabase);
        map.put("comments", comments);
        return "jysjk/jysjk_edit";
    }

    @RequestMapping("to_check")
    public String toCheck(Map<String, Object> map, Integer id) {
        ExperienceDatabase experienceDatabase = experienceDatabaseService.findOneById(id);
        List<Comment> comments = commentService.findByEdId(id);
        map.put("experienceDatabase", experienceDatabase);
        map.put("comments", comments);
        return "jysjk/jysjk_check";
    }

    @RequestMapping("view")
    public String view(Map<String, Object> map, Integer id) {
        ExperienceDatabase experienceDatabase = experienceDatabaseService.findOneById(id);
        List<Comment> comments = commentService.findByEdId(id);
        map.put("experienceDatabase", experienceDatabase);
        map.put("comments", comments);
        return "jysjk/jysjk_view";
    }

    /**
     * @param id
     * @return
     */
    @RequestMapping("comment")
    @ResponseBody
    public void comment(HttpSession session, @RequestParam Integer id, @RequestParam String content) {
        User user = (User) session.getAttribute("USER");
        Comment comment = new Comment();
        comment.setEdId(id);
        comment.setUserId(user.getId());
        comment.setContent(content);
        commentService.add(comment);
        experienceDatabaseService.noCheck(id);
        Integer num = experienceDatabaseService.findCheckNum(user.getTeamId());
        session.setAttribute("NUM", num);
        ExperienceDatabase experienceDatabase = experienceDatabaseService.findOneById(id);
        try {
            mailService.sendHtmlMail(experienceDatabase.getUser().getEmail(), "经验数据库被驳回通知", "<span>您有一条经验数据被驳回：</span><a href='" + webUrl + "to_edit?id=" + experienceDatabase.getId() + "'>点击查看</a>");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("edit")
    public String edit(HttpSession session,ExperienceDatabase experienceDatabase) {
        experienceDatabase.setAudit(0);
        experienceDatabaseService.update(experienceDatabase);
        User user =(User) session.getAttribute("USER");
        if(user.getId()==experienceDatabase.getUserId()){
            return "redirect:myself?userId=" + experienceDatabase.getUserId();
        }else {
            return "redirect:list";
        }
    }


    /**
     * 直接删除
     *
     * @param id
     * @return
     */
    @RequestMapping("delete")
    public String delete(HttpSession session, @RequestParam Integer id, @RequestParam Integer pageNum) {
        User user = (User) session.getAttribute("USER");
        ExperienceDatabase experienceDatabase = experienceDatabaseService.findOneById(id);
        if (user.getId() == experienceDatabase.getUserId()) {
            commentService.removeByEdId(id);
            experienceDatabaseService.delete(id);
            return "redirect:myself?userId=" + user.getId() + "&pageNum=" + pageNum;
        } else {
            experienceDatabaseService.delete(id);
            return "redirect:list?pageNum=" + pageNum;
        }
    }

    /**
     * 移除，在线不可搜索或查询
     *
     * @param id
     * @return
     */
    @RequestMapping("remove")
    public String remove(HttpSession session, @RequestParam Integer id, @RequestParam Integer pageNum) {
        User user = (User) session.getAttribute("USER");
        experienceDatabaseService.remove(id);
        if (user.getRole().getNickName().equals("超级管理员")) {
            return "redirect:list?pageNum=" + pageNum;
        } else {
            return "redirect:checked?teamId=" + user.getTeamId();
        }
    }

    /**
     * 经验数据审核通过 设置状态为 1 已审核
     *
     * @param id
     * @return
     */
    @RequestMapping("/approve")
    public String approve(HttpSession session, @RequestParam Integer id) {
        User user = (User) session.getAttribute("USER");
        experienceDatabaseService.approve(id);
        Integer num = experienceDatabaseService.findCheckNum(user.getTeamId());
        session.setAttribute("NUM", num);
        if (user.getRole().getNickName().equals("超级管理员")) {
            return "redirect:list";
        } else {
            return "redirect:checked?teamId=" + user.getTeamId();
        }
    }

    /**
     * 发送审核请求，更改状态。
     * 查找同部门部长邮箱并发送邮件，并返回发送邮件是否成功结果
     *
     * @param userId
     * @param edId
     */
    @RequestMapping("sendCheck")
    @ResponseBody
    public void sendCheck(HttpSession session, @RequestParam Integer userId, @RequestParam Integer edId) {
        User user = userService.findOne(userId);
        experienceDatabaseService.sendCheck(edId);
        Integer num = experienceDatabaseService.findCheckNum(user.getTeamId());
        session.setAttribute("NUM", num);
        List<User> users = userService.findByTeamId(user.getTeamId());
        for (User user1 : users) {
            if (user1.getRole().getNickName().equals("部长")) {
                try {
                    mailService.sendHtmlMail(user1.getEmail(), "经验数据审核通知", "<span>您有一条经验数据待审核：</span><a href='" + webUrl + "nocheck?teamId=" + user.getTeamId() + "'>审核</a>");
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
