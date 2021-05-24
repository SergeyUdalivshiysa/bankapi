package ru.volegov.framework.implementation;

import ru.volegov.framework.ApplicationContext;
import ru.volegov.framework.annotations.Controller;
import ru.volegov.util.PropertiesManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ApplicationContextImpl implements ApplicationContext {

    private static Map<Class<?>, Object> beanMap;

    static {
        beanMap = new HashMap<>();
    }

    @Override
    public Map<Class<?>, Object> getControllers() {
        return beanMap;
    }

    public static List<Class<?>> findControllerClassesFromDirectory(String path) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        File directory = new File(path);
        if (!directory.exists()) {
            return classes;
        }
        String packagePrefix = path.replace("src/main/java/", "").replace("/", ".");
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) classes.addAll(findControllerClassesFromDirectory(file.getPath()));
            if (file.getName().endsWith(".java")) {
                classes.add(Class.forName(packagePrefix + '.' + file.getName().substring(0, file.getName().lastIndexOf("."))));
            }
        }
        return classes;
    }

    @Override
    public void initializeContext() throws ClassNotFoundException, IOException, URISyntaxException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Class<?>> classes;
        File sourceFile = new File(ApplicationContext.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI());
        if (sourceFile.isDirectory()) classes = findControllerClassesFromDirectory(PropertiesManager.CONTROLLER_PATH);
        else classes = findControllerClassesFromJar(sourceFile);
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                initiateController(clazz);
            }
        }
    }

    public List<Class<?>> findControllerClassesFromJar(File sourceFile) throws IOException, URISyntaxException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        Set<String> classNames = new HashSet<>();
        try (JarFile jarFile = new JarFile(sourceFile)) {
            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName()
                            .replace("/", ".")
                            .replace(".class", "");
                    classNames.add(className);
                    if (className.matches("(.+)?controller\\..+")) {
                        Class<?> classObject = Class.forName(className);
                        classes.add(classObject);
                    }
                }
            }
            return classes;
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
