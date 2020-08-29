package com.ls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 热更新、热加载
 * 1、Settings->Compiler->Build project automatically
 * 2、Ctrl+Shift+A ->搜索registry，找到Registry...，注意是后面有三个点的那个，然后找到compiler.automake.allow.when.app.running，勾选
 * 3、热部署快捷键Ctrl+F9,java代码修改后需要执行一下快捷键才能实际生效
 *
 */
@SpringBootApplication
@EnableScheduling   //开启定时任务
public class VLoushiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VLoushiApplication.class, args);
    }

}
