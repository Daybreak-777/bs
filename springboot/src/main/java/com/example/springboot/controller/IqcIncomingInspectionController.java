package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.IqcIncomingInspection;
import com.example.springboot.service.IqcIncomingInspectionService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/iqcIncomingInspection")
public class IqcIncomingInspectionController {

    @Resource
    private IqcIncomingInspectionService iqcIncomingInspectionService;

    /**
     * 新增来料检验数据
     * @param iqcIncomingInspection: 数据对象
     * @return Result
     */
    @PostMapping("/add")
    public Result add(@RequestBody IqcIncomingInspection iqcIncomingInspection) {
        iqcIncomingInspectionService.add(iqcIncomingInspection);
        return Result.success();
    }

    /**
     * 更新来料检验数据
     * @param iqcIncomingInspection: 数据对象
     * @return Result
     */
    @PutMapping("/update")
    public Result update(@RequestBody IqcIncomingInspection iqcIncomingInspection) {
        iqcIncomingInspectionService.update(iqcIncomingInspection);
        return Result.success();
    }

    /**
     * 删除某个来料检验数据
     * @param id: 对象id
     * @return Result
     */
    @DeleteMapping("/deleteById/{id}")
    public Result delete(@PathVariable Integer id) {
        iqcIncomingInspectionService.deleteById(id);
        return Result.success();
    }

    /**
     * 删除一批来料检验数据
     * @param ids: 对象ids
     * @return Result
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        iqcIncomingInspectionService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 查询所有的来料检验数据
     * @return Result
     */
    @GetMapping("/selectAll")
    public Result selectAll(IqcIncomingInspection iqcIncomingInspection) {
        List<IqcIncomingInspection> list = iqcIncomingInspectionService.selectAll(iqcIncomingInspection);
        return Result.success(list);
    }

    /**
     * 通过路径Id传参查询某个来料检验对象 （/selectById/1 路径参数)
     * @param id: 对象id
     * @return Result
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        IqcIncomingInspection iqcIncomingInspection = iqcIncomingInspectionService.selectById(id);
        return Result.success(iqcIncomingInspection);
    }

    /**
     * 分页查询来料检验数据
     * @param pageNum: 当前页数
     * @param pageSize: 每页大小
     * @return Result
     */
    @GetMapping("/selectPage")
    public Result selectPage( IqcIncomingInspection iqcIncomingInspection,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<IqcIncomingInspection> pageInfo = iqcIncomingInspectionService.selectPage(iqcIncomingInspection, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 各供应商的不良数量对比数据
     * @param startDate: 开始日期
     * @param endDate：结束日期
     * @return Result
     */
    @GetMapping("/supplierDefectiveCount")
    public Result supplierDefectiveCount(@RequestParam String startDate, @RequestParam String endDate) {
        List<Map<String, Object>> data = iqcIncomingInspectionService.getSupplierDefectiveCount(startDate, endDate);
        return Result.success(data);
    }

    /**
     * 不同物料的不良数量对比数据
     * @param startDate: 开始日期
     * @param endDate：结束日期
     * @return Result
     */
    @GetMapping("/materialDefectiveCount")
    public Result materialDefectiveCount(@RequestParam String startDate, @RequestParam String endDate) {
        List<Map<String, Object>> data = iqcIncomingInspectionService.getMaterialDefectiveCount(startDate, endDate);
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
        List<Map<String, Object>> data = iqcIncomingInspectionService.getTrendData(startDate, endDate);
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
        List<Map<String, Object>> data = iqcIncomingInspectionService.getInspectionResultRatio(startDate, endDate);
        return Result.success(data);
    }
}
