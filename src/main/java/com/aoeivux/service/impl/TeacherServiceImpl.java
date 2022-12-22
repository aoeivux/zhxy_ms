package com.aoeivux.service.impl;


import com.aoeivux.mapper.TeacherMapper;
import com.aoeivux.pojo.LoginForm;
import com.aoeivux.pojo.Teacher;
import com.aoeivux.service.TeacherService;
import com.aoeivux.util.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("teacherServiceImpl")
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Override
    public Teacher login(LoginForm loginForm) {
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.eq("name", loginForm.getUsername());
        teacherQueryWrapper.eq("password", MD5.encrypt( loginForm.getPassword() ));

        Teacher teacher = baseMapper.selectOne(teacherQueryWrapper);

        return teacher;

    }

    @Override
    public IPage<Teacher> getTeacherByOpr(Page<Teacher> page, Teacher teacher) {
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        String name = teacher.getName();
        String clazzName = teacher.getClazzName();
        if (!StringUtils.isEmpty(name)) {
            teacherQueryWrapper.like("name",name);
        }

        if(!StringUtils.isEmpty(clazzName)){
            teacherQueryWrapper.like("clazz_name", clazzName);
        }

        teacherQueryWrapper.orderByAsc("id");

        return baseMapper.selectPage(page, teacherQueryWrapper);

    }
}
