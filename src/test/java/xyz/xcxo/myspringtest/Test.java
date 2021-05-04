package xyz.xcxo.myspringtest;

import xyz.xcxo.myspring.MySpringApplicationContext;

/**
 * @author peishaopeng
 * @version 1.0.0
 * @ClassName Test
 * @Description TODO
 * @data 2021/4/19 11:12 下午
 */
public class Test {
    public static void main(String[] args) {
        MySpringApplicationContext mySpringApplicationContext = new MySpringApplicationContext(AppConfig.class);

        System.out.println(mySpringApplicationContext.getBean("userService"));
        System.out.println(mySpringApplicationContext.getBean("userService"));
    }
}
