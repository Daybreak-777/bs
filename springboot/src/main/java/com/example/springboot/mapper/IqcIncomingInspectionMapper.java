package com.example.springboot.mapper;

import com.example.springboot.entity.IqcIncomingInspection;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IqcIncomingInspectionMapper {

    List<IqcIncomingInspection> selectAll(IqcIncomingInspection iqcIncomingInspection);

    @Select("select * from iqc_incoming_inspection where id = #{id}")
    IqcIncomingInspection selectById(Integer id);

    void insert(IqcIncomingInspection iqcIncomingInspection);

    void updateById(IqcIncomingInspection iqcIncomingInspection);

    @Delete("delete from iqc_incoming_inspection where id = #{id}")
    void deleteById(Integer id);
}