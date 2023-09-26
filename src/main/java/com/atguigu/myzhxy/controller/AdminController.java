package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.Admin;
import com.atguigu.myzhxy.service.AdminService;
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

@Api(tags = "管理员控制器")
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {
    @Autowired
    private AdminService adminService;
    //DELETE /sms/adminController/deleteAdmin
    @ApiOperation("删除管理员信息")
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(@ApiParam("要删除的所有的Admin的id的JSON集合")@RequestBody List<Integer> ids){
        adminService.removeByIds(ids);
        return Result.ok();
    }
    //POST /sms/adminController/saveOrUpdateAdmin
    @ApiOperation("增加或者修改管理员的信息")
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(@ApiParam("JSON格式的管理员信息")@RequestBody Admin admin){
        Integer id = admin.getId();
        if(null == id || 0 == id){
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }
    //GET /sms/adminController/getAllAdmin/1/3
    @ApiOperation("分页带条件查询管理员信息")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(
            @ApiParam("分页查询的页码数") @PathVariable Integer pageNo,
            @ApiParam("分页查询页大小")@PathVariable Integer pageSize,
            @ApiParam("分页查询的查询条件") Admin admin
    ){
        Page<Admin> page = new Page<>(pageNo,pageSize);
        IPage<Admin> ipage = adminService.getAllAdmin(page,admin);
        return Result.ok(ipage);
    }
}
