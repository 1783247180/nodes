# Redis安装

下载地址官网 https://redis.io/download
安装准备：（1）VMware Workation虚拟机（2）Linux(CentOS)系统（3）SecureCRT(Xshell也行)
安装过程：
（1）安装编译器：yum install gcc-c++（需要先安装C++编译器，redis 使用c++写的）
（2）下载Redis源码并上传到服务器
（3）解压Redis压缩包：tar -zxvf *redis*
（4）进入Redis目录进行编译：make
（2/3/4步）或者直接在Linux上($符不用输入)
$ wget http://download.redis.io/releases/redis-3.2.9.tar.gz（下载）
$ tar xzf redis-3.2.9.tar.gz（解压）
$ cd redis-3.2.9
$ make（编译）
编译后是二进制文件仅在目录中 src 可用。运行Redis：
要想更好的使用还需
（5）安装Redis：make PREFIX=/user/local/redis install
（6）将redis.conf拷贝到Redis安装目录：cp redis.conf /usr/local/redis
（7）进入安装目录，更改redis.conf文件：vim redis.conf --> daemonize no 改为 yes（之后可以后台模式运行）（vi 下按i 进行编辑 按esc后shift+zz(或者直接l俩大写Z)，或者：wq 保存并退出）
（8）启动redis后端模式：./bin/redis-server ./redis.conf
（9）终止redis的操作 ： ./bin/redis-cli shutdown
  (10) 打开redis:./bin/redis-cli使用redis,使用 set name helloworld ,就可以插入值,使用get name 查看del删除keys *  查看所有的key
  (11)ps -ef | grep -i redis查看是否已经启动



上述命令输入时遇到错误比如-bash: wget: command not found 是你电脑上没装这个功能，百度装一下就好了 这个问题 ： yum -y install wget 就OK了

# Jedis的使用

> Jedis 是 Redis 官方首选的 Java 客户端开发包

#### 虚拟机设置

- 查看虚拟机的ip

```shell
ifconfig
#将查到的ip地址写在Jedis("192.168.40.128",6379);中
```

- 将虚拟机的6379端口打开

```shell
#运行下面的命令 如果是新建的一个新的 文件，你需要先安装 iptables，再打开
vim /etc/sysconfig/iptables
## 安装命令
yum install -y iptables-services
```

- 重启服务

```shell
service iptables restart
# 执行上面的命令，如果提示
Redirecting to /bin/systemctl restart iptables.service
# 则执行
/bin/systemctl restart iptables.service
```

- 启动redis服务 （参考上篇文章）

#### java代码

- 新建一个maven的java项目
- 引入依赖

```xml
        <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>2.9.0</version>
            <type>jar</type>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.9</version>
        </dependency>
```

- 建立测试类

```java
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Auther: curry
 * @Date: 2018/5/31 23:04
 * @Description:
 */

public class Test {
    @Test
    /*
    * jedis单例测试
    * */
    public void test01(){
//1.设置IP地址和端口
        Jedis jedis=new Jedis("192.168.40.128",6379);
        //2.保存数据
        jedis.set("name","imooc");
        //3.获取数据
        String name = jedis.get("name");
        System.out.println(name);
        jedis.close();
    }
    @Test
    public void demo2(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(30);
        config.setMaxIdle(10);
        JedisPool jedisPool = new JedisPool(config,"192.168.40.128",6379);
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.set("name", "毛毛");
            String value = jedis.get("name");
            System.out.println(value);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(jedis != null){
                jedis.close();
            }
            if(jedisPool != null){
                jedisPool.destroy();
            }
        }
    }
}
```

意外问题

* 提示time out错误 需要再iptables防火墙上添加上6379端口 并重启防火墙服务
* 提示connection refuse错误 需要注释掉redis.conf 文件中的 bind 127.0.0.1
* 提示JedisDataException错误 需要将redis.conf文件中的protect-mode 置为no
  最后重启redis 

# Redis数据结构

https://blog.csdn.net/Dream_Weave/article/details/85132547