package com.example.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.exception.CustomException;
import com.example.springboot.mapper.AdminMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Resource
    public AdminMapper adminMapper;

    public void add(Admin admin) {
        String username = admin.getUsername(); // 账号
        String password = admin.getPassword(); // 密码
        String name = admin.getName(); // 名称
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if (dbAdmin != null) { // 注册的账号已存在
            throw new CustomException("500", "账号已存在");
        }
        if (StrUtil.isBlank(password)) { // 密码没填
            admin.setPassword("admin"); // 默认密码
        }
        if (StrUtil.isBlank(name)) { // 名称没填
            admin.setName(username); // 默认名称
        }
        // 设置角色
        admin.setRole("ADMIN");
        adminMapper.insert(admin);
    }

    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }

    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }

    public List<Admin> selectAll(Admin admin) {
        // 额外的业务操作
        return adminMapper.selectAll(admin);
    }

    public List<Admin> selectList(Admin admin) {
        System.out.println(admin);
        return null;
    }

    public PageInfo<Admin> selectPage(Admin admin, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }

    public Admin login(Account account) {
        String username = account.getUsername(); // 账号
        String password = account.getPassword(); // 密码
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if (dbAdmin == null) { // 没查询到任何用户
            throw new CustomException("500", "账号不存在");
        }
        if (!dbAdmin.getPassword().equals(password)) { // 密码校验
            throw new CustomException("500", "账号或密码错误");
        }
        return dbAdmin;
    }

    public void updatePassword(Account account) {
        Integer id = account.getId();
        String password = account.getPassword();
        String newPassword = account.getNewPassword();
        Admin admin = this.selectById(id);
        if (!admin.getPassword().equals(password)) { // 检查原密码是否正确
            throw new CustomException("500", "对不起, 原密码错误");
        }
        admin.setPassword(newPassword); //设置新密码
        this.update(admin);
    }
}
