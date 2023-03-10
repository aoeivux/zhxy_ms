package com.aoeivux.service;

import com.aoeivux.pojo.Admin;
import com.aoeivux.pojo.LoginForm;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AdminService extends IService<Admin> {
    Admin login(LoginForm loginForm);

    IPage<Admin> getAdminByOpr(Page<Admin> page, String adminName);
}
