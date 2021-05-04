package xyz.xcxo.myspring;

/**
 * @author peishaopeng
 * @version 1.0.0
 * @ClassName BeanDefine
 * @Description Bean 定义
 * @data 2021/5/5 1:58 上午
 */
public class BeanDefine {

    /**
     * Bean 类型
     */
    private Class clazz;

    /**
     * 作用域
     */
    private String scope;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    // 是否是懒加载等...
}
