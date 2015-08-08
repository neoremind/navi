#Navi
![](https://travis-ci.org/neoremind/navi.svg?branch=master)

Navi is a distributed service framework that provides cluster management and high performance RPC. With Navi, you can easily distribute application with minimal effort to create a highly scalable architecture capable of handing remote procudure call and service registration and discovery.

Implemented in Java and Spring framework, navi wraps Zookeeper and uses Protostuff/Protobuf transport to make it easy to build up cluster aware applications. Naiv allows you to focus efforts on your application logic, so programming experience is very friendly with its simple XML and annotation configuration.

Navi可以理解为Navigation的简称，即“制导/导航”。

Navi是一个分布式服务框架，提供高性能和无侵入式的RPC远程服务调用方案，以及SOA服务治理方案，秉承简单够用、灵活扩展的开发原则。

利用Java和Spring Framework实现，集成Zookeeper，使用HTTP原生通信传输，利用Protostuff/Protobuf作为序列化协议，同时提供了无侵入式的灵活配置方式，XML或者注解方式使开发一个服务变得非常容易。

#Features 
* Providing high availability. By using Zookeeper for underlying group management, Navi can make it easy to publish/remove service nodes.
* Providing Protobuf to serialize and deserialize data.
* Using optional software loading balancing and failover strategy.
* Simple configuration with XML or annotaion way to expose service in Spring.

#特点
##远程通讯
透明化的远程方法调用，就像调用本地方法一样调用远程方法，只需简单配置，没有任何API侵入。提供基于长连接的、池化HTTP的通讯基础，提供“请求-应答”模式的信息交换。

##集群容错
高性能、多序列化协议支持，以及软负载均衡，失败容错，地址路由等集群支持。

##自动发现
基于注册中心目录服务，使服务消费方能动态的查找服务提供方，使地址透明，集群整体保证高可用性。服务发布简单自由，使服务提供方可以平滑增加机器，做到横向水平扩展。


#User Guide
[使用手册(中文版)](https://github.com/neoremind/navi/wiki/%E4%BD%BF%E7%94%A8%E6%89%8B%E5%86%8C-%E4%B8%AD%E6%96%87%E7%89%88)  

[配置说明](https://github.com/neoremind/navi/wiki/%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E)

#Development
[设计文档](https://github.com/neoremind/navi/wiki/%E8%AE%BE%E8%AE%A1%E6%96%87%E6%A1%A3)

[性能测试](https://github.com/neoremind/navi/wiki/%E6%80%A7%E8%83%BD%E6%B5%8B%E8%AF%95)

