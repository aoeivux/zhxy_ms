package com.aoeivux.service.impl;

import com.aoeivux.mapper.GradeMapper;
import com.aoeivux.pojo.Grade;
import com.aoeivux.service.GradeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("gradeServiceImpl")
@Transactional
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {
}
