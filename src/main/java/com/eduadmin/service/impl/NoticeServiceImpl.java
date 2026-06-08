package com.eduadmin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.eduadmin.mapper.NoticeMapper;
import com.eduadmin.pojo.Notice;
import com.eduadmin.pojo.PageResult;
import com.eduadmin.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public void save(Notice notice) {
        notice.setCreateTime(LocalDateTime.now());
        notice.setUpdateTime(LocalDateTime.now());
        if (notice.getStatus() == null) {
            notice.setStatus(1);
        }
        noticeMapper.insert(notice);
    }

    @Override
    public PageResult<Notice> page(String title, Integer status, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<Notice> list = noticeMapper.list(title, status);
        Page<Notice> p = (Page<Notice>) list;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    @Override
    public Notice getById(Integer id) {
        return noticeMapper.getById(id);
    }

    @Override
    public void update(Notice notice) {
        notice.setUpdateTime(LocalDateTime.now());
        noticeMapper.update(notice);
    }

    @Override
    public void deleteById(Integer id) {
        noticeMapper.deleteById(id);
    }
}
