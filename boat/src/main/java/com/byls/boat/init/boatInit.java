package com.byls.boat.init;

import com.byls.boat.service.impl.BoatNavigationRecordsTiming;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Description 项目初始化操作
 * @Date 2024/11/5 10:54
 * @Created by lxj
 */
@Component
@Slf4j
public class boatInit implements CommandLineRunner {

    @Autowired
    BoatNavigationRecordsTiming navigationRecordsTiming;
    @Override
    public void run(String... args) {
        try {
            new Thread(navigationRecordsTiming).start();
        }catch (Exception e){
            log.error("项目初始化处理逻辑失败");
        }
    }
}
