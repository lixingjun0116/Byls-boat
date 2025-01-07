package com.byls.boat.service.init;

import com.byls.boat.service.catchhandler.CacheCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Description 初始化资源
 * @Author lxj
 * @Date 2025/1/3 14:00
 * @Created by lxj
 */
@Slf4j
@Component
public class InitResouceCommand implements CommandLineRunner {

    @Autowired
    private CacheCenter cacheCenter;

    @Override
    public void run(String... args) throws Exception {
        try {
            // 初始化资源
            initStatic();

        } catch (Exception e) {
            log.error("项目初始化处理逻辑失败");
        }
    }

    //初始化相关资源
    public void initStatic() throws InterruptedException {
        boolean runFlag = true;
        while (runFlag) {
            try {
                cacheCenter.initCache();
                runFlag = false;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                Thread.sleep(1000);
            }
        }
    }
}
