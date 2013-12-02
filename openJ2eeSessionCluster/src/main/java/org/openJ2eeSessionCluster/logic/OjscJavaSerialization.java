package org.openJ2eeSessionCluster.logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.openJ2eeSessionCluster.interfaces.OjscSerialization;
import org.openJ2eeSessionCluster.util.OjscUtils;

/**
 * serialize objects with java
 * @author mchyzer
 *
 */
public class OjscJavaSerialization implements OjscSerialization {

  /**
   * @see OjscSerialization#serialize(Object)
   */
  @Override
  public String serialize(Object object) {
    if (object == null) {
      return "";
    }
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream( baos );
      oos.writeObject( object );
      oos.close();
      return OjscUtils.stringToString( Base64.encodeBase64( baos.toByteArray() ) );
    } catch (Exception e) {
      throw new RuntimeException("Cannot serialize: " + object.getClass(), e);
    }
  }

  /**
   * @see OjscSerialization#unserialize(String, Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T unserialize(String string, Class<T> theClass) {
    
    if (OjscUtils.isBlank(string)) {
      return null;
    }
    
    try {
      byte [] data = Base64.decodeBase64(string);
      ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(  data ) );
      Object o  = ois.readObject();
      ois.close();
      return (T)o;
      
    } catch (Exception e) {
      throw new RuntimeException("Cannot unserialize string", e);
    }
  }


  
}
