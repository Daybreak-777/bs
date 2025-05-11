package com.example.springboot.mapper;

import com.example.springboot.entity.Supplier;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SupplierMapper {

    List<Supplier> selectAll(Supplier supplier);

    @Select("select * from supplier where id = #{id}")
    Supplier selectById(Integer id);

    void insert(Supplier supplier);

    void updateById(Supplier supplier);

    @Delete("delete from supplier where id = #{id}")
    void deleteById(Integer id);

}
