package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Employee;
import com.example.springboot.service.EmployeeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    // get: 查询 post: 更新 put: 修改 delete: 删除
    @Resource
    private EmployeeService employeeService;

    /**
     * 新增数据
     * @param employee: 数据对象
     * @return Result
     */
    // @RequestBody: 把前端传来的json字符串映射为java的对象或者数组
    @PostMapping("/add")
    public Result add(@RequestBody Employee employee) {
        employeeService.add(employee);
        return Result.success();
    }

    /**
     * 更新数据
     * @param employee: 数据对象
     * @return Result
     */
    // @RequestBody: 把前端传来的json字符串映射为java的对象或者数组
    @PutMapping("/update")
    public Result update(@RequestBody Employee employee) {
        employeeService.update(employee);
        return Result.success();
    }

    /**
     * 删除某个数据
     * @param id: 对象id
     * @return Result
     */
    @DeleteMapping("/deleteById/{id}")
    public Result delete(@PathVariable Integer id) {
        employeeService.deleteById(id);
        return Result.success();
    }

    /**
     * 删除一批数据
     * @param ids: 对象ids
     * @return Result
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        employeeService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 查询所有的数据
     * @return Result
     */
    @GetMapping("/selectAll")
    public Result selectAll(Employee employee) {
        List<Employee> list = employeeService.selectAll(employee);
        return Result.success(list);
    }

    /**
     * 通过路径Id传参查询某个对象 （/selectById/1 路径参数)
     * @param id: 对象id
     * @return Result
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Employee employee = employeeService.selectById(id);
        return Result.success(employee);
    }

    /**
     *  分页查询数据
     * @param pageNum: 当前页数
     * @param pageSize: 每页大小
     * @return Result
     */
    @GetMapping("/selectPage")
    public Result selectPage( Employee employee,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Employee> pageInfo = employeeService.selectPage(employee, pageNum, pageSize);
        return Result.success(pageInfo);
    }

//    /**
//     * 通过url传参查询某个对象（/selectOne?id=1&no=xx 请求参数）
//     * @return Result
//     */
//    @GetMapping("/selectOne")
//    public Result selectOne(@RequestParam Integer id,@RequestParam(required = false) String no){
//        Employee employee = employeeService.selectById(id);
//        return Result.success(employee);
//    }
//
//    /**
//     * get请求对象可以接受多个参数，取决于对象里的面的属性，并且这些参数都不是必传的
//     * @return Result
//     */
//    @GetMapping("/selectList")
//    public Result selectOne(Employee employee){
//        List<Employee> list = employeeService.selectList(employee);
//        return Result.success(list);
//    }
}
