package com.example.springboot.mapper;

import com.example.springboot.entity.IqcIncomingInspection;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface IqcIncomingInspectionMapper {

    List<IqcIncomingInspection> selectAll(IqcIncomingInspection iqcIncomingInspection);

    @Select("select * from iqc_incoming_inspection where id = #{id}")
    IqcIncomingInspection selectById(Integer id);

    void insert(IqcIncomingInspection iqcIncomingInspection);

    void updateById(IqcIncomingInspection iqcIncomingInspection);

    @Delete("delete from iqc_incoming_inspection where id = #{id}")
    void deleteById(Integer id);

    @MapKey("supplier")
    List<Map<String, Object>> getSupplierDefectiveCount(String startDate, String endDate);
    @MapKey("materialName")
    List<Map<String, Object>> getMaterialDefectiveCount(String startDate, String endDate);
    @MapKey("inspectionDate")
    List<Map<String, Object>> getTrendData(String startDate, String endDate);
    @MapKey("inspectionResult")
    List<Map<String, Object>> getInspectionResultRatio(String startDate, String endDate);
}