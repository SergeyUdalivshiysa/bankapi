package framework.implementation;

import framework.ApplicationContext;
import framework.annotations.Controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationContextImpl implements ApplicationContext {

    private static Map<Class<?>, Object> beanMap;

    static {
        beanMap = new HashMap<>();
    }

    public static List<Class<?>> findControllerClasses(String path) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        File directory = new File(path);
        if (!directory.exists()) {
            return classes;
        }
        String packagePrefix = path.replace("src/main/java/", "").replace("/", ".");
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) classes.addAll(findControllerClasses(file.getPath()));
            if (file.getName().endsWith(".java")) {
                classes.add(Class.forName(packagePrefix + '.' + file.getName().substring(0, file.getName().lastIndexOf("."))));
            }
        }
        return classes;
    }

    @Override
    public List<Class<?>> getControllers() {
        return (List<Class<?>>) beanMap.keySet();
    }

    @Override
    public void initializeContext() throws ClassNotFoundException, IOException, URISyntaxException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Class<?>> classes = findControllerClasses("src/main/java/controller");
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                initiateController(clazz);
            }
        }
    }

    public void initiateController(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Object instance = clazz.getDeclaredConstructor().newInstance();
        putBeanToMap(instance);
        clazz.getMethod("initialize").invoke(instance);
    }

    public void putBeanToMap(Object bean) {
        Class<?> clazz = bean.getClass();
        beanMap.put(clazz, bean);
    }
}
