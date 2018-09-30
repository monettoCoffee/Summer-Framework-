# Summer-Framework-

仿Spring实现简易Bean工厂。仅供学习参考。

支持xml配置与注解配置两种方式。完成注解:Component,Value,Autowired。

1.创建ApplicationContext对象。

2.A.通过扫包,扫描注解的方式将Bean的相关信息加入IOC容器。

2.B.通过扫描xml文件的方式将Bean的相关信息加入IOC容器。

3.通过getBean方法得到一个实例。并判断这个Bean是通过xml注册还是注解注册。

4.如果当前IOC容器没有实例则创建实例。

4.根据SummerBean中封装的相关信息反射到实例Bean的属性上。

5.对Bean进行测试。
