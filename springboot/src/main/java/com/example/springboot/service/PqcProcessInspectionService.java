package com.example.springboot.service;

import com.example.springboot.entity.PqcProcessInspection;
import com.example.springboot.mapper.PqcProcessInspectionMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PqcProcessInspectionService {

    @Resource
    public PqcProcessInspectionMapper pqcProcessInspectionMapper;

    public void add(PqcProcessInspection pqcProcessInspection) {
        pqcProcessInspectionMapper.insert(pqcProcessInspection);
    }

    public void update(PqcProcessInspection pqcProcessInspection) {
        pqcProcessInspectionMapper.updateById(pqcProcessInspection);
    }

    public void deleteById(Integer id) {
        pqcProcessInspectionMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public PqcProcessInspection selectById(Integer id) {
        return pqcProcessInspectionMapper.selectById(id);
    }

    public List<PqcProcessInspection> selectAll(PqcProcessInspection pqcProcessInspection) {
        return pqcProcessInspectionMapper.selectAll(pqcProcessInspection);
    }

    public PageInfo<PqcProcessInspection> selectPage(PqcProcessInspection pqcProcessInspection, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<PqcProcessInspection> list = pqcProcessInspectionMapper.selectAll(pqcProcessInspection);
        return PageInfo.of(list);
    }

    public List<Map<String, Object>> getWorkshopDefectiveCount(String startDate, String endDate) {
        return pqcProcessInspectionMapper.getWorkshopDefectiveCount(startDate, endDate);
    }

    public List<Map<String, Object>> getMaterialDefectiveCount(String startDate, String endDate) {
        return pqcProcessInspectionMapper.getMaterialDefectiveCount(startDate, endDate);
    }

    public List<Map<String, Object>> getTrendData(String startDate, String endDate) {
        return pqcProcessInspectionMapper.getTrendData(startDate, endDate);
    }

    public List<Map<String, Object>> getInspectionResultRatio(String startDate, String endDate) {
        return pqcProcessInspectionMapper.getInspectionResultRatio(startDate, endDate);
    }
}