package com.example.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.entity.IqcIncomingInspection;
import com.example.springboot.exception.CustomException;
import com.example.springboot.mapper.IqcIncomingInspectionMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IqcIncomingInspectionService {

    @Resource
    public IqcIncomingInspectionMapper iqcIncomingInspectionMapper;

    public void add(IqcIncomingInspection iqcIncomingInspection) {
        iqcIncomingInspectionMapper.insert(iqcIncomingInspection);
    }

    public void update(IqcIncomingInspection iqcIncomingInspection) {
        iqcIncomingInspectionMapper.updateById(iqcIncomingInspection);
    }

    public void deleteById(Integer id) {
        iqcIncomingInspectionMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public IqcIncomingInspection selectById(Integer id) {
        return iqcIncomingInspectionMapper.selectById(id);
    }

    public List<IqcIncomingInspection> selectAll(IqcIncomingInspection iqcIncomingInspection) {
        return iqcIncomingInspectionMapper.selectAll(iqcIncomingInspection);
    }

    public PageInfo<IqcIncomingInspection> selectPage(IqcIncomingInspection iqcIncomingInspection, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<IqcIncomingInspection> list = iqcIncomingInspectionMapper.selectAll(iqcIncomingInspection);
        return PageInfo.of(list);
    }
}