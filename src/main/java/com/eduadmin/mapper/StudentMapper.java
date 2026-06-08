package com.eduadmin.mapper;

import com.eduadmin.pojo.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {

    /**
     * 添加学生
     */
    @Insert("insert into student(name, no, gender, phone,id_card, is_college, address, degree, graduation_date,clazz_id, create_time, update_time) VALUES " +
            "(#{name},#{no},#{gender},#{phone},#{idCard},#{isCollege},#{address},#{degree},#{graduationDate},#{clazzId},#{createTime},#{updateTime})")
    void insert(Student student);

    /**
     * 动态条件查询
     */
    List<Student> list(String name, Integer degree, Integer clazzId);

    /**
     * 根据ID查询学生信息
     */
    @Select("select * from student where id = #{id}")
    Student getById(Integer id);

    /**
     * 修改学生信息
     */
    void update(Student student);

    /**
     * 批量删除学生信息
     */
    void delete(List<Integer> ids);

    @Select("select count(*) from student where clazz_id = #{id}")
    Integer countByClazzId(Integer id);

    /**
     * 批量插入
     */
    void insertBatch(List<Student> studentList);
}
