package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Admin;
import com.example.springboot.service.AdminService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    // get: 查询 post: 更新 put: 修改 delete: 删除
    @Resource
    private AdminService AdminService;

    /**
     * 新增数据
     * @param admin: 数据对象
     * @return Result
     */
    // @RequestBody: 把前端传来的json字符串映射为java的对象或者数组
    @PostMapping("/add")
    public Result add(@RequestBody Admin admin) {
        AdminService.add(admin);
        return Result.success();
    }

    /**
     * 更新数据
     * @param admin: 数据对象
     * @return Result
     */
    // @RequestBody: 把前端传来的json字符串映射为java的对象或者数组
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin) {
        AdminService.update(admin);
        return Result.success();
    }

    /**
     * 删除某个数据
     * @param id: 对象id
     * @return Result
     */
    @DeleteMapping("/deleteById/{id}")
    public Result delete(@PathVariable Integer id) {
        AdminService.deleteById(id);
        return Result.success();
    }

    /**
     * 删除一批数据
     * @param ids: 对象ids
     * @return Result
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        AdminService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 查询所有的数据
     * @return Result
     */
    @GetMapping("/selectAll")
    public Result selectAll(Admin admin) {
        List<Admin> list = AdminService.selectAll(admin);
        return Result.success(list);
    }

    /**
     * 通过路径Id传参查询某个对象 （/selectById/1 路径参数)
     * @param id: 对象id
     * @return Result
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Admin admin = AdminService.selectById(id);
        return Result.success(admin);
    }

    /**
     *  分页查询数据
     * @param pageNum: 当前页数
     * @param pageSize: 每页大小
     * @return Result
     */
    @GetMapping("/selectPage")
    public Result selectPage( Admin admin,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Admin> pageInfo = AdminService.selectPage(admin, pageNum, pageSize);
        return Result.success(pageInfo);
    }

//    /**
//     * 通过url传参查询某个对象（/selectOne?id=1&no=xx 请求参数）
//     * @return Result
//     */
//    @GetMapping("/selectOne")
//    public Result selectOne(@RequestParam Integer id,@RequestParam(required = false) String no){
//        Admin admin = AdminService.selectById(id);
//        return Result.success(admin);
//    }
//
//    /**
//     * get请求对象可以接受多个参数，取决于对象里的面的属性，并且这些参数都不是必传的
//     * @return Result
//     */
//    @GetMapping("/selectList")
//    public Result selectOne(admin admin){
//        List<admin> list = AdminService.selectList(admin);
//        return Result.success(list);
//    }
}
