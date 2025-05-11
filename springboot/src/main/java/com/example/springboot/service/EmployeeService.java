package com.example.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Employee;
import com.example.springboot.exception.CustomException;
import com.example.springboot.mapper.EmployeeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Resource
    public EmployeeMapper employeeMapper;

    public void add(Employee employee) {
        String no = employee.getNo(); // 工号
        String username = employee.getUsername(); // 账号
        String password = employee.getPassword(); // 密码
        String name = employee.getName(); // 名称
        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if (dbEmployee != null) { // 注册的账号已存在
            throw new CustomException("500", "账号已存在");
        }
        Employee dbEmployeeNo = employeeMapper.selectByNo(no);
        if (dbEmployeeNo != null) { // 注册的工号已存在
            throw new CustomException("500", "工号已存在");
        }
        if (StrUtil.isBlank(password)) { // 密码没填
            employee.setPassword("123456"); // 默认密码
        }
        if (StrUtil.isBlank(name)) { // 名称没填
            employee.setName(username); // 默认名称
        }
        // 设置角色
        employee.setRole("EMP");
        employeeMapper.insert(employee);
    }

    public void update(Employee employee) {
        employeeMapper.updateById(employee);
    }

    public void deleteById(Integer id) {
        employeeMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public Employee selectById(Integer id) {
        return employeeMapper.selectById(id);
    }

    public List<Employee> selectAll(Employee employee) {
        return employeeMapper.selectAll(employee);
    }

    public List<Employee> selectList(Employee employee) {
        System.out.println(employee);
        return null;
    }

    public PageInfo<Employee> selectPage(Employee employee, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Employee> list = employeeMapper.selectAll(employee);
        return PageInfo.of(list);
    }

    public Employee login(Account account) {
        String username = account.getUsername(); // 账号
        String password = account.getPassword(); // 密码
        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if (dbEmployee == null) { // 没查询到任何用户
            throw new CustomException("500", "账号不存在");
        }
        if (!dbEmployee.getPassword().equals(password)) { // 密码校验
            throw new CustomException("500", "账号或密码错误");
        }
        return dbEmployee;
    }

    public void register(Employee employee) {
        this.add(employee);
    }

    public void updatePassword(Account account) {
        Integer id = account.getId();
        String password = account.getPassword();
        String newPassword = account.getNewPassword();
        Employee employee = this.selectById(id);
        if (!employee.getPassword().equals(password)) { // 检查原密码是否正确
            throw new CustomException("500", "对不起, 原密码错误");
        }
        employee.setPassword(newPassword); //设置新密码
        this.update(employee);
    }
}
