package com.aoeivux.service.impl;

import com.aoeivux.mapper.ClazzMapper;
import com.aoeivux.pojo.Clazz;
import com.aoeivux.service.ClazzService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.xml.internal.bind.v2.TODO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("clazzServiceImpl")
@Transactional

public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService{

    @Override
    public IPage<Clazz> getClazzsByOpr(Page<Clazz> classPage, Clazz clazz) {
        QueryWrapper<Clazz> clazzQueryWrapper = new QueryWrapper<>();
        String gradeName = clazz.getGradeName();
        String name = clazz.getName();
        if (!StringUtils.isEmpty(name)) {
            clazzQueryWrapper.like("name", name);
        }
        // TODO 不知道是否正确 SOLVES
        if (!StringUtils.isEmpty(gradeName)) {
            clazzQueryWrapper.like("grade_name", gradeName);
        }

        clazzQueryWrapper.orderByAsc("id");
        Page<Clazz> clazzPage = baseMapper.selectPage(classPage, clazzQueryWrapper);
        return clazzPage;
    }
}