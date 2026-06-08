package com.eduadmin.controller;

import com.eduadmin.pojo.Notice;
import com.eduadmin.pojo.PageResult;
import com.eduadmin.pojo.Result;
import com.eduadmin.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/notices")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @PostMapping
    public Result save(@RequestBody Notice notice) {
        log.info("新增公告: {}", notice);
        noticeService.save(notice);
        return Result.success();
    }

    @GetMapping
    public Result page(String title,
                       Integer status,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("分页查询公告: title={}, status={}", title, status);
        PageResult<Notice> pageResult = noticeService.page(title, status, page, pageSize);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("根据ID查询公告: {}", id);
        Notice notice = noticeService.getById(id);
        return Result.success(notice);
    }

    @PutMapping
    public Result update(@RequestBody Notice notice) {
        log.info("修改公告: {}", notice);
        noticeService.update(notice);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("删除公告: {}", id);
        noticeService.deleteById(id);
        return Result.success();
    }
}
