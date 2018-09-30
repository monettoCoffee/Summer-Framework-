package com.monetto;

import com.monetto.entry.Student;
import com.monetto.entry.StudentPack;
import com.monetto.factory.BeanFactory;

/**
 * @author monetto
 */
public class MainClass {
    public static void main(String[] args) {
        BeanFactory applicationContext = new BeanFactory();
        applicationContext.instanceBeansFromXml("summerBean.xml");
        applicationContext.instanceBeansFromAnnotation("com.monetto.entry");
        Student s1 = applicationContext.getBean("student2");
        Student s2 = applicationContext.getBean("student2");
        Student s3 = applicationContext.getBean("student3");
        Student s4 = applicationContext.getBean("student3");
        Student s5 = applicationContext.getBean("singletonStudent");
        Student s6 = applicationContext.getBean("singletonStudent");
        Student s7 = applicationContext.getBean("prototypeStudent");
        Student s8 = applicationContext.getBean("prototypeStudent");
        StudentPack s = applicationContext.getBean("studentPack");
        System.out.println("测试是否成功实例化与属性初始化");
        System.out.print(s1.getPassword());
        System.out.print(s2.getPassword());
        System.out.print(s3.getPassword());
        System.out.print(s4.getPassword());
        System.out.print(s5.getPassword());
        System.out.print(s6.getPassword());
        System.out.print(s7.getPassword());
        System.out.println(s8.getPassword());
        System.out.println("测试单例与多例是否一致");
        System.out.println(s1 == s2);
        System.out.println(s3 == s4);
        System.out.println(s5 == s6);
        System.out.println(s7 == s8);
        System.out.println("测试是否完成自动注入");
        System.out.println(s.test());
    }
}
