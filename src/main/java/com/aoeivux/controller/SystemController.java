package com.aoeivux.controller;

import com.aoeivux.pojo.Admin;
import com.aoeivux.pojo.LoginForm;
import com.aoeivux.pojo.Student;
import com.aoeivux.pojo.Teacher;
import com.aoeivux.service.AdminService;
import com.aoeivux.service.StudentService;
import com.aoeivux.service.TeacherService;
import com.aoeivux.util.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Api(tags = "系统控制器")
@RestController
@RequestMapping("sms/system")
public class SystemController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    // /sms/system/headerImgUpload
    @ApiOperation("文件上传接口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("头像文件") @RequestPart("multipartFile") MultipartFile multipartFile
            )
    {
        // 保存图片
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String originalFilename = multipartFile.getOriginalFilename();
        int lastIndexOf = originalFilename.lastIndexOf(".");
        String newFileName = uuid.concat(originalFilename.substring(lastIndexOf));

        // String realPath = httpServletRequest.getRealPath("public/upload/"); //内置tomcat无法指定准确位置,需要指定云服务器图片位置或者本地绝对位置
        //
        String realPath = "D:/Dev/java_projects/zhxy_ms/target/classes/public/upload/";
        String path = realPath.concat(newFileName);
        System.out.println(path);
        try {
            multipartFile.transferTo(new File(path));
        } catch (IOException | IllegalStateException e) {
            throw new RuntimeException(e);
        }

        String resPath="upload/".concat(newFileName);
        // 响应图片路径
        return Result.ok(resPath);
    }


    @GetMapping("/getInfo")
    public Result getInfo(@RequestHeader("token") String token){

        // 判断session中的token是否过期
        boolean expiration = JwtHelper.isExpiration(token);
        if(expiration){
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }

        // 从token中解析出用户id和用户类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);
        // map存储返回的Result对象
        Map<String, Object> map = new LinkedHashMap<>();
        switch(userType){
            case 1:
                Admin admin = adminService.getById(userId);
                map.put("userType", userType);
                map.put("user", admin);
                break;
            case 2:
                Student student = studentService.getById(userId);
                map.put("userType", userType);
                map.put("user", student);
                break;
            case 3:
                Teacher teacher = teacherService.getById(userId);
                map.put("userType", userType);
                map.put("user", teacher);
                break;
        }

        return Result.ok(map);
    }


    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm, HttpServletRequest request){
        // 验证码校验
        String sessionCode = (String)request.getSession().getAttribute("verifiCode");

        // System.out.println("sessionCode==>" + sessionCode);  得到的是验证码明文
        String loginCode = loginForm.getVerifiCode();
        if(( "".equals(sessionCode) || null == sessionCode )){
            return Result.fail().message("验证码失效，请刷新后重试");
        }
        if( !(sessionCode.equalsIgnoreCase(loginCode) )){
            return Result.fail().message("验证码有误，请刷新后重试");
        }
        // 登陆成功后，将成功的session验证码删除
        request.removeAttribute("verifiCode");
        // 不同用户类型，校验
        Integer userType = loginForm.getUserType();
        // map用于存储用户对象信息
        Map<String, Object>  tokenMap = new LinkedHashMap<>();
        // 用户类型+用户ID+有效时间 = tokenMap  也就是下面生成的token
        switch (userType) {
            case 1:
                try {
                    Admin admin = adminService.login(loginForm);
                    if(null != admin){
                        String token = JwtHelper.createToken(admin.getId().longValue(), 1);
                        tokenMap.put("token", token);
                    }else {
                        throw new RuntimeException("用户名或密码有误");
                    }
                    return Result.ok(tokenMap);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }

            case 2:
                try {
                    Student student = studentService.login(loginForm);
                    if(null != student){
                        String token = JwtHelper.createToken(student.getId().longValue(), 2);
                        tokenMap.put("token", token);
                    }else {
                        throw new RuntimeException("用户名或密码有误");
                    }
                    return Result.ok(tokenMap);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }

            case 3:
                try {
                    Teacher teacher = teacherService.login(loginForm);
                    if(null != teacher){
                        String token = JwtHelper.createToken(teacher.getId().longValue(), 3);
                        tokenMap.put("token", token);
                    }else {
                        throw new RuntimeException("用户名或密码有误");
                    }
                    return Result.ok(tokenMap);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }

        return Result.fail().message("查无此用户");
    }


    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response){
        // 获取图片
        BufferedImage image = CreateVerifiCodeImage.getVerifiCodeImage();
        // 获取图片上面的验证码
        String code = new String(CreateVerifiCodeImage.getVerifiCode());
        // 将验证码放入Session域，为下一次后端登陆验证做准备
        request.getSession().setAttribute("verifiCode", code);
        // 将验证码图片响应给浏览器
        try {
            ImageIO.write(image, "JPG", response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @ApiOperation("修改密码")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(@RequestHeader("token") String token,
                            @PathVariable("oldPwd") String oldPwd,
                            @PathVariable("newPwd") String newPwd){
        boolean yOn = JwtHelper.isExpiration(token);
        if(yOn){
            //token过期
            return Result.fail().message("token失效!");
        }
        //通过token获取当前登录的用户id
        Long userId = JwtHelper.getUserId(token);
        //通过token获取当前登录的用户类型
        Integer userType = JwtHelper.getUserType(token);
        // 将明文密码转换为暗文
        oldPwd= MD5.encrypt(oldPwd);
        newPwd= MD5.encrypt(newPwd);
        if(userType == 1){
            QueryWrapper<Admin> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("id",userId.intValue()).eq("password",oldPwd);
            Admin admin = adminService.getOne(queryWrapper);
            if (null!=admin) {
                admin.setPassword(newPwd);
                adminService.saveOrUpdate(admin);
            }else{
                return Result.fail().message("原密码输入有误！");
            }
        }else if(userType == 2){
            QueryWrapper<Student> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("id",userId.intValue()).eq("password",oldPwd);
            Student student = studentService.getOne(queryWrapper);
            if (null!=student) {
                student.setPassword(newPwd);
                studentService.saveOrUpdate(student);
            }else{
                return Result.fail().message("原密码输入有误！");
            }
        }
        else if(userType == 3){
            QueryWrapper<Teacher> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("id",userId.intValue()).eq("password",oldPwd);
            Teacher teacher = teacherService.getOne(queryWrapper);
            if (null!=teacher) {
                teacher.setPassword(newPwd);
                teacherService.saveOrUpdate(teacher);
            }else{
                return Result.fail().message("原密码输入有误！");
            }
        }
        return Result.ok();
    }




}
