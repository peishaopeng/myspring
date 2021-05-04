package xyz.xcxo.myspring;

import sun.jvm.hotspot.types.Field;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author peishaopeng
 * @version 1.0.0
 * @ClassName MySpringApplicationContext
 * @Description TODO
 * @data 2021/4/19 10:50 下午
 */
public class MySpringApplicationContext {

    private Class configClass;

    /**
     * 单例池
     */
    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();
    /**
     * 扫描到的所有的Bean定义
     */
    private ConcurrentHashMap<String, BeanDefine> beanDefinitionMap = new ConcurrentHashMap<>();

    public MySpringApplicationContext(Class configClass) {
        this.configClass = configClass;
        //解析配置类
        //ComponentScan注解 --> 扫描路径 --> 扫描 --> BeanDefinition --> BeanMap
        scan(configClass);

    }

    // 扫描
    private void scan(Class configClass){
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
        URL resource = classLoader.getResource(componentScanValue.replace(".", "/"));
        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                //只处理class文件
                if (f.getName().endsWith(".class")) {
                    System.out.println("被扫描路径下的类的绝对路径 ==> " + f);
                    String className = componentScanValue + "." + f.getName().split("\\.")[0];
                    System.out.println("类的className ==> " + className);
                    // 进行类加载
                    try {
                        Class<?> aClass = classLoader.loadClass(className);
                        // 判断是否存在ComponentScan注解，存在即表示当前类是一个Bean
                        if (aClass.isAnnotationPresent(Component.class)) {
                            // 解析类 --> BeanDefine，判断Bean是单例(singleton) Bean，还是原型(prototyped) Bean,
                            // TODO 暂时不考虑懒加载
                            Component declaredAnnotation = aClass.getDeclaredAnnotation(Component.class);
                            String beanName = declaredAnnotation.value();

                            BeanDefine beanDefine = new BeanDefine();
                            // Bean 的作用域
                            if (aClass.isAnnotationPresent(Scope.class)) {
                                Scope scopeAnnotation = aClass.getDeclaredAnnotation(Scope.class);
                                beanDefine.setScope(scopeAnnotation.value());
                            } else {
                                // 没有定义作用域时，默认时单例
                                beanDefine.setScope("singleton");
                            }
                            // TODO 暂时不考虑beanName重复及未定义beanName的情况
                            beanDefinitionMap.put(beanName, beanDefine);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }


    public Object getBean(String beanName) {
        if (!beanDefinitionMap.containsKey(beanName)) {
            // 抛出Bean不存在的异常，这里用NullPointerException代替一下子 😁
            throw new NullPointerException();
        }
        BeanDefine beanDefine = beanDefinitionMap.get(beanName);
        if (beanDefine.getScope().equals("singleton")){
            //单例bean从单例池中获取
            return singletonObjects.get(beanName);
        } else {
            // 原型Bean，创建Bean对象

        }
        return null;
    }

}
