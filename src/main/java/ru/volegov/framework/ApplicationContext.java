package ru.volegov.framework;

import java.util.Map;

public interface ApplicationContext {

    /**
     * Returns the Map of all found controllers classes and object instances
     *
     * @return
     */
    Map<Class<?>, Object> getControllers();

    /**
     * Find all controllers and trigger its initialize
     *
     * @throws Exception
     */
    void initializeContext() throws Exception;

}
