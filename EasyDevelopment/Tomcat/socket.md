# 预备知识

## 补码

正数的原码,反码,补码都是相同的

负数的补码用蒋老师的方式算

## HTTP与TCP

从OSI参考模型上讲，TCP属于运输层的一个很重要的协议，负责提供应用进程之间的通信。而HTTP属于应用层上的一种协议。你可以理解为HTTP是上层的协议，需要下层TCP支持

## UDP与TCP

1、连接方面区别

TCP面向连接如打电话要先拨号建立连接。

UDP是无连接的，即发送数据之前不需要建立连接。

2、安全方面的区别

TCP提供可靠的服务，通过TCP连接传送的数据，无差错，不丢失，不重复，且按序到达。

UDP尽最大努力交付，即不保证可靠交付。

3、传输效率的区别

TCP传输效率相对较低。

UDP传输效率高，适用于对高速传输和实时性有较高的通信或广播通信。

4、连接对象数量的区别

TCP连接只能是点到点、一对一的。

UDP支持一对一，一对多，多对一和多对多的交互通信。

## try-with-resource

JAVA的一大特性就是JVM会对内部资源实现自动回收，即自动GC，给开发者带来了极大的便利。但是JVM对外部资源的引用却无法自动回收，例如数据库连接，网络连接以及输入输出IO流等，这些连接就需要我们手动去关闭，不然会导致外部资源泄露，连接池溢出以及文件被异常占用等。

JDK1.7之后有了try-with-resource处理机制。首先被自动关闭的资源需要实现Closeable或者AutoCloseable接口，因为只有实现了这两个接口才可以自动调用close()方法去自动关闭资源。写法为try(){}catch(){}，将要关闭的外部资源在try()中创建，catch()捕获处理异常。其实try-with-resource机制是一种语法糖，其底层实现原理仍然是try{}catch(){}finally{}写法，不过在catch(){}代码块中有一个addSuppressed()方法，即异常抑制方法。如果业务处理和关闭连接都出现了异常，业务处理的异常会抑制关闭连接的异常，只抛出处理中的异常，仍然可以通过getSuppressed()方法获得关闭连接的异常。
![img](F:\Typora\EasyDevelopment\Tomcat\img\1.png)

# InetAddress类

API:

```java
       // 获取本机的InetAddress实例
        InetAddress address = InetAddress.getLocalHost();
        System.out.println("IP地址：" + address.getHostAddress());
        byte[] bytes = address.getAddress();// 获取字节数组形式的IP地址
        List<Integer> list=new ArrayList();
        for (byte a:bytes) {
           int q= a & 0xff;
           list.add(q);
        }
        System.out.println("字节数组形式的IP：" + Arrays.toString(bytes));
        System.out.println("list集合: "+list);
```

```txt
IP地址：192.168.40.1
字节数组形式的IP：[-64, -88, 40, 1]
list集合: [192, 168, 40, 1]
```

这里就有问题了,IP地址是192.168.40.1,为什么转成字节数组形式就变成了[-64, -88, 40, 1]?

这里因为address.getAddress()方法的返回值是byte数组,其次ipv4是4个0-255间的整数组成的，byte是1字节8 位，正好满足(2的8次方=256)，所以用byte去接是合适的。但由于java中没有unsigned的类型，所有数都是带有正负位的，所以最高位的 1 将会被解释为符号位,另外 Java 中存储是按照补码存储，如果是地址中一个是168，转二进制 1010 1000，这会被java认为是补码形式，转换成原码便是 1101 1000，转换成十进制数便是 -88。

你没法使用byte直接输出地址，必须转为更高级的类型。将 byte 数组中的值与 0xFF 按位与(&)(自动转换类型)，过程中 byte 会隐式类型转换为 int，当与 0xFF 按位与的时候，会将除了低 8 位的其他位全部置 0

# Socket类

```java
client:
//1.创建客户端Socket，指定服务器地址和端口
Socket socket=new Socket("localhost", 8888);
//2.获取输出流，向服务器端发送信息
OutputStream os=socket.getOutputStream();//字节输出流

server:
//1.创建一个服务器端Socket，即ServerSocket，指定绑定的端口，并监听此端口
ServerSocket serverSocket=new ServerSocket(8888);
socket=serverSocket.accept();
//获取输入流，并读取客户端信息
socket.getInputStream();
```



# URL类

API

```java
//创建一个URL实例
URL url = new URL("http://www.baidu.com");
//通过URL的openStream方法获取URL对象所表示的资源的字节输入流
InputStream is = url.openStream();
```

is的内容就是

```html
<!DOCTYPE html>
<!--STATUS OK--><html> <head><meta http-equiv=content-type content=text/html;charset=utf-8><meta http-equiv=X-UA-Compatible content=IE=Edge><meta content=always name=referrer><link rel=stylesheet type=text/css href=http://s1.bdstatic.com/r/www/cache/bdorz/baidu.min.css><title>百度一下，你就知道</title></head> <body link=#0000cc> <div id=wrapper> <div id=head> <div class=head_wrapper> <div class=s_form> <div class=s_form_wrapper> <div id=lg> <img hidefocus=true src=//www.baidu.com/img/bd_logo1.png width=270 height=129> </div> <form id=form name=f action=//www.baidu.com/s class=fm> <input type=hidden name=bdorz_come value=1> <input type=hidden name=ie value=utf-8> <input type=hidden name=f value=8> <input type=hidden name=rsv_bp value=1> <input type=hidden name=rsv_idx value=1> <input type=hidden name=tn value=baidu><span class="bg s_ipt_wr"><input id=kw name=wd class=s_ipt value maxlength=255 autocomplete=off autofocus></span><span class="bg s_btn_wr"><input type=submit id=su value=百度一下 class="bg s_btn"></span> </form> </div> </div> <div id=u1> <a href=http://news.baidu.com name=tj_trnews class=mnav>新闻</a> <a href=http://www.hao123.com name=tj_trhao123 class=mnav>hao123</a> <a href=http://map.baidu.com name=tj_trmap class=mnav>地图</a> <a href=http://v.baidu.com name=tj_trvideo class=mnav>视频</a> <a href=http://tieba.baidu.com name=tj_trtieba class=mnav>贴吧</a> <noscript> <a href=http://www.baidu.com/bdorz/login.gif?login&amp;tpl=mn&amp;u=http%3A%2F%2Fwww.baidu.com%2f%3fbdorz_come%3d1 name=tj_login class=lb>登录</a> </noscript> <script>document.write('<a href="http://www.baidu.com/bdorz/login.gif?login&tpl=mn&u='+ encodeURIComponent(window.location.href+ (window.location.search === "" ? "?" : "&")+ "bdorz_come=1")+ '" name="tj_login" class="lb">登录</a>');</script> <a href=//www.baidu.com/more/ name=tj_briicon class=bri style="display: block;">更多产品</a> </div> </div> </div> <div id=ftCon> <div id=ftConw> <p id=lh> <a href=http://home.baidu.com>关于百度</a> <a href=http://ir.baidu.com>About Baidu</a> </p> <p id=cp>&copy;2017&nbsp;Baidu&nbsp;<a href=http://www.baidu.com/duty/>使用百度前必读</a>&nbsp; <a href=http://jianyi.baidu.com/ class=cp-feedback>意见反馈</a>&nbsp;京ICP证030173号&nbsp; <img src=//www.baidu.com/img/gs.gif> </p> </div> </div> </div> </body> </html>
```

# Socket实现UDP

这个暂时了解即可

客户端

```java
package com.imooc;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
/*
 * 客户端
 */
public class UDPClient {
	public static void main(String[] args) throws IOException {
		/*
		 * 向服务器端发送数据
		 */
		//1.定义服务器的地址、端口号、数据
		InetAddress address=InetAddress.getByName("localhost");
		int port=8800;
		byte[] data="用户名：admin;密码：123".getBytes();
		//2.创建数据报，包含发送的数据信息
		DatagramPacket packet=new DatagramPacket(data, data.length, address, port);
		//3.创建DatagramSocket对象
		DatagramSocket socket=new DatagramSocket();
		//4.向服务器端发送数据报
		socket.send(packet);	
		/*
		 * 接收服务器端响应的数据
		 */
		//1.创建数据报，用于接收服务器端响应的数据
		byte[] data2=new byte[1024];
		DatagramPacket packet2=new DatagramPacket(data2, data2.length);
		//2.接收服务器响应的数据
		socket.receive(packet2);
		//3.读取数据
		String reply=new String(data2, 0, packet2.getLength());
		System.out.println("我是客户端，服务器说："+reply);
		//4.关闭资源
		socket.close();
	}
}
```

服务器

```java
package com.imooc;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
/*
 * 服务器端，实现基于UDP的用户登陆
 */
public class UDPServer {
	public static void main(String[] args) throws IOException {
		/*
		 * 接收客户端发送的数据
		 */
		//1.创建服务器端DatagramSocket，指定端口
		DatagramSocket socket=new DatagramSocket(8800);
		//2.创建数据报，用于接收客户端发送的数据
		byte[] data =new byte[1024];//创建字节数组，指定接收的数据包的大小
		DatagramPacket packet=new DatagramPacket(data, data.length);
		//3.接收客户端发送的数据
		System.out.println("****服务器端已经启动，等待客户端发送数据");
		socket.receive(packet);//此方法在接收到数据报之前会一直阻塞
		//4.读取数据
		String info=new String(data, 0, packet.getLength());
		System.out.println("我是服务器，客户端说："+info);
		/*
		 * 向客户端响应数据
		 */
		//1.定义客户端的地址、端口号、数据
		InetAddress address=packet.getAddress();
		int port=packet.getPort();
		byte[] data2="欢迎您!".getBytes();
		//2.创建数据报，包含响应的数据信息
		DatagramPacket packet2=new DatagramPacket(data2, data2.length, address, port);
		//3.响应客户端
		socket.send(packet2);
		//4.关闭资源
		socket.close();
	}
}
```



# Socket实现TCP

服务器端

1. 创建ServerSocket对象,绑定监听端口
2. 通过accept()方法监听客户端请求
3. 连接建立后,通过输入流读取客户端发送的请求信息
4. 通过输出流向客户端发送响应信息
5. 关闭相关资源(try-with-resource)

```java
package my;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket
public class MyServer {
    public static void main(String[] args) {
try(ServerSocket serverSocket=new ServerSocket(8888)){
    while (true){
        Socket socket = serverSocket.accept();
        MyServerThread myServerThread = new MyServerThread(socket);
myServerThread.start();
        InetAddress address=socket.getInetAddress();
        System.out.println("当前客户端的IP："+address.getHostAddress());
    }

} catch (IOException e) {
    e.printStackTrace();
}
    }
}
```

```java
package my;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class MyServerThread extends Thread{
    private Socket socket;
    public MyServerThread(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try(
                BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter pw=new PrintWriter(socket.getOutputStream())
                ){
            String info;
            while((info=br.readLine())!=null){
                System.out.println("我是服务器，客户端说："+info);
            }
            socket.shutdownInput();
            //向客户端写数据
            pw.write("欢迎您！");
            pw.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```



客户端

1. 创建Socket对象,指明需要连接的服务器的地址和端口号
2. 连接建立后,通过输出流向服务器发送请求信息
3. 通过输入流获取服务器响应的信息
4. 关闭资源

```java
import java.io.*;
import java.net.Socket;
public class MyClient {
    public static void main(String[] args) {
try(
        Socket socket=new Socket("127.0.0.1",8888);
        PrintWriter pw=new PrintWriter(socket.getOutputStream());
        BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ){
    //写数据到服务器
    pw.write("用户名：root;密码：root");
    pw.flush();
    socket.shutdownOutput();//关闭输出流
    //从服务器读数据
    String info=null;
while((info=br.readLine())!=null){
    System.out.println("我是客户端，服务器说："+info);
}
}catch (IOException e) {
    e.printStackTrace();}}}
```

