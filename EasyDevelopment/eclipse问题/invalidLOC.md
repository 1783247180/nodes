import org.springframework.context.annotation.Bean;报错是因为pom.xml下载jar的时候因为网络问题下载不完整,所以需要去D:\reposi\org\springframework\spring-context

![1570111690475](F:\Typora\EasyDevelopment\Tomcat\img\1570111690475.png)

找到对应版本(这里是4.3.3)删除该文件夹然后再重新在pom.xml中导入

https://www.cnblogs.com/biehongli/p/8452956.html