package framework;

import java.util.List;

public interface ApplicationContext {
    List<Class<?>> getControllers();

    void initializeContext() throws Exception;

    void findControllerClasses(String basePackage) throws Exception;

    void initiateControllers() throws Exception;

    void putBeanToMap(Object bean);
}
