package com.chl.applet.service.impl;

import com.chl.applet.dao.ExperienceDatabaseDao;
import com.chl.applet.entity.ExperienceDatabase;
import com.chl.applet.service.ExperienceDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class ExperienceDatabaseServiceImpl implements ExperienceDatabaseService {
    private final ExperienceDatabaseDao experienceDatabaseDao;

    @Autowired
    public ExperienceDatabaseServiceImpl(ExperienceDatabaseDao experienceDatabaseDao) {
        this.experienceDatabaseDao = experienceDatabaseDao;
    }

    @Override
    public List<ExperienceDatabase> findAll() {
        return experienceDatabaseDao.selectAll();
    }

    @Override
    public ExperienceDatabase findOneById(Integer id) {
        return experienceDatabaseDao.selectOneById(id);
    }

    @Override
    public List<ExperienceDatabase> findByUserId(Integer userId) {
        return experienceDatabaseDao.selectByUserId(userId);
    }

    @Override
    public List<ExperienceDatabase> findByKeywords(String keywords, String startTime, String endTime, Integer audit, Integer userId) {
        return experienceDatabaseDao.selectByKeywords(keywords.trim(),startTime,endTime,audit,userId);
    }


    @Override
    public List<ExperienceDatabase> findChecked(Integer teamId, String keywords, String startTime, String endTime) {
        return experienceDatabaseDao.selectChecked(teamId, keywords, startTime, endTime);
    }

    @Override
    public List<ExperienceDatabase> findByTeamId(Integer teamId,Integer audit) {
        return experienceDatabaseDao.selectByTeamId(teamId,audit);
    }

    @Override
    public Integer findCheckNum(Integer teamId) {
        return experienceDatabaseDao.selectCheckNum(teamId);
    }

    @Override
    public void delete(Integer id) {
        experienceDatabaseDao.delete(id);
    }

    @Override
    public void remove(Integer id) {
        ExperienceDatabase experienceDatabase = experienceDatabaseDao.selectOneById(id);
        experienceDatabase.setAudit(0);
        experienceDatabaseDao.update(experienceDatabase);
    }

    @Override
    public void approve(Integer id) {
        ExperienceDatabase experienceDatabase = experienceDatabaseDao.selectOneById(id);
        experienceDatabase.setAudit(1);
        experienceDatabaseDao.update(experienceDatabase);
    }

    @Override
    public void sendCheck(Integer id) {
        ExperienceDatabase experienceDatabase = experienceDatabaseDao.selectOneById(id);
        experienceDatabase.setAudit(2);
        experienceDatabaseDao.update(experienceDatabase);
    }

    @Override
    public void noCheck(Integer id) {
        ExperienceDatabase experienceDatabase = experienceDatabaseDao.selectOneById(id);
        experienceDatabase.setAudit(3);
        experienceDatabaseDao.update(experienceDatabase);
    }

    @Override
    public void update(ExperienceDatabase experienceDatabase) {
        experienceDatabase.setAlterDate(new Date());
        experienceDatabaseDao.update(experienceDatabase);
    }

    @Override
    public void insert(ExperienceDatabase experienceDatabase) {
        experienceDatabase.setAudit(0);
        experienceDatabase.setRecommend(0);
        experienceDatabase.setEntryDate(new Date());
        experienceDatabaseDao.insert(experienceDatabase);
    }
}
