package com.aoeivux.service.impl;

import com.aoeivux.mapper.StudentMapper;
import com.aoeivux.pojo.LoginForm;
import com.aoeivux.pojo.Student;
import com.aoeivux.service.StudentService;
import com.aoeivux.util.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("studentServiceImpl")
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Override
    public Student login(LoginForm loginForm) {
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("name", loginForm.getUsername());
        studentQueryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));

        Student student = baseMapper.selectOne(studentQueryWrapper);
        return student;
    }
}
