package framework;

import java.util.List;

public interface ApplicationContext {

    /**
     * Returns the list of all found controllers
     *
     * @return
     */
    List<Class<?>> getControllers();

    /**
     * Find all controllers and trigger its initialize
     *
     * @throws Exception
     */
    void initializeContext() throws Exception;

}
