package com.aoeivux.service;

import com.aoeivux.mapper.TeacherMapper;
import com.aoeivux.pojo.LoginForm;
import com.aoeivux.pojo.Teacher;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Repository;


public interface TeacherService extends IService<Teacher> {
    Teacher login(LoginForm loginForm);

    IPage<Teacher> getTeacherByOpr(Page<Teacher> page,Teacher teacher);
}
