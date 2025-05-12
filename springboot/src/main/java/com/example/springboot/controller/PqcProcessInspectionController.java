package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.PqcProcessInspection;
import com.example.springboot.service.PqcProcessInspectionService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pqcProcessInspection")
public class PqcProcessInspectionController {

    @Resource
    private PqcProcessInspectionService pqcProcessInspectionService;

    /**
     * 新增来料检验数据
     * @param pqcProcessInspection: 数据对象
     * @return Result
     */
    @PostMapping("/add")
    public Result add(@RequestBody PqcProcessInspection pqcProcessInspection) {
        pqcProcessInspectionService.add(pqcProcessInspection);
        return Result.success();
    }

    /**
     * 更新来料检验数据
     * @param pqcProcessInspection: 数据对象
     * @return Result
     */
    @PutMapping("/update")
    public Result update(@RequestBody PqcProcessInspection pqcProcessInspection) {
        pqcProcessInspectionService.update(pqcProcessInspection);
        return Result.success();
    }

    /**
     * 删除某个来料检验数据
     * @param id: 对象id
     * @return Result
     */
    @DeleteMapping("/deleteById/{id}")
    public Result delete(@PathVariable Integer id) {
        pqcProcessInspectionService.deleteById(id);
        return Result.success();
    }

    /**
     * 删除一批来料检验数据
     * @param ids: 对象ids
     * @return Result
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        pqcProcessInspectionService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 查询所有的来料检验数据
     * @return Result
     */
    @GetMapping("/selectAll")
    public Result selectAll(PqcProcessInspection pqcProcessInspection) {
        List<PqcProcessInspection> list = pqcProcessInspectionService.selectAll(pqcProcessInspection);
        return Result.success(list);
    }

    /**
     * 通过路径Id传参查询某个来料检验对象 （/selectById/1 路径参数)
     * @param id: 对象id
     * @return Result
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        PqcProcessInspection pqcProcessInspection = pqcProcessInspectionService.selectById(id);
        return Result.success(pqcProcessInspection);
    }

    /**
     * 分页查询来料检验数据
     * @param pageNum: 当前页数
     * @param pageSize: 每页大小
     * @return Result
     */
    @GetMapping("/selectPage")
    public Result selectPage( PqcProcessInspection pqcProcessInspection,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<PqcProcessInspection> pageInfo = pqcProcessInspectionService.selectPage(pqcProcessInspection, pageNum, pageSize);
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
        List<Map<String, Object>> data = pqcProcessInspectionService.getWorkshopDefectiveCount(startDate, endDate);
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
        List<Map<String, Object>> data = pqcProcessInspectionService.getMaterialDefectiveCount(startDate, endDate);
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
        List<Map<String, Object>> data = pqcProcessInspectionService.getTrendData(startDate, endDate);
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
        List<Map<String, Object>> data = pqcProcessInspectionService.getInspectionResultRatio(startDate, endDate);
        return Result.success(data);
    }
}
