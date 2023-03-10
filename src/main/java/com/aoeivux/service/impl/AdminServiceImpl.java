package com.aoeivux.service.impl;

import com.aoeivux.mapper.AdminMapper;
import com.aoeivux.pojo.Admin;
import com.aoeivux.pojo.LoginForm;
import com.aoeivux.service.AdminService;
import com.aoeivux.util.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.management.Query;


@Service("adminServiceImpl")
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService{
    @Override
    public Admin login(LoginForm loginForm) {
        // 返回Admin的QueryWrapper对象
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Admin admin = baseMapper.selectOne(queryWrapper);

        return admin;
    }

    @Override
    public IPage<Admin> getAdminByOpr(Page<Admin> page, String adminName) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(adminName)) {
            queryWrapper.like("name", adminName);
        }
        queryWrapper.orderByAsc("id");
        Page<Admin> adminPage = baseMapper.selectPage(page, queryWrapper);
        return adminPage;

    }
}