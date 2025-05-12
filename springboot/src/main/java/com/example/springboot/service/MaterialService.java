package com.example.springboot.service;

import com.example.springboot.entity.Material;
import com.example.springboot.mapper.MaterialMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {

    @Resource
    public MaterialMapper materialMapper;

    public void add(Material material) {
        materialMapper.insert(material);
    }

    public void update(Material material) {
        materialMapper.updateById(material);
    }

    public void deleteById(Integer id) {
        materialMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public Material selectById(Integer id) {
        return materialMapper.selectById(id);
    }

    public List<Material> selectAll(Material material) {
        return materialMapper.selectAll(material);
    }

    public List<Material> selectList(Material material) {
        System.out.println(material);
        return null;
    }

    public PageInfo<Material> selectPage(Material material, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Material> list = materialMapper.selectAll(material);
        return PageInfo.of(list);
    }

}
