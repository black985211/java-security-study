package org.example;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

public class App {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector();

        Context ctx = tomcat.addContext("", null);
        Tomcat.addServlet(ctx, "hello", new HelloServlet());   // 登记一个厨师，名字叫 hello
        ctx.addServletMappingDecoded("/hello", "hello");       // 访问 /hello 的请求交给 hello 处理
        Tomcat.addServlet(ctx, "login", new LoginServlet());
        ctx.addServletMappingDecoded("/login", "login");
        Tomcat.addServlet(ctx, "me", new MeServlet());
        ctx.addServletMappingDecoded("/me", "me");
        Tomcat.addServlet(ctx, "profile", new ProfileServlet());
        ctx.addServletMappingDecoded("/profile", "profile");
        Tomcat.addServlet(ctx, "users", new AdminUsersServlet());
        ctx.addServletMappingDecoded("/admin/users", "users");

        FilterDef def = new FilterDef();
        def.setFilterName("loginFilter");
        def.setFilter(new LoginFilter());
        ctx.addFilterDef(def);
        FilterMap Allmap = new FilterMap();
        Allmap.setFilterName("loginFilter");
        Allmap.addURLPattern("/*");          // /* 表示所有请求都要先过这个门卫
        ctx.addFilterMap(Allmap);

        FilterDef admindef = new FilterDef();
        admindef.setFilterName("AdminFilter");
        admindef.setFilter(new AdminFilter());
        ctx.addFilterDef(admindef);
        FilterMap adminMap = new FilterMap();
        adminMap.setFilterName("AdminFilter");
        adminMap.addURLPattern("/admin/*");
        ctx.addFilterMap(adminMap);

        tomcat.start();
        tomcat.getServer().await();   // 让程序一直运行，等待请求
    }
}