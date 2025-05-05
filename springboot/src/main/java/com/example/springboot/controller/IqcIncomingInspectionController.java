package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.IqcIncomingInspection;
import com.example.springboot.service.IqcIncomingInspectionService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 获取来料检验折线图数据
     * @return
     */
    @GetMapping("/iqcLineData")
    public Result getIqcLineData() {

        return Result.success();
    }
}
