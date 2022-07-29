package com.xiaoliu.serurity.config;


import com.xiaoliu.serurity.filter.TokenAuthenticationFilter;
import com.xiaoliu.serurity.filter.TokenLoginFilter;
import com.xiaoliu.serurity.security.DefaultPasswordEncoder;
import com.xiaoliu.serurity.security.TokenLogoutHandler;
import com.xiaoliu.serurity.security.TokenManager;
import com.xiaoliu.serurity.security.UnauthorizedEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <p>
 * Security配置类核心配置类
 * </p>
 *
 * @author qy
 * @since 2019-11-18
 */
//核心配置类
@Configuration
@EnableWebSecurity
//开启基于方法的安全认证机制，也就是说在web层的controller启用注解机制的安全确认，
//只有加了@EnableGlobalMethodSecurity(prePostEnabled=true) 那么在上面使用的 就是开启注解方式的认证授权
//@PreAuthorize(“hasAuthority(‘admin’)”)才会生效
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TokenWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;  //这个框架自己写的自定义查询数据库的类，在service-acl模块使用，impl中
    //下面前两个是自己定义的
    private TokenManager tokenManager;
    private DefaultPasswordEncoder defaultPasswordEncoder;
    //redis的操作类
    private RedisTemplate redisTemplate;

    @Autowired
    public TokenWebSecurityConfig(UserDetailsService userDetailsService, DefaultPasswordEncoder defaultPasswordEncoder,
                                  TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.userDetailsService = userDetailsService;
        this.defaultPasswordEncoder = defaultPasswordEncoder;
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 核心配置设置
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                .authenticationEntryPoint(new UnauthorizedEntryPoint())
                .and().csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                //设置退出地址，随便自己写的，其他不变，在indexcontroller中
                .and().logout().logoutUrl("/admin/acl/index/logout")
                .addLogoutHandler(new TokenLogoutHandler(tokenManager,redisTemplate)).and()
                .addFilter(new TokenLoginFilter(authenticationManager(), tokenManager, redisTemplate))
                .addFilter(new TokenAuthenticationFilter(authenticationManager(), tokenManager, redisTemplate)).httpBasic();
    }

    /**
     * 密码处理
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(defaultPasswordEncoder);
    }

    /**
     * 配置哪些请求不拦截
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/**",
                "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**"
               );
        //web.ignoring().antMatchers("/*/**"
        //);
    }
}
