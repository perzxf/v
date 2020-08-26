package com.ls.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    /**
     * 注入ShiroRealm  身份认证
     * 不能省略，可能导致service无法注入
     */
    @Bean
    public ShiroRealm shiroRealm() {
        return new ShiroRealm();
    }

    /**
     * 注入securityManager
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
        return securityManager;
    }

    /**
     * Filter工厂，设置过滤条件与跳转条件
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();

        // Shiro的核心安全接口
        bean.setSecurityManager(securityManager);

        // 设置登陆页，如果不设置，默认会自动寻找Web工程根目录下的"login.jsp"页面
        bean.setLoginUrl("/login");

        // 自定义拦截规则
        Map<String,String> map = new LinkedHashMap<>();
        /**
         * 过滤链的定义，从上往下自行，一般将 /** 放在最下面
         * authc -->所有的url必须认证才可以访问
         * anon  -->所有的url都可以匿名访问
         */
        //配置不会被拦截的链接  顺序判断
//        map.put("/spider/**","anon");
        map.put("/","anon");  //根目录
        map.put("/assets/**","anon");   //资源文件


        // 设置退出登陆，配置退出过滤器，其中的具体的退出代码Shiro已经帮我们实现了
        map.put("/exitLogin", "logout");
        // 对所有用户认证
        map.put("/**", "authc");

        bean.setFilterChainDefinitionMap(map);
        return bean;
    }

    /**
     * Shiro生命周期处理器
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 注册AuthorizationAttributeSourceAdvisor
     * 如果要开启注解，必须添加
     * 开启Shiro注解，（如@RequiresRoles,@RequiresPermissions）,需借助SpringAOP扫描使用shiro注解的类，并在必要时进行安全逻辑验证
     * 配置以下两个Bean即可实现这个功能
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        return advisor;
    }
}
