package com.example.springboot.mapper;

import com.example.springboot.entity.Product;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProductMapper {

    List<Product> selectAll(Product product);

    @Select("select * from product where id = #{id}")
    Product selectById(Integer id);

    void insert(Product product);

    void updateById(Product product);

    @Delete("delete from product where id = #{id}")
    void deleteById(Integer id);

}
