package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Product;
import com.example.springboot.service.ProductService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    /**
     * 新增数据
     * @param product: 数据对象
     * @return Result
     */
    @PostMapping("/add")
    public Result add(@RequestBody Product product) {
        productService.add(product);
        return Result.success();
    }

    /**
     * 更新数据
     * @param product: 数据对象
     * @return Result
     */
    @PutMapping("/update")
    public Result update(@RequestBody Product product) {
        productService.update(product);
        return Result.success();
    }

    /**
     * 删除某个数据
     * @param id: 对象id
     * @return Result
     */
    @DeleteMapping("/deleteById/{id}")
    public Result delete(@PathVariable Integer id) {
        productService.deleteById(id);
        return Result.success();
    }

    /**
     * 删除一批数据
     * @param ids: 对象ids
     * @return Result
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        productService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 查询所有的数据
     * @return Result
     */
    @GetMapping("/selectAll")
    public Result selectAll(Product product) {
        List<Product> list = productService.selectAll(product);
        return Result.success(list);
    }

    /**
     * 通过路径Id传参查询某个对象 （/selectById/1 路径参数)
     * @param id: 对象id
     * @return Result
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Product product = productService.selectById(id);
        return Result.success(product);
    }

    /**
     *  分页查询数据
     * @param pageNum: 当前页数
     * @param pageSize: 每页大小
     * @return Result
     */
    @GetMapping("/selectPage")
    public Result selectPage( Product product,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Product> pageInfo = productService.selectPage(product, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
