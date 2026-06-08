package com.eduadmin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.eduadmin.mapper.StudentMapper;
import com.eduadmin.pojo.PageResult;
import com.eduadmin.pojo.Student;
import com.eduadmin.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private StudentRedisCache studentRedisCache;

    @Override
    public void save(Student student) {
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.insert(student);
        studentRedisCache.clearCache();
    }

    @Override
    public PageResult page(String name, Integer degree, Integer clazzId, Integer page, Integer pageSize) {
        PageResult cached = studentRedisCache.getPageCache(name, degree, clazzId, page, pageSize);
        if (cached != null) {
            return cached;
        }

        PageHelper.startPage(page, pageSize);
        List<Student> studentList = studentMapper.list(name, degree, clazzId);
        Page<Student> p = (Page<Student>) studentList;
        PageResult pageResult = new PageResult(p.getTotal(), p.getResult());

        studentRedisCache.setPageCache(name, degree, clazzId, page, pageSize, pageResult);
        return pageResult;
    }

    @Override
    public Student getInfo(Integer id) {
        Student cached = studentRedisCache.getInfoCache(id);
        if (cached != null) {
            return cached;
        }

        Student student = studentMapper.getById(id);
        if (student != null) {
            studentRedisCache.setInfoCache(id, student);
        }
        return student;
    }

    @Override
    public void update(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
        studentRedisCache.clearCache();
    }

    @Override
    public void delete(List<Integer> ids) {
        studentMapper.delete(ids);
        studentRedisCache.clearCache();
    }
}
