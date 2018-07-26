# HighConcurrency
# recent edit：2018年7月26日
基于SSM的模拟高并发秒杀系统（优化中）
这是一个模拟高并发秒杀的系统，采用技术：spring、springmvc、mybatis、mysql、redis。
开发工具为：IDEA
用maven构建项目。
采用jemeter测试并发量，目前并发量qps：每秒能接受375个线程同时访问，在20秒内能完成3000个线程的访问。
因为受到电脑、采用的数据库的性能的影响，所以并发量比较低，之后会继续优化更新。
