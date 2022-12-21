package com.aoeivux.service.impl;

import com.aoeivux.mapper.ClassesMapper;
import com.aoeivux.pojo.Classes;
import com.aoeivux.service.ClassesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("classesServiceImpl")
@Transactional
public class ClassesServiceImpl extends ServiceImpl<ClassesMapper, Classes> implements ClassesService {
}
