package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Supplier;
import com.example.springboot.service.SupplierService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    @Resource
    private SupplierService supplierService;

    /**
     * 新增数据
     * @param supplier: 数据对象
     * @return Result
     */
    @PostMapping("/add")
    public Result add(@RequestBody Supplier supplier) {
        supplierService.add(supplier);
        return Result.success();
    }

    /**
     * 更新数据
     * @param supplier: 数据对象
     * @return Result
     */
    @PutMapping("/update")
    public Result update(@RequestBody Supplier supplier) {
        supplierService.update(supplier);
        return Result.success();
    }

    /**
     * 删除某个数据
     * @param id: 对象id
     * @return Result
     */
    @DeleteMapping("/deleteById/{id}")
    public Result delete(@PathVariable Integer id) {
        supplierService.deleteById(id);
        return Result.success();
    }

    /**
     * 删除一批数据
     * @param ids: 对象ids
     * @return Result
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        supplierService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 查询所有的数据
     * @return Result
     */
    @GetMapping("/selectAll")
    public Result selectAll(Supplier supplier) {
        List<Supplier> list = supplierService.selectAll(supplier);
        return Result.success(list);
    }

    /**
     * 通过路径Id传参查询某个对象 （/selectById/1 路径参数)
     * @param id: 对象id
     * @return Result
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Supplier supplier = supplierService.selectById(id);
        return Result.success(supplier);
    }

    /**
     *  分页查询数据
     * @param pageNum: 当前页数
     * @param pageSize: 每页大小
     * @return Result
     */
    @GetMapping("/selectPage")
    public Result selectPage( Supplier supplier,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Supplier> pageInfo = supplierService.selectPage(supplier, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
