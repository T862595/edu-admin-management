package com.eduadmin.mapper;

import com.eduadmin.pojo.Notice;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NoticeMapper {

    @Insert("insert into notice(title, content, status, create_time, update_time) " +
            "values(#{title}, #{content}, #{status}, #{createTime}, #{updateTime})")
    void insert(Notice notice);

    List<Notice> list(String title, Integer status);

    @Select("select * from notice where id = #{id}")
    Notice getById(Integer id);

    void update(Notice notice);

    @Delete("delete from notice where id = #{id}")
    void deleteById(Integer id);
}
