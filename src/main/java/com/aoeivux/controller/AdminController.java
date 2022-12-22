package com.aoeivux.controller;


import com.aoeivux.pojo.Admin;
import com.aoeivux.service.AdminService;
import com.aoeivux.util.JwtHelper;
import com.aoeivux.util.MD5;
import com.aoeivux.util.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "管理员控制器")
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    private AdminService adminService;


    @ApiOperation("不同类型用户修改密码")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @RequestHeader("token") String token,
            @PathVariable("odlPwd") String oldPwd,
            @PathVariable("newPwd") String newPwd
    ){
        // 判断session token是否过期
        boolean expiration = JwtHelper.isExpiration(token);
        if(expiration){
            return Result.fail().message("token失败，请重新登陆。");
        }

        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        switch (userType) {
            case 1://Admin
                QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
                break;
            case 2://Student
                break;
            case 3://Teacher
                break;

        }


        return Result.ok();
    }


    @ApiOperation("分页模糊查询管理员的信息")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(
            @ApiParam("分页数") @PathVariable Integer pageNo,
            @ApiParam("分页大小") @PathVariable Integer pageSize,
            @ApiParam("管理员名字") String adminName
    ){
        Page<Admin> page = new Page<>(pageNo, pageSize);
        IPage<Admin> ipage  = adminService.getAdminByOpr(page, adminName);
        return Result.ok(ipage);
    }
    // /sms/adminController/deleteAdmin
    @ApiOperation("删除管理员信息")
    @PostMapping("/deleteAdmin")
    public Result deleteAdmin(
            @ApiParam("JSON数据得到的要删除用户ID列表对象") @RequestBody List<Integer> ids
            ){
        adminService.removeByIds(ids);
        return Result.ok();
    }
    // /sms/adminController/saveOrUpdateAdmin
    @ApiOperation("增加或者修改管理员信息")
    @PostMapping("saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(
            @ApiParam("Json数据得到的Admin对象") @RequestBody Admin admin
    ){
        Integer id = admin.getId();
        if(null == id || 0 == id ){ // 添加新管理员JSON没有ID数据，修改则有ID数据
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }

}
