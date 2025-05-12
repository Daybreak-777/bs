package com.example.springboot.service;

import com.example.springboot.entity.FqcProductInspection;
import com.example.springboot.mapper.FqcProductInspectionMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FqcProductInspectionService {

    @Resource
    public FqcProductInspectionMapper fqcProductInspectionMapper;

    public void add(FqcProductInspection fqcProductInspection) {
        fqcProductInspectionMapper.insert(fqcProductInspection);
    }

    public void update(FqcProductInspection fqcProductInspection) {
        fqcProductInspectionMapper.updateById(fqcProductInspection);
    }

    public void deleteById(Integer id) {
        fqcProductInspectionMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public FqcProductInspection selectById(Integer id) {
        return fqcProductInspectionMapper.selectById(id);
    }

    public List<FqcProductInspection> selectAll(FqcProductInspection fqcProductInspection) {
        return fqcProductInspectionMapper.selectAll(fqcProductInspection);
    }

    public PageInfo<FqcProductInspection> selectPage(FqcProductInspection fqcProductInspection, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<FqcProductInspection> list = fqcProductInspectionMapper.selectAll(fqcProductInspection);
        return PageInfo.of(list);
    }

    public List<Map<String, Object>> getWorkshopDefectiveCount(String startDate, String endDate) {
        return fqcProductInspectionMapper.getWorkshopDefectiveCount(startDate, endDate);
    }

    public List<Map<String, Object>> getProductDefectiveCount(String startDate, String endDate) {
        return fqcProductInspectionMapper.getProductDefectiveCount(startDate, endDate);
    }

    public List<Map<String, Object>> getTrendData(String startDate, String endDate) {
        return fqcProductInspectionMapper.getTrendData(startDate, endDate);
    }

    public List<Map<String, Object>> getInspectionResultRatio(String startDate, String endDate) {
        return fqcProductInspectionMapper.getInspectionResultRatio(startDate, endDate);
    }
}