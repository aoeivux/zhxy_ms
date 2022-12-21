package com.aoeivux.service;

import com.aoeivux.pojo.LoginForm;
import com.aoeivux.pojo.Student;
import com.baomidou.mybatisplus.extension.service.IService;

public interface StudentService extends IService<Student> {
    Student login(LoginForm loginForm);
}
