package com.example.springboot.service;

import com.example.springboot.entity.Supplier;
import com.example.springboot.mapper.SupplierMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    @Resource
    public SupplierMapper supplierMapper;

    public void add(Supplier supplier) {
        supplierMapper.insert(supplier);
    }

    public void update(Supplier supplier) {
        supplierMapper.updateById(supplier);
    }

    public void deleteById(Integer id) {
        supplierMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public Supplier selectById(Integer id) {
        return supplierMapper.selectById(id);
    }

    public List<Supplier> selectAll(Supplier supplier) {
        return supplierMapper.selectAll(supplier);
    }

    public List<Supplier> selectList(Supplier supplier) {
        System.out.println(supplier);
        return null;
    }

    public PageInfo<Supplier> selectPage(Supplier supplier, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Supplier> list = supplierMapper.selectAll(supplier);
        return PageInfo.of(list);
    }

}
