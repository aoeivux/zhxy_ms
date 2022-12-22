package com.aoeivux.service.impl;

import com.aoeivux.mapper.StudentMapper;
import com.aoeivux.pojo.LoginForm;
import com.aoeivux.pojo.Student;
import com.aoeivux.service.StudentService;
import com.aoeivux.util.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    @Override
    public IPage<Student> getStudentByOpr(Page<Student> page, Student student) {
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        String name = student.getName();
        String clazzName = student.getClazzName();

        if (!StringUtils.isEmpty(name)) {
            studentQueryWrapper.like("name", name);
        }

        if (!StringUtils.isEmpty(clazzName)) {
            studentQueryWrapper.like("clazz_name", clazzName);
        }
        studentQueryWrapper.orderByAsc("id");
        return baseMapper.selectPage(page, studentQueryWrapper);

    }
}
