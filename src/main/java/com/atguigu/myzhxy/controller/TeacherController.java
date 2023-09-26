package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.Teacher;
import com.atguigu.myzhxy.service.TeacherService;
import com.atguigu.myzhxy.util.MD5;
import com.atguigu.myzhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "教师控制器")
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    //DELETE /sms/teacherController/deleteTeacher
    @ApiOperation("删除教师信息")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(@ApiParam("要删除的所有的Student的id的JSON集合")@RequestBody List<Integer> ids){
        teacherService.removeByIds(ids);
        return Result.ok();
    }
    //POST /sms/teacherController/saveOrUpdateTeacher
    @ApiOperation("保存或者修改教师信息")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(@ApiParam("JSON格式的教师信息")@RequestBody Teacher teacher){
        //如果是新增要对密码进行加密
        Integer id = teacher.getId();
        if (id ==null || id == 0) {
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }

        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }
    //GET /sms/teacherController/getTeachers/1/3
    @ApiOperation("分页带条件查询教师信息")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(
            @ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页查询的查询条件") Teacher teacher
    ){
        Page<Teacher> page = new Page<>(pageNo,pageSize);
        IPage<Teacher> iPage=  teacherService.getTeachers(page,teacher);
        return Result.ok(iPage);
    }
}
