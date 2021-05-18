package controller;

import com.sun.net.httpserver.HttpExchange;
import framework.annotations.Controller;
import framework.annotations.RequestMapping;
import web.Server;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

abstract public class AbstractController {



    protected Set<Method> methodSet = Arrays.stream(this.getClass().getMethods())
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .collect(Collectors.toSet());

    protected String basePath = this.getClass().getAnnotation(Controller.class).path();

    public void initialize(){
        Server.getServer().createContext(basePath, this::dispatch);
    }

    void dispatch(HttpExchange exchange) {
        String path = exchange.getRequestURI().getRawPath();
        String remainingPath = path.replace(basePath, "");
        try {
            if (isPathValid(remainingPath)) getAndInvokeMethod(exchange, remainingPath);
            else sendNotFound(exchange);
        }
        catch (Exception e) {
            e.printStackTrace();
            sendNotFound(exchange);
        }

    }

    protected boolean isPathValid(String remainingPath) {
        return remainingPath.matches("/?\\w*");
    }

    protected void sendNotFound(HttpExchange exchange) {

    }

    protected void getAndInvokeMethod(HttpExchange exchange, String remainingPath) throws InvocationTargetException, IllegalAccessException {
        remainingPath = remainingPath.length() > 0 ? remainingPath.substring(1) : remainingPath;
        String requestMethod = exchange.getRequestMethod();
        List<Method> filteredByMethodMethods = getMethodsByRequestMethod(requestMethod);
        Method method = getMethodByPathVariable(filteredByMethodMethods, remainingPath);
        if (getPath(method).equals("")) method.invoke(this, exchange);
        else method.invoke(this, exchange, remainingPath);
    }

    protected List<Method> getMethodsByRequestMethod(String requestMethod) {
        return this.methodSet
                .stream()
                .filter(method -> method.getAnnotation(RequestMapping.class).requestMethod()
                        .equalsIgnoreCase(requestMethod))
                .collect(Collectors.toList());
    }

    protected Method getMethodByPathVariable(List<Method> methods, String remainingPath) {
        if (remainingPath.equals("")) return methods
                .stream()
                .filter(method -> getPath(method).equals(""))
                .findFirst().get();
        else return methods.stream().filter(method -> getPath(method).matches("/\\{\\w+\\}")).findFirst().get();
    }

    protected String getPath(Method method) {
        return method.getAnnotation(RequestMapping.class).path();
    }


}
