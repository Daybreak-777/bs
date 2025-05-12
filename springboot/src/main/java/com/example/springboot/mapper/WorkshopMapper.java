package com.example.springboot.mapper;

import com.example.springboot.entity.Workshop;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface WorkshopMapper {

    List<Workshop> selectAll(Workshop workshop);

    @Select("select * from workshop where id = #{id}")
    Workshop selectById(Integer id);

    void insert(Workshop workshop);

    void updateById(Workshop workshop);

    @Delete("delete from workshop where id = #{id}")
    void deleteById(Integer id);

}
