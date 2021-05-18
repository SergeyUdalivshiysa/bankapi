package framework;

import framework.annotations.Controller;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class ApplicationContext {

    private static Map<Class<?>, Object> beanMap = new HashMap<>();
    private static List<Class<?>> controllers = new ArrayList<>();

    public static List<Class<?>> getControllers() {
        return controllers;
    }

    public static void initializeContext() throws IOException, URISyntaxException, ClassNotFoundException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        findControllerClasses();
        initiateControllers();
    }

    public static void findControllerClasses() throws IOException, URISyntaxException, ClassNotFoundException {
        String basePackage = "controller";
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Enumeration<URL> resources = classLoader.getResources(basePackage);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File file = new File(resource.toURI());
            for (File classFile : Objects.requireNonNull(file.listFiles())) {
                String fileName = classFile.getName();
                if (fileName.endsWith(".class")) {
                    String className = fileName.substring(0, fileName.lastIndexOf("."));
                    Class<?> classObject = Class.forName(basePackage + "." + className);
                    if (classObject.isAnnotationPresent(Controller.class)) {
                        ApplicationContext.controllers.add(classObject);
                    }
                }
            }
        }
    }

    public static void initiateControllers() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        for (Class<?> clazz : getControllers()) {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            putBeanToMap(instance);
            clazz.getMethod("initialize").invoke(instance);
        }
    }

    private static void putBeanToMap(Object bean) {
        Class<?> clazz = bean.getClass();
        beanMap.put(clazz, bean);
    }
}
