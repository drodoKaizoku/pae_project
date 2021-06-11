package be.ipl.pae.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@SuppressWarnings("rawtypes")
public class InjectionService {

  private Map<Class, Object> singleton = new HashMap<>();
  private Properties props = new Properties();

  /**
   * Creates the InjectionService Object and reads/loads the file.
   * 
   * @param fileName the path to the properties file
   */
  public InjectionService(String fileName) {
    try (FileInputStream file = new FileInputStream(fileName)) {
      props.load(file);
    } catch (IOException ioException) {
      System.exit(1);
    }
  }

  /**
   * Inject the needed dependecies into the implemented class from the interface class in
   * parameters.
   * 
   * @param interfaceOfInstance the interface of the class wich needs dependencies
   * @return the implemented class with its dependencies
   */
  public Object getImplementingInstance(Class interfaceOfInstance) {
    // pour toujours avoir une seule instanciation de l'interface injection de dependance est
    // d'avoir une seule instanciation
    if (singleton.containsKey(interfaceOfInstance)) {
      return singleton.get(interfaceOfInstance);
    }

    String nameImpl = props.getProperty(interfaceOfInstance.getSimpleName());

    try {
      Constructor<?> constructor = Class.forName(nameImpl).getDeclaredConstructors()[0];
      constructor.setAccessible(true);
      Class<?>[] parameterList = constructor.getParameterTypes();
      List<Object> parameterInstancied = new ArrayList<Object>();

      // On injecte les dependance par reccursivite cad on va placer un constructeur
      // dans une servlet qu'on ajoute au contexte dans le main et dans cette servlet
      // on met un constructeur qui a IUserUcc IUserDto etc... et pour chaque parametre
      // fin ainsi de suite
      for (Class<?> currentClass : parameterList) {
        parameterInstancied.add(getImplementingInstance(currentClass));
      }

      Object instance = constructor.newInstance(parameterInstancied.toArray());
      singleton.put(interfaceOfInstance, instance);
      return instance;

    } catch (Exception exception) {
      System.exit(1);
      return null;
    }

  }

  public String getVariable(String variableName) {
    String variableValue = props.getProperty(variableName);
    return variableValue;
  }

}
