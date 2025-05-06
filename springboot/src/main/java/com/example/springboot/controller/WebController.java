package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.Employee;
import com.example.springboot.exception.CustomException;
import com.example.springboot.service.AdminService;
import com.example.springboot.service.EmployeeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class WebController {

    @Resource
    private EmployeeService employeeService;
    @Resource
    private AdminService adminService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring Boot!";
    }

    /**
     * 管理员或者员工登录
     * @param account: 数据对象
     * @return Result
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account result = null;
        if ("ADMIN".equals(account.getRole())) {
            result = adminService.login(account);
        }
        else if ("EMP".equals(account.getRole())) {
            result = employeeService.login(account);
        }
        return Result.success(result);
    }

    /**
     * 员工注册
     * @param employee: 数据对象
     * @return Result
     */
    @PostMapping("/register")
    public Result register(@RequestBody Employee employee) {
        employeeService.register(employee);
        return Result.success();
    }

    /**
     * 修改密码
     * @param account: 数据对象
     * @return Result
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        String role = account.getRole();
        if ("ADMIN".equals(role)) {
            adminService.updatePassword(account);
        }
        else if ("EMP".equals(role)) {
            employeeService.updatePassword(account);
        }
        return Result.success();
    }
}
