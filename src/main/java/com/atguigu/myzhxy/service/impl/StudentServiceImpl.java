package com.atguigu.myzhxy.service.impl;

import com.atguigu.myzhxy.mapper.StudentMapper;
import com.atguigu.myzhxy.pojo.LoginForm;
import com.atguigu.myzhxy.pojo.Student;
import com.atguigu.myzhxy.service.StudentService;
import com.atguigu.myzhxy.util.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("studentServiceImpl")
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Override
    public Student login(LoginForm loginForm) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Student student = baseMapper.selectOne(queryWrapper);
        return student;
    }

    @Override
    public Student getStudentById(Long userId) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<Student>();
        queryWrapper.eq("id",userId);
        Student student = baseMapper.selectOne(queryWrapper);
        return student;
    }

    @Override
    public IPage<Student> getStudentByOpr(Page<Student> pageParam, Student student) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        String clazzname = student.getClazzName();
        if (!StringUtils.isEmpty(clazzname)) {
            queryWrapper.like("clazz_name",clazzname);
        }
        String name = student.getName();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name",name);
        }
        queryWrapper.orderByAsc("id");
        Page<Student> studentPage = baseMapper.selectPage(pageParam, queryWrapper);
        return studentPage;
    }

    @Override
    public int issno(String sno) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sno",sno);
        // 调用MyBatis Plus的BaseMapper的selectCount方法
        int count = baseMapper.selectCount(queryWrapper);

        // 判断是否存在重复记录
//        if (count > 0) {
//            System.out.println("学号已经存在，重复了");
//        } else {
//            System.out.println("学号不存在，可以使用");
//        }
        return count;
    }
}
