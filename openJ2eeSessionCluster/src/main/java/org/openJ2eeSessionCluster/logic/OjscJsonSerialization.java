package org.openJ2eeSessionCluster.logic;

import org.openJ2eeSessionCluster.interfaces.OjscSerialization;
import org.openJ2eeSessionCluster.util.OjscUtils;

/**
 * serialize objects with json
 * @author mchyzer
 *
 */
public class OjscJsonSerialization implements OjscSerialization {

  /**
   * @see OjscSerialization#serialize(Object)
   */
  @Override
  public String serialize(Object object) {
    return OjscUtils.jsonConvertToNoWrap(object);
  }

  /**
   * @see OjscSerialization#unserialize(String, Class)
   */
  @Override
  public <T> T unserialize(String string, Class<T> theClass) {
    return OjscUtils.jsonConvertFrom(string, theClass);
  }


}
