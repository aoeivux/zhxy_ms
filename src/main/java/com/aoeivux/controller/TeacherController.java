package com.aoeivux.controller;

import com.aoeivux.pojo.Teacher;
import com.aoeivux.service.TeacherService;
import com.aoeivux.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "教师控制器")
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @ApiOperation("删除教师信息")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(
            @ApiParam("JSON数据中的ID类型用来删除用户") @RequestBody List<Integer> ids
    ){
        teacherService.removeByIds(ids);
        return Result.ok();
    }


    // /sms/teacherController/getTeachers/1/3?name=xxx&clazzName=xxx
    @ApiOperation("获取教师信息")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(
            @ApiParam("分页查询页数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询总大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("JSON中的Teacher对象") Teacher teacher // 注意RequestBody不接受Get请求
            ){
        Page<Teacher> page = new Page<>(pageNo, pageSize);
        IPage<Teacher> ipage = teacherService.getTeacherByOpr(page,teacher);
        return Result.ok(ipage);
    }

    // /sms/teacherController/saveOrUpdateTeacher
    @ApiOperation("添加或者更新教师信息,若有ID类型则为修改，否则为添加信息。")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(
            @ApiParam("JSON中的teacher对象") @RequestBody Teacher teacher
    ){
        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }
}
