package com.example.springboot.service;

import com.example.springboot.entity.Product;
import com.example.springboot.mapper.ProductMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Resource
    public ProductMapper productMapper;

    public void add(Product product) {
        productMapper.insert(product);
    }

    public void update(Product product) {
        productMapper.updateById(product);
    }

    public void deleteById(Integer id) {
        productMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public Product selectById(Integer id) {
        return productMapper.selectById(id);
    }

    public List<Product> selectAll(Product product) {
        return productMapper.selectAll(product);
    }

    public List<Product> selectList(Product product) {
        System.out.println(product);
        return null;
    }

    public PageInfo<Product> selectPage(Product product, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> list = productMapper.selectAll(product);
        return PageInfo.of(list);
    }

}
