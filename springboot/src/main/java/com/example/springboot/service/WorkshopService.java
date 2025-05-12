package com.example.springboot.service;

import com.example.springboot.entity.Workshop;
import com.example.springboot.mapper.WorkshopMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkshopService {

    @Resource
    public WorkshopMapper workshopMapper;

    public void add(Workshop workshop) {
        workshopMapper.insert(workshop);
    }

    public void update(Workshop workshop) {
        workshopMapper.updateById(workshop);
    }

    public void deleteById(Integer id) {
        workshopMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public Workshop selectById(Integer id) {
        return workshopMapper.selectById(id);
    }

    public List<Workshop> selectAll(Workshop workshop) {
        return workshopMapper.selectAll(workshop);
    }

    public List<Workshop> selectList(Workshop workshop) {
        System.out.println(workshop);
        return null;
    }

    public PageInfo<Workshop> selectPage(Workshop workshop, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Workshop> list = workshopMapper.selectAll(workshop);
        return PageInfo.of(list);
    }

}
