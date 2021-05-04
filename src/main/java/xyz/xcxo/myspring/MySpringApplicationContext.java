package xyz.xcxo.myspring;

import sun.jvm.hotspot.types.Field;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;

/**
 * @author peishaopeng
 * @version 1.0.0
 * @ClassName MySpringApplicationContext
 * @Description TODO
 * @data 2021/4/19 10:50 下午
 */
public class MySpringApplicationContext {

    private Class configClass;

    public MySpringApplicationContext(Class configClass) {
        this.configClass = configClass;
        //解析配置类
        //ComponentScan注解 --> 扫描路径 --> 扫描
        ComponentScan componentScanAnnotation = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);
        //扫描路径
        String componentScanValue = componentScanAnnotation.value();
        System.out.println("获取到扫描路径 ==> " + componentScanValue);
        //获取扫描路径下需要bean工厂管理的bean
        //扫描，根据包名获取类路径并加载
        //类加载器：Bootstrap ---> jre/lib
        //        Ext ---------> jre/ext/lib
        //        App ---------> calsspath
        ClassLoader classLoader = MySpringApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource(componentScanValue.replace(".","/"));
        File file = new File(resource.getFile());
        if (file.isDirectory()){
            File[] files = file.listFiles();
            for (File f : files) {
                //只处理class文件
                if (f.getName().endsWith(".class")){
                    System.out.println("被扫描路径下的类的绝对路径 ==> " + f);
                    String className = componentScanValue + "." + f.getName().split("\\.")[0];
                    System.out.println("类的className ==> " + className);
                    //进行类加载
                    try {
                        Class<?> aClass = classLoader.loadClass(className);
                        // 判断是否存在ComponentScan注解，存在即表示当前类是一个Bean
                        if (aClass.isAnnotationPresent(Component.class)){
                            //类加载
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public Object getBean(String beanName){
        return null;
    }
}
