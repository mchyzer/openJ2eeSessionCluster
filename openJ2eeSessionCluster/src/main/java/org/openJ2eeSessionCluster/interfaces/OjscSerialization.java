package org.openJ2eeSessionCluster.interfaces;

/**
 * serialize an object into a string, and a string into an object.
 * note, the object *should* be serializable
 * @author mchyzer
 *
 */
public interface OjscSerialization {

  /**
   * serialize the object into a string
   * @param object
   * @return the string
   */
  public String serialize(Object object);
  
  /**
   * unserialize a string into an object
   * @param string
   * @param theClass
   * @return the object
   */
  public <T> T unserialize(String string, Class<T> theClass);
  
}
