https://blog.csdn.net/wangyanqun2017/article/details/85466492

# Shiro与SSM整合

1. 除了ssm的依赖还需要额外的依赖

   ```xml
   <!--shiro需要的依赖-->
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-context</artifactId>
       <version>4.2.4.RELEASE</version>
   </dependency>
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-webmvc</artifactId>
       <version>4.2.4.RELEASE</version>
   </dependency>
   <dependency>
       <groupId>org.apache.shiro</groupId>
       <artifactId>shiro-core</artifactId>
       <version>1.4.0</version>
   </dependency>
   <dependency>
       <groupId>org.apache.shiro</groupId>
       <artifactId>shiro-web</artifactId>
       <version>1.4.0</version>
   </dependency>
   <dependency>
       <groupId>org.apache.shiro</groupId>
       <artifactId>shiro-spring</artifactId>
       <version>1.4.0</version>
   </dependency>
   ```

2. web.xml中指定配置文件目录以及指定shiroFilter的拦截规则

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
            version="3.1">
       <!-- 指定Spring Bean的配置文件所在目录。默认配置在WEB-INF目录下 -->
       <context-param>
           <param-name>contextConfigLocation</param-name>
           <param-value>classpath*:spring/applicationContext.xml</param-value>
       </context-param>
       <!-- shiro过虑器，DelegatingFilterProx会从spring容器中找shiroFilter -->
       <filter>
           <filter-name>shiroFilter</filter-name>
           <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
       </filter>
       <filter-mapping>
           <filter-name>shiroFilter</filter-name>
           <url-pattern>/*</url-pattern>
       </filter-mapping>
          <!-- SSM整合需要的组件这里没有列出 -->
   </web-app>
   ```

3. SSM整合的时候会有4个配置文件applicationContext.xml,spring-context.xml,spring-mybatis.xml,spring-servlet.xml在spring-context.xml中需要将shiroFilter与SecurityManager与自定义realm注入到spring容器中

   ```xml
   <!-- 1. 配置自定义的realm -->
       <bean class="com.sz.shiro.realm.CustomRealm" id="realm">
       </bean>
   <!-- 2. 配置安全管理器SecurityManager -->
   	<bean id="securityManager" class="org.apache.shiro.web.mgt.DefaultWebSecurityManager">
   		<property name="realm" ref="realm"/>
   	</bean>
   <!-- 3. 定义ShiroFilter, id必须跟web.xml中配置代理filter的filtername的值一致 -->
   	<bean id="shiroFilter" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean">
   		<property name="securityManager" ref="securityManager"/>
           <property name="loginUrl" value="login.html"/><!-- 登录认证失败跳转的路径 -->
           <property name="unauthorizedUrl" value="403.html"/><!-- 没有权限时跳转的路径 -->
           <property name="filterChainDefinitions">
               <value>
                   <!--authc需要认证才可以访问,anon无需认证-->
                   /login.html=anon
                   /user/subLogin=anon
                   /user/add=roles["admin"]<!--如果用户不是对应admin角色,那么就跳到403.html-->
                   /*=authc<!--authc需要登录认证了之后才能访问,否则跳到login.html-->
                   <!--/*=authc一定要写后面,因为url地址是从上到下匹配的,如果写在前面就会出现所有请求都需要认证-->
                   <!--静态资源需要放行 /static/**=anon-->
               </value>
               </property>
   	</bean>
   ```

   ![1558769155908](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1558769155908.png)

   ![1558769195429](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1558769195429.png)

   自定义filter

   自定义，分认证与授权，如果是认证则继承认证的类，如果是授权则继承授权的类。然后重写方法，其中参数Object o 即代表存在哪些参数，即perms[a,b,c]中的abc然后判断返回结果true 或者false即可

   1. 写一个RolesOrFilter

      ```java
      package com.sz.filter;
      import org.apache.shiro.subject.Subject;
      import org.apache.shiro.web.filter.authz.AuthorizationFilter;
      import javax.servlet.ServletRequest;
      import javax.servlet.ServletResponse;
      //与授权相关
      public class RolesOrFilter  extends AuthorizationFilter {
          @Override
          protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
              //用户传入的角色数组,只要用户拥有其中一个角色就可以通过
              Subject subject=getSubject(servletRequest,servletResponse);
              String[] roles=(String[])o;
              if(roles==null||roles.length==0){
                  return  true;
              }
              for (String role:roles){
                  if(subject.hasRole(role)){
                      return true;//通过授权
                  }
              }
              return false;//未通过授权
          }
      }
      ```

   2. 在后台xml文件中引入该filter的bean，然后在主过滤器中引入filter的标签，写入entry key 与 value-ref，然后在filter过滤中

      ```xml
      <bean id="shiroFilter" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean">
              <property name="securityManager" ref="securityManager"/>
              <property name="loginUrl" value="login.html"/>
              <property name="unauthorizedUrl" value="403.html"/>
              <property name="filterChainDefinitions">
                  <value>
                      /login.html=anon
                      /user/subLogin=anon
                      /user/subTest3=rolesOr["user","admin"]
                      /*=authc
                  </value>
              </property>
              <property name="filters">
                  <util:map>
                      <entry key="rolesOr" value-ref="rolesOrFilter" />
                  </util:map>
              </property>
          </bean>
          <!--自定义的shiro过滤器-->
          <bean class="com.sz.filter.RolesOrFilter" id="rolesOrFilter" />
      ```

4. 自定义realm

   ```java
   package com.sz.shiro.realm;
   import com.sz.pojo.User;
   import com.sz.service.UserService;
   import org.apache.shiro.authc.AuthenticationException;
   import org.apache.shiro.authc.AuthenticationInfo;
   import org.apache.shiro.authc.AuthenticationToken;
   import org.apache.shiro.authc.SimpleAuthenticationInfo;
   import org.apache.shiro.authz.AuthorizationInfo;
   import org.apache.shiro.authz.SimpleAuthorizationInfo;
   import org.apache.shiro.realm.AuthorizingRealm;
   import org.apache.shiro.subject.PrincipalCollection;
   import org.springframework.beans.factory.annotation.Autowired;
   import java.util.HashSet;
   import java.util.List;
   import java.util.Set;
   public class CustomRealm extends AuthorizingRealm {
       @Autowired
       private UserService userService;
   //授权
       @Override
       protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
   String username=(String) principalCollection.getPrimaryPrincipal();
   //从数据库或者缓存中获取角色数据
           List<String> roles=userService.giveRoles(username);
           List<String> permissions= null;
           for (String role:roles) {
               permissions.addAll(userService.givePermission(role));
           }
           SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
          simpleAuthorizationInfo.addStringPermissions(permissions);
           simpleAuthorizationInfo.addRoles(roles);
           return simpleAuthorizationInfo;
       }
       //认证方法,AuthenticationToken是传过来的认证信息
       @Override
       protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
   //从主体传过来的认证信息中,获得用户名
           String username=(String) authenticationToken.getPrincipal();
   //通过用户名到数据库中获取凭证,通过用户名获取密码
           User user = userService.login(username);
           if (user==null) {
               return null;
           }
           return new SimpleAuthenticationInfo(username,user.getPassword(),"customRealm");
       }
   }
   
   ```

5. 登录方法

   ```java
   @RequestMapping(value="/subLogin",produces="application/json;charset=utf-8")
       public String subLogin(User user){
           //认证步骤
           //1.创建SecurityManager,配置文件中进行了配置
           //2.主体提交认证请求,subject.login(usernamePasswordToken);
           //3.SecurityManager通过自定义的realm的doGetAuthenticationInfo进行认证
           Subject subject= SecurityUtils.getSubject();
           UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUsername(), user.getPassword());
           try {
               subject.login(usernamePasswordToken);
               System.out.println("是否有admin:"+subject.hasRole("admin"));
              System.out.println("permissions:"+subject.isPermittedAll("user:select","user:add"));
           } catch (AuthenticationException e) {
               e.printStackTrace();
               return "登录失败";
           }
           return "Result_login";
       }
   ```

6. 数据库设计

   user表

   id	username	password
   1	mark	e10adc3949ba59abbe56e057f20f883e
   2	root	        e10adc3949ba59abbe56e057f20f883e

   user_roles表

   id	username	role_name
   1	root	             admin
   2	a	             admin

   3      a                   user

   roles_permissions表

   id	role_name	permission
   1	admin	user:select
   2	admin	user:add
   3	admin	user:delete

   关系:

   user_roles.username建立外键约束于user.username

   roles_permissions.role_name建立外键约束于user_roles.role_name

   ```xml
   <select id="givePower" resultType="string">
       select role_name from user_roles where username=#{username}
   </select>
   <select id="givePermission" resultType="string">
       select permission from roles_permissions where role_name=#{roleName}
   </select>
   ```

7. 授权注解,在Controller里面的方法上面添加(好像需要添加一些东西)

   1. 添加额外依赖

      ```xml
      <!--SSM项目中已经有jar包依赖这个依赖了,所以不需要添加了-->
      <dependency>
          <groupId>org.aspectj</groupId>
          <artifactId>aspectjweaver</artifactId>
          <version>1.8.9</version>
      </dependency>
      ```

   2. 在spring-servlet.xml(必须是这个文件)文件中开启aop配置，然后引入bean配置，将权限管理注入到Author的bean中即可。

      为什么一定是这个文件spring-servlet.xml?

      因为在web.xml中

      ```xml
      <servlet>
          <servlet-name>springMVC</servlet-name>
          <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
          <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:spring/spring-servlet.xml</param-value>
          </init-param>
          <load-on-startup>1</load-on-startup>
        </servlet>
        <servlet-mapping>
          <servlet-name>springMVC</servlet-name>
          <url-pattern>/</url-pattern>
        </servlet-mapping>
      ```

      在spring-servlet.xml中配置了

      ```xml
      <!-- 启动注解 -->
          <context:component-scan base-package="com.sz.controller">
              <!--排除service注解,专注于Controller-->
              <context:exclude-filter type="annotation"
                                      expression="org.springframework.stereotype.Service" />
          </context:component-scan>
      ```

      这就说明controller包里面用注解(@Controller)注入到spring容器的bean都是从spring-servlet.xml进行注入的

      想要在spring-servlet.xml注入的bean中使用shiro授权注解,就必须将开启shiro授权注解的xml配置写在spring-servlet.xml中

      配置如下；

      ```xml
      <!--授权注解开启-->
      <bean class="org.apache.shiro.spring.LifecycleBeanPostProcessor" id="lifecycleBeanPostProcessor"/>
          <bean class="org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor"
                id="authorizationAttributeSourceAdvisor">
              <property name="securityManager" ref="securityManager"/>
          </bean>
          <bean class="org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator" depends-on="lifecycleBeanPostProcessor">
              <property name="proxyTargetClass" value="true" />
      </bean>
      ```

   在Controller里面的方法使用注解:

   @RequiresAuthentication：表示当前Subject已经通过login 进行了身份验证；即 Subject. isAuthenticated() 返回 true
   @RequiresUser：表示当前 Subject 已经身份验证或者通过记住我登录的。
   @RequiresGuest：表示当前Subject没有身份验证或通过记住我登录过，即是游客身份。
   @RequiresRoles(value={“admin”, “user”}, logical= Logical.AND)：表示当前 Subject 需要角色 admin 和user
   @RequiresPermissions (value={“user:a”, “user:b”}, logical= Logical.OR)：表示当前 Subject 需要权限 user:a 或user:b。

8. 对realm里面的密码进行md5加密,在spring-context.xml中

   ```xml
   <!--将md5加密方法放进CustomRealm中-->
       <bean class="com.sz.shiro.realm.CustomRealm" id="realm">
           <property name="credentialsMatcher" ref="hashedCredentialsMatcher"/>
       </bean>
       <!--替换当前Realm的HashedCredentialsMatcher属性,使前台传过来的明文密码被md5加密-->
       <bean id="hashedCredentialsMatcher" class="org.apache.shiro.authc.credential.HashedCredentialsMatcher">
           <property name="hashAlgorithmName" value="md5"/>
           <property name="hashIterations" value="1"/>
       </bean>
   ```

   这个东西很智能会将登录方法里面的usernamePasswordToken中的user.getPassword()进行MD5加密

   ```java
   @RequestMapping(value="/subLogin")
       @ResponseBody
       public String subLogin(User user){
           //2.主体提交认证请求
           Subject subject= SecurityUtils.getSubject();
           UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUsername(), user.getPassword());
           try {
               subject.login(usernamePasswordToken);
               System.out.println("是否有admin:"+subject.hasRole("admin"));
               System.out.println("permissions:"+subject.isPermittedAll("user:select","use"));
               System.out.println("----------------------------------------");
           } catch (AuthenticationException e) {
               e.printStackTrace();
               return "登录失败";
           }
           return "登录成功";
       }
   ```

9. shiro缓存

   

10. Session

11. 补充对shiro的理解

    自己在学习shiro框架时,用户在登录的时候只执行认证方法而没有去执行授权doGetAuthorizationInfo()方法.发现对shiro认证与授权理解错误. 
    shiro并不是在认证之后就马上对用户授权,而是在用户认证通过之后,接下来要访问的资源或者目标方法需要权限的时候才会调用doGetAuthorizationInfo()方法,进行授权. 比如当认证通过后,访问@RequiresPermissions注解的目标方法,或者目标页面中有shiro的权限标签,这是shiro就会调用doGetAuthorizationInfo()方法.

    