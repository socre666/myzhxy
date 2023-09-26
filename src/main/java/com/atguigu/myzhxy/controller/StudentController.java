package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.Student;
import com.atguigu.myzhxy.service.StudentService;
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

@Api(tags = "班级控制器")
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {
    @Autowired
    private StudentService studentService;
    //DELETE /sms/studentController/delStudentById
    @ApiOperation("删除学生信息")
    @DeleteMapping("/delStudentById")
    public Result delStudentById(@ApiParam("要删除的所有的Student的id的JSON集合")@RequestBody List<Integer> ids){
        studentService.removeByIds(ids);
        return Result.ok();
    }
    //POST /sms/studentController/addOrUpdateStudent
    @ApiOperation("添加或者修改学生信息")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(
            @ApiParam("JSON格式的学生信息")@RequestBody Student student
    ){
        Integer id = student.getId();
        if(null == id || 0 == id){
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        //判断学号是否重复，假如重复了，需要重新输入不一样的学号才行
        String sno = student.getSno();
        if(studentService.issno(sno)>0){
            return Result.fail().message("学号已经存在了请重新输入其他学号");
        }
        studentService.saveOrUpdate(student);
        return Result.ok();
    }
    //GET /sms/studentController/getStudentByOpr/1/3
    @ApiOperation("分页带条件查询学生信息")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(
            @ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询页大小") @PathVariable("pageSize") Integer pageSize,
//            String gradeName,
//            String name
            @ApiParam("分页查询的查询条件") Student student
    ){
        //分页信息封装Page对象
        Page<Student> page = new Page<>(pageNo,pageSize);
        //进行分页查询
        IPage<Student> ipage = studentService.getStudentByOpr(page,student);
        //封装Result
        return Result.ok(ipage);
    }
}
