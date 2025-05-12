package com.example.springboot.mapper;

import com.example.springboot.entity.FqcProductInspection;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface FqcProductInspectionMapper {

    List<FqcProductInspection> selectAll(FqcProductInspection fqcproductInspection);

    @Select("select * from fqc_product_inspection where id = #{id}")
    FqcProductInspection selectById(Integer id);

    void insert(FqcProductInspection fqcproductInspection);

    void updateById(FqcProductInspection fqcproductInspection);

    @Delete("delete from fqc_product_inspection where id = #{id}")
    void deleteById(Integer id);

    @MapKey("workshop")
    List<Map<String, Object>> getWorkshopDefectiveCount(String startDate, String endDate);
    @MapKey("product")
    List<Map<String, Object>> getProductDefectiveCount(String startDate, String endDate);
    @MapKey("inspectionDate")
    List<Map<String, Object>> getTrendData(String startDate, String endDate);
    @MapKey("inspectionResult")
    List<Map<String, Object>> getInspectionResultRatio(String startDate, String endDate);
}