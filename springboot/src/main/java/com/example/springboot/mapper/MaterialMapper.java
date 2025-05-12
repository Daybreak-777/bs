package com.example.springboot.mapper;

import com.example.springboot.entity.Material;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MaterialMapper {

    List<Material> selectAll(Material material);

    @Select("select * from material where id = #{id}")
    Material selectById(Integer id);

    void insert(Material material);

    void updateById(Material material);

    @Delete("delete from material where id = #{id}")
    void deleteById(Integer id);

}
