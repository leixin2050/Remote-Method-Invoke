# Remote-Method-Invoke
&emsp;&emsp;RemoteMethodInvoke框架是指远程方法调用，它的进一步升级为RPC框架。它的作用在于方法的实现存在于服务器端，客户端可以通过远程调用实现此方法，方法在服务器端得以实现，然后在将结果回传给客户端，客户端与服务器的连接使用的是短连接，
在方法执行完毕且发送结果后立即断开连接。<br>
&emsp;&emsp;此框架大体分为两个部分：<br>
    &emsp;&emsp;**1、服务器端**<br>
        &emsp;&emsp;&emsp;&emsp;1）建立RMI服务器<br>
        &emsp;&emsp;&emsp;&emsp;2）侦听客户端的连接请求<br>
        &emsp;&emsp;&emsp;&emsp;3）接受客户端发送的想要执行的方法的信息（json字符串）<br>
        &emsp;&emsp;&emsp;&emsp;4）根据Method标志定位相关Method；<br>
        &emsp;&emsp;&emsp;&emsp;5）代理机制执行方法<br>
        &emsp;&emsp;&emsp;&emsp;6）返回执行结果给客户端<br>
        &emsp;&emsp;&emsp;&emsp;7）关闭连接<br>
    &emsp;&emsp;**2、客户端**<br>
        &emsp;&emsp;&emsp;&emsp;1）请求连接RMI服务器<br>
        &emsp;&emsp;&emsp;&emsp;2）向服务器发送请求调用的方法的信息（json字符串）<br>
        &emsp;&emsp;&emsp;&emsp;3）等待服务器端返回结果<br>
        &emsp;&emsp;&emsp;&emsp;4）接收到结果后断开连接<br>
