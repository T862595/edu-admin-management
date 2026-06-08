package com.eduadmin.service;

import com.eduadmin.pojo.Notice;
import com.eduadmin.pojo.PageResult;

public interface NoticeService {

    void save(Notice notice);

    PageResult<Notice> page(String title, Integer status, Integer page, Integer pageSize);

    Notice getById(Integer id);

    void update(Notice notice);

    void deleteById(Integer id);
}
