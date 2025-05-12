package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.FqcProductInspection;
import com.example.springboot.service.FqcProductInspectionService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fqcProductInspection")
public class FqcProductInspectionController {

    @Resource
    private FqcProductInspectionService fqcProductInspectionService;

    /**
     * 新增来料检验数据
     * @param fqcProductInspection: 数据对象
     * @return Result
     */
    @PostMapping("/add")
    public Result add(@RequestBody FqcProductInspection fqcProductInspection) {
        fqcProductInspectionService.add(fqcProductInspection);
        return Result.success();
    }

    /**
     * 更新来料检验数据
     * @param fqcProductInspection: 数据对象
     * @return Result
     */
    @PutMapping("/update")
    public Result update(@RequestBody FqcProductInspection fqcProductInspection) {
        fqcProductInspectionService.update(fqcProductInspection);
        return Result.success();
    }

    /**
     * 删除某个来料检验数据
     * @param id: 对象id
     * @return Result
     */
    @DeleteMapping("/deleteById/{id}")
    public Result delete(@PathVariable Integer id) {
        fqcProductInspectionService.deleteById(id);
        return Result.success();
    }

    /**
     * 删除一批来料检验数据
     * @param ids: 对象ids
     * @return Result
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        fqcProductInspectionService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 查询所有的来料检验数据
     * @return Result
     */
    @GetMapping("/selectAll")
    public Result selectAll(FqcProductInspection fqcProductInspection) {
        List<FqcProductInspection> list = fqcProductInspectionService.selectAll(fqcProductInspection);
        return Result.success(list);
    }

    /**
     * 通过路径Id传参查询某个来料检验对象 （/selectById/1 路径参数)
     * @param id: 对象id
     * @return Result
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        FqcProductInspection fqcProductInspection = fqcProductInspectionService.selectById(id);
        return Result.success(fqcProductInspection);
    }

    /**
     * 分页查询来料检验数据
     * @param pageNum: 当前页数
     * @param pageSize: 每页大小
     * @return Result
     */
    @GetMapping("/selectPage")
    public Result selectPage( FqcProductInspection fqcProductInspection,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<FqcProductInspection> pageInfo = fqcProductInspectionService.selectPage(fqcProductInspection, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 各生产车间的不良数量对比数据
     * @param startDate: 开始日期
     * @param endDate：结束日期
     * @return Result
     */
    @GetMapping("/workshopDefectiveCount")
    public Result workshopDefectiveCount(@RequestParam String startDate, @RequestParam String endDate) {
        List<Map<String, Object>> data = fqcProductInspectionService.getWorkshopDefectiveCount(startDate, endDate);
        return Result.success(data);
    }

    /**
     * 不同产品的不良数量对比数据
     * @param startDate: 开始日期
     * @param endDate：结束日期
     * @return Result
     */
    @GetMapping("/productDefectiveCount")
    public Result productDefectiveCount(@RequestParam String startDate, @RequestParam String endDate) {
        List<Map<String, Object>> data = fqcProductInspectionService.getProductDefectiveCount(startDate, endDate);
        return Result.success(data);
    }

    /**
     * 来料数量、不良率趋势数据
     * @param startDate: 开始日期
     * @param endDate：结束日期
     * @return Result
     */
    @GetMapping("/trendData")
    public Result trendData(@RequestParam String startDate, @RequestParam String endDate) {
        List<Map<String, Object>> data = fqcProductInspectionService.getTrendData(startDate, endDate);
        return Result.success(data);
    }

    /**
     * 合格占比数据
     * @param startDate: 开始日期
     * @param endDate：结束日期
     * @return Result
     */
    @GetMapping("/inspectionResultRatio")
    public Result inspectionResultRatio(@RequestParam String startDate, @RequestParam String endDate) {
        List<Map<String, Object>> data = fqcProductInspectionService.getInspectionResultRatio(startDate, endDate);
        return Result.success(data);
    }
}
