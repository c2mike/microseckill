# microseckill
写在前面。这是我学习了微服务之后用eureka,zuul,ribbon,spring cloud config，resttemplate，redis，ribbitmq，nginx，vue做的一个项目。不能说很成功，但至少是用心做的。

首先说下自己的业务流程，前端先获取商品的秒杀地址，如果秒杀时间还没到，就获取失败，否则获取成功，获取成功后在发起秒杀请求，服务器收到请求后在redis中进行减库存的操作，如果获取库存的返回值是0，就标记该商品秒杀结束，下次再有对该商品的秒杀请求进来就不在访问redis，直接返回失败。如果减库存成功的话，就向消息队列里面写入订单消息，并且为了保证写入消息队列的可靠性，会做一次数据库记录的操作，这个操作通过异步调用另一个微服务的接口实现。

再说下自己的微服务架构如下
--两个配置中心
--两个网关中心
--一个执行秒杀操作的微服务
--一个用于同步时间的微服务
--一个用于获取秒杀列表的微服务
--一个用于读消息队列的写数据库操作的微服务
--一个配置中心
（配置中心忘记注册到eureka了）
![image](https://github.com/c2mike/microseckill/blob/master/pic/%E6%8D%95%E8%8E%B7.PNG)


关于配置中心
-- 配置中心只是在网关服务中写入了限流配置，限流数据根据测试的结果来
![image](https://github.com/c2mike/microseckill/blob/master/pic/%E6%8D%95%E8%8E%B712.PNG)


关于nginx配置
![image](https://github.com/c2mike/microseckill/blob/master/pic/%E6%8D%95%E8%8E%B711.PNG)


--关于测试
![image](https://github.com/c2mike/microseckill/blob/master/pic/memory.PNG)


因为自己的ECS实例配置为单核2G内存(实际上扣除系统占用的，可用的只有1.4G左右)部署了微服务后内存基本吃完了，可以在上图中看到内存基本用完了。就没在ECS上进行部署测试了。一下测试数据取自我个人的PC。配置为8g内存，i5cpu(8核）

秒杀列表接口的压测
测试配置开启5000个线程同时启动
![image](https://github.com/c2mike/microseckill/blob/master/pic/%E6%8D%95%E8%8E%B71.PNG)


测试结果中出现了错误，
![image](https://github.com/c2mike/microseckill/blob/master/pic/%E6%8D%95%E8%8E%B73.PNG)


我调整线程数为4500后错误率降为0,并且响应速度也加快了一倍
![image](https://github.com/c2mike/microseckill/blob/master/pic/%E6%8D%95%E8%8E%B74.PNG)


获取秒杀接口的测试
5000个线程的并发请求
![image](https://github.com/c2mike/microseckill/blob/master/pic/%E6%8D%95%E8%8E%B75.PNG)


错误率0.14%，降低线程数为4500后就没有了，
日志中可以发现错误是由于并发数量太大引起的，没有获取到信号量并且没有配置相应的服务降级
![image](https://github.com/c2mike/microseckill/blob/master/pic/%E6%8D%95%E8%8E%B76.PNG)


执行秒杀操作接口的测试
这个接口处理逻辑比较复炸，耗时较高，在并发线程数为4500时出现了较高的错误率
![image](https://github.com/c2mike/microseckill/blob/master/pic/%E6%8D%95%E8%8E%B78.PNG)


调整线程数为3500后正常，平均相应速度为2.7秒
![image](https://github.com/c2mike/microseckill/blob/master/pic/%E6%8D%95%E8%8E%B710.PNG)

最后看下测试前后有没有超卖等问题
测试前的数据
![image](https://github.com/c2mike/microseckill/blob/master/pic/%E6%8D%95%E8%8E%B72.PNG)


测试后的数据
![image](https://github.com/c2mike/microseckill/blob/master/pic/%E6%8D%95%E8%8E%B79.PNG)
都卖完了，第一行还有剩余是因为生成测试数据的时候第一行的商品被我遗漏了。
