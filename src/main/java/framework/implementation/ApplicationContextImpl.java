package framework.implementation;

import framework.ApplicationContext;
import framework.annotations.Controller;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class ApplicationContextImpl implements ApplicationContext {

    private static Map<Class<?>, Object> beanMap = new HashMap<>();
    private static List<Class<?>> controllers = new ArrayList<>();

    public List<Class<?>> getControllers() {
        return controllers;
    }

    public void initializeContext() throws ClassNotFoundException, IOException, URISyntaxException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        findControllerClasses("controller");
        initiateControllers();
    }

    public void findControllerClasses(String basePackage) throws IOException, URISyntaxException, ClassNotFoundException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Enumeration<URL> resources = classLoader.getResources(basePackage);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File file = new File(resource.toURI());
            for (File classFile : Objects.requireNonNull(file.listFiles())) {
                String fileName = classFile.getName();
                if (classFile.isDirectory()) findControllerClasses(basePackage + "/" + classFile.getName());
                if (fileName.endsWith(".class")) {
                    String className = fileName.substring(0, fileName.lastIndexOf("."));
                    basePackage = basePackage.replace("/", ".");
                    Class<?> classObject = Class.forName(basePackage + "." + className);
                    if (classObject.isAnnotationPresent(Controller.class)) {
                        ApplicationContextImpl.controllers.add(classObject);
                    }
                }
            }
        }
    }


    public void initiateControllers() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Class<?> clazz : getControllers()) {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            putBeanToMap(instance);
            clazz.getMethod("initialize").invoke(instance);
        }
    }

    public void putBeanToMap(Object bean) {
        Class<?> clazz = bean.getClass();
        beanMap.put(clazz, bean);
    }
}
