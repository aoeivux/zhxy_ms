package com.aoeivux.controller;

import com.aoeivux.pojo.Admin;
import com.aoeivux.pojo.LoginForm;
import com.aoeivux.pojo.Student;
import com.aoeivux.pojo.Teacher;
import com.aoeivux.service.AdminService;
import com.aoeivux.service.StudentService;
import com.aoeivux.service.TeacherService;
import com.aoeivux.util.CreateVerifiCodeImage;
import com.aoeivux.util.JwtHelper;
import com.aoeivux.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("sms/system")
public class SystemController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;


    @GetMapping("/getInfo")
    public Result getInfo(@RequestHeader("token") String token){
        boolean expiration = JwtHelper.isExpiration(token);

        return Result.ok();
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


}
