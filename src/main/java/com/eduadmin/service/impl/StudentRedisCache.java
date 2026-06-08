package com.eduadmin.service.impl;

import com.eduadmin.pojo.PageResult;
import com.eduadmin.pojo.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 学员模块 Redis 缓存
 */
@Slf4j
@Component
public class StudentRedisCache {

    private static final String PAGE_KEY_PREFIX = "student:page:";
    private static final String INFO_KEY_PREFIX = "student:info:";
    private static final long CACHE_TTL_MINUTES = 30;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public PageResult getPageCache(String name, Integer degree, Integer clazzId, Integer page, Integer pageSize) {
        String key = buildPageKey(name, degree, clazzId, page, pageSize);
        Object value = redisTemplate.opsForValue().get(key);
        if (value instanceof PageResult pageResult) {
            log.info("学员分页查询命中Redis缓存, key={}", key);
            return pageResult;
        }
        return null;
    }

    public void setPageCache(String name, Integer degree, Integer clazzId, Integer page, Integer pageSize, PageResult pageResult) {
        String key = buildPageKey(name, degree, clazzId, page, pageSize);
        redisTemplate.opsForValue().set(key, pageResult, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        log.info("学员分页查询写入Redis缓存, key={}", key);
    }

    public Student getInfoCache(Integer id) {
        String key = INFO_KEY_PREFIX + id;
        Object value = redisTemplate.opsForValue().get(key);
        if (value instanceof Student student) {
            log.info("学员详情命中Redis缓存, key={}", key);
            return student;
        }
        return null;
    }

    public void setInfoCache(Integer id, Student student) {
        String key = INFO_KEY_PREFIX + id;
        redisTemplate.opsForValue().set(key, student, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        log.info("学员详情写入Redis缓存, key={}", key);
    }

    /**
     * 学员数据变更后清除缓存，保证列表与详情数据一致
     */
    public void clearCache() {
        clearByPattern(PAGE_KEY_PREFIX + "*");
        clearByPattern(INFO_KEY_PREFIX + "*");
        log.info("学员模块Redis缓存已清除");
    }

    private void clearByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    private String buildPageKey(String name, Integer degree, Integer clazzId, Integer page, Integer pageSize) {
        return PAGE_KEY_PREFIX
                + nullToEmpty(name) + ":"
                + nullToEmpty(degree) + ":"
                + nullToEmpty(clazzId) + ":"
                + page + ":"
                + pageSize;
    }

    private String nullToEmpty(Object value) {
        return value == null ? "" : value.toString();
    }
}
