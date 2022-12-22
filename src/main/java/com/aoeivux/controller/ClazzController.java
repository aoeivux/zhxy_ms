package com.aoeivux.controller;


import com.aoeivux.pojo.Clazz;
import com.aoeivux.service.ClazzService;
import com.aoeivux.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "班级控制器")
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {
    @Autowired
    private ClazzService clazzService;

    // http://localhost:9001/sms/clazzController/getClazzs
    @ApiOperation("查询所有班级信息")
    @GetMapping("/getClazzs")
    public Result getClazzs(
    ){
        List<Clazz> list = clazzService.list();
        return Result.ok(list);
    }


    // http://localhost:9001/sms/clazzController/saveOrUpdateClazz
    @ApiOperation("添加班级信息,有ID类型就为修改班级信息，没有则为添加班级信息")
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(
            @ApiParam("这是JSON数据中的Clazz对象") @RequestBody Clazz clazz
    ){
        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }

    // http://localhost:9001/sms/clazzController/deleteClazz
    @ApiOperation("删除班级信息，多选或者单选")
    @DeleteMapping("/deleteClazz")
    public Result deleteClazz(
            @ApiParam("删除班级信息的Id列表") @RequestBody List<Integer> ids
           /*
           * @RequestBody针对前端返回的JSON数据请求体数据进行解析成对象
           * @RequestParam针对请求头，URL中的参数进行解析
           * */
    ){
        clazzService.removeByIds(ids);
        return Result.ok();
    }

    // http://localhost:9001/sms/clazzController/getClazzsByOpr/1/3
    @ApiOperation("获取模糊查询班级名称的名称")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzsByOpr(
            @ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的大小") @PathVariable("pageSize") Integer pageSize,
            /*@ApiParam("年级名称") @RequestParam("gradeName") String gradeName,
              @ApiParam("班级名称") @RequestParam("name") String name*/
            @ApiParam("查询条件") Clazz clazz
    ) {
        Page<Clazz> page = new Page<>(pageNo, pageSize);
        IPage<Clazz> ipage = clazzService.getClazzsByOpr(page, clazz);
        return Result.ok(ipage);

    }

}
