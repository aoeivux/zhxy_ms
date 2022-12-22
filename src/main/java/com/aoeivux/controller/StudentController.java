package com.aoeivux.controller;

import com.aoeivux.pojo.Student;
import com.aoeivux.service.StudentService;
import com.aoeivux.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "学生控制器")
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {


    @Autowired
    private StudentService studentService;


    // /sms/studentController/getStudentByOpr/1/3
    @ApiOperation("分页模糊查询学生信息")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result studentController(
        @ApiParam("分页页码数") @PathVariable("pageNo") Integer pageNo,
        @ApiParam("分页总大小") @PathVariable("pageSize") Integer pageSize,
        Student student
    ){
        Page<Student> page = new Page<>(pageNo, pageSize);
        IPage<Student> ipage = studentService.getStudentByOpr(page, student);

        return Result.ok(ipage);
    }
    // /sms/studentController/delStudentById
    @ApiOperation("删除用户信息")
    @DeleteMapping("/delStudentById")
    public Result deleteStudent(
            @ApiParam("JSON中获取的到的id类型数据对象") @RequestBody List<Integer> ids
    ){
        studentService.removeByIds(ids);
        return Result.ok();
    }

    // /sms/studentController/addOrUpdateStudent
    @ApiOperation("添加或者修改学生信息")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(
            @ApiParam("JSON数据中获取得到的Student学生对象") @RequestBody Student student
    ){
        studentService.saveOrUpdate(student);
        return Result.ok();
    }


}
