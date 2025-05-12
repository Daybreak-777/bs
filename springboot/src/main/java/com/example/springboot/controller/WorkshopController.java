package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Workshop;
import com.example.springboot.service.WorkshopService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workshop")
public class WorkshopController {

    @Resource
    private WorkshopService workshopService;

    /**
     * 新增数据
     * @param workshop: 数据对象
     * @return Result
     */
    @PostMapping("/add")
    public Result add(@RequestBody Workshop workshop) {
        workshopService.add(workshop);
        return Result.success();
    }

    /**
     * 更新数据
     * @param workshop: 数据对象
     * @return Result
     */
    @PutMapping("/update")
    public Result update(@RequestBody Workshop workshop) {
        workshopService.update(workshop);
        return Result.success();
    }

    /**
     * 删除某个数据
     * @param id: 对象id
     * @return Result
     */
    @DeleteMapping("/deleteById/{id}")
    public Result delete(@PathVariable Integer id) {
        workshopService.deleteById(id);
        return Result.success();
    }

    /**
     * 删除一批数据
     * @param ids: 对象ids
     * @return Result
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        workshopService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 查询所有的数据
     * @return Result
     */
    @GetMapping("/selectAll")
    public Result selectAll(Workshop workshop) {
        List<Workshop> list = workshopService.selectAll(workshop);
        return Result.success(list);
    }

    /**
     * 通过路径Id传参查询某个对象 （/selectById/1 路径参数)
     * @param id: 对象id
     * @return Result
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Workshop workshop = workshopService.selectById(id);
        return Result.success(workshop);
    }

    /**
     *  分页查询数据
     * @param pageNum: 当前页数
     * @param pageSize: 每页大小
     * @return Result
     */
    @GetMapping("/selectPage")
    public Result selectPage( Workshop workshop,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Workshop> pageInfo = workshopService.selectPage(workshop, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
