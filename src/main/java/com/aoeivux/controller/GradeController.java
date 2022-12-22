package com.aoeivux.controller;

import com.aoeivux.pojo.Grade;
import com.aoeivux.service.GradeService;
import com.aoeivux.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "年级控制器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;


    @ApiOperation("获取所有年级信息")
    @GetMapping("/getGrades")
    public Result getGrades(){
        List<Grade> list = gradeService.list();
        return Result.ok(list);
    }

    // gradeController/deleteGrade
    @ApiOperation("删除Grade年级信息")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(
            @ApiParam("要删除的所有Grade的ID的JSON集合") @RequestBody List<Integer> ids
    ){
        gradeService.removeByIds(ids);
        return Result.ok();
    }



    // http://localhost:9001/sms/gradeController/saveOrUpdateGrade
    @ApiOperation("保存或修改Grade，有ID则修改，没有则增加")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(
            @ApiParam("这是JSON的Grade对象") @RequestBody Grade grade
    ){
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }

    @ApiOperation("根据年级名模糊查询、带分页")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGradesByOpr(
            @ApiParam("分页查询页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页模糊查询年级名的请求参数") @RequestParam(value = "gradeName", required = false) String gradeName
    ){
        // 设置分页信息
        Page<Grade> gradePage = new Page<>(pageNo, pageSize);
        // 调用服务层方法，传入信息的参数和查询年级名信息
        IPage<Grade> pageResult =  gradeService.getGradeByOpr(gradePage, gradeName);
        return Result.ok(pageResult);
    }
}
