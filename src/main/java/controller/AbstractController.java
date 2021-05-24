package controller;

import com.sun.net.httpserver.HttpExchange;
import framework.annotations.Controller;
import framework.annotations.RequestMapping;
import framework.web.Server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

abstract public class AbstractController implements ControllerInterface {

    protected Set<Method> methodSet = Arrays.stream(this.getClass().getMethods())
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .collect(Collectors.toSet());

    protected String basePath = this.getClass().getAnnotation(Controller.class).path();

    @Override
    public void initialize() {
        Server.getServer().createContext(basePath, this::dispatch);
    }

    protected void dispatch(HttpExchange exchange) {
        String path = exchange.getRequestURI().getRawPath();
        String remainingPath = path.replace(basePath, "");
        if (remainingPath.endsWith("/")) remainingPath = remainingPath.substring(0, remainingPath.length() - 1);
        if (remainingPath.equals("")) remainingPath = "/";
        try {
            if (isPathValid(remainingPath)) getAndInvokeMethod(exchange, remainingPath);
            else sendNotFound(exchange);
        } catch (Exception e) {
            e.printStackTrace();
            sendNotFound(exchange);
        }
    }

    protected boolean isPathValid(String remainingPath) {
        return remainingPath.matches("/[\\w/]*");
    }

    protected void sendNotFound(HttpExchange exchange) {
        try {
            exchange.sendResponseHeaders(404, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        exchange.close();
    }

    protected void getAndInvokeMethod(HttpExchange exchange, String remainingPath) throws InvocationTargetException, IllegalAccessException {
        String requestMethod = exchange.getRequestMethod();
        List<Method> filteredByMethodMethods = getMethodsByRequestMethod(requestMethod);
        Method method = getMethodByPathVariable(filteredByMethodMethods, remainingPath);
        if (method != null) {
            invokeMethod(method, exchange, remainingPath);
        }
        else sendNotFound(exchange);
    }

    protected void invokeMethod(Method method, HttpExchange exchange, String remainingPath) throws InvocationTargetException, IllegalAccessException {
       int parameterNumber =  method.getParameterTypes().length;
       if (parameterNumber == 1) method.invoke(this, exchange);
       if (parameterNumber > 1) {
           String parameter = getParameter(remainingPath, getMethodPath(method));
           method.invoke(this, exchange, parameter);
       }
    }

    protected String getParameter(String remainingPath, String methodPath){
        int indexOfBracket = methodPath.indexOf("{");
        remainingPath = remainingPath.substring(indexOfBracket);
        int indexOfSlash = remainingPath.indexOf("/");
        if (indexOfSlash < 0) {
            return remainingPath;
        }
        return remainingPath.substring(0, remainingPath.indexOf("/"));
    }

    protected List<Method> getMethodsByRequestMethod(String requestMethod) {
        return this.methodSet
                .stream()
                .filter(method -> method.getAnnotation(RequestMapping.class).requestMethod()
                        .equalsIgnoreCase(requestMethod))
                .collect(Collectors.toList());
    }

    protected Method getMethodByPathVariable(List<Method> methods, String remainingPath) {
       Optional<Method> optional = methods.stream()
               .filter(method -> remainingPath.matches(getMethodPathRegex(method))).findFirst();
        return optional.orElse(null);
    }

    protected String getMethodPath(Method method) {
        return method.getAnnotation(RequestMapping.class).path();
    }

    protected String getMethodPathRegex(Method method) {
        return getMethodPath(method).replaceAll("\\{.*?\\}", "\\\\w+");
    }
}
