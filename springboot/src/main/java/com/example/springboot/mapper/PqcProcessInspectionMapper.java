package com.example.springboot.mapper;

import com.example.springboot.entity.PqcProcessInspection;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface PqcProcessInspectionMapper {

    List<PqcProcessInspection> selectAll(PqcProcessInspection pqcProcessInspection);

    @Select("select * from pqc_process_inspection where id = #{id}")
    PqcProcessInspection selectById(Integer id);

    void insert(PqcProcessInspection pqcProcessInspection);

    void updateById(PqcProcessInspection pqcProcessInspection);

    @Delete("delete from pqc_process_inspection where id = #{id}")
    void deleteById(Integer id);

    @MapKey("workshop")
    List<Map<String, Object>> getWorkshopDefectiveCount(String startDate, String endDate);
    @MapKey("material")
    List<Map<String, Object>> getMaterialDefectiveCount(String startDate, String endDate);
    @MapKey("inspectionDate")
    List<Map<String, Object>> getTrendData(String startDate, String endDate);
    @MapKey("inspectionResult")
    List<Map<String, Object>> getInspectionResultRatio(String startDate, String endDate);
}