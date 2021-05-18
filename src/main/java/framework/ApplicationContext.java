package framework;

import framework.annotations.Controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

public interface ApplicationContext {
    List<Class<?>> getControllers();

    void initializeContext() throws Exception;

    void findControllerClasses() throws Exception;

    void initiateControllers() throws Exception;

    void putBeanToMap(Object bean);
}
