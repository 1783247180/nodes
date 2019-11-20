### SpringBoot与ODBC整合

由于自己的电脑的maven是用的阿里云的镜像拉取,而阿里云的镜像拉取中缺失odbc的jar包,所有需要自己将jar包放入本地maven库

1. 在网上自己下载到ojdbc8.jar

2. 在cmd命令中运行:mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc8 -DartifactId=ojdbc8 -Dversion=12.2.0 -Dpackaging=jar -Dfile=D:\Java\ojdbc8.jar

   注意D:\Java\ojdbc8.jar这个是你自己下载的ojdbc8.jar的路径

3. 然后在项目中

   ```xml
   <dependency>
               <groupId>com.oracle</groupId>
               <artifactId>ojdbc8</artifactId>
               <version>12.2.0</version>
   </dependency>
   ```

   

