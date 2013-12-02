/**
 * @author mchyzer
 * $Id: TwoFactorHibernateConfig.java,v 1.1 2013/06/20 06:02:51 mchyzer Exp $
 */
package org.openJ2eeSessionCluster.config;

import org.openJ2eeSessionCluster.interfaces.OjscEncryption;
import org.openJ2eeSessionCluster.interfaces.OjscSerialization;
import org.openJ2eeSessionCluster.interfaces.OjscSession;
import org.openJ2eeSessionCluster.interfaces.OjscSessionStorage;
import org.openJ2eeSessionCluster.util.OjscUtils;

/**
 * config stuff of OJSC
 */
public class OjscConfig extends ConfigPropertiesCascadeBase {

  

  /**
   * 
   * @return ojscsession storage
   */
  public OjscSessionStorage ojscSessionStorage() {

    String className = propertyValueString("ojsc.ojscSessionStorageImplementation");
    @SuppressWarnings("unchecked")
    Class<OjscSessionStorage> ojscSessionStorageClass = OjscUtils.forName(className);
    OjscSessionStorage ojscSessionStorage = OjscUtils.newInstance(ojscSessionStorageClass);
    return ojscSessionStorage;

  }
  
  /**
   * get the serialization implementation
   * @return the serialization
   */
  public OjscSerialization ojscSerialization() {
    
    String className = propertyValueString("ojsc.ojscSerializationImplementation");
    @SuppressWarnings("unchecked")
    Class<OjscSerialization> ojscSerializationClass = OjscUtils.forName(className);
    OjscSerialization ojscSerialization = OjscUtils.newInstance(ojscSerializationClass);
    return ojscSerialization;

  }
  
  /**
   * get the session implementation
   * @return the session
   */
  public OjscSession ojscSession() {
    
    String className = propertyValueString("ojsc.ojscSessionImplementation");
    @SuppressWarnings("unchecked")
    Class<OjscSession> ojscSessionClass = OjscUtils.forName(className);
    OjscSession ojscSession = OjscUtils.newInstance(ojscSessionClass);
    return ojscSession;

  }
  
  
  
  /**
   * get the serialization implementation
   * @return the authz
   */
  public OjscEncryption ojscEncryption() {
    
    String className = propertyValueString("ojsc.ojscEncryptionImplementation");
    @SuppressWarnings("unchecked")
    Class<OjscEncryption> ojscEncryptionClass = OjscUtils.forName(className);
    OjscEncryption ojscEncrypttion = OjscUtils.newInstance(ojscEncryptionClass);
    return ojscEncrypttion;

  }
  
  
  
  /**
   * use the factory
   */
  private OjscConfig() {
    
  }
  
  /**
   * retrieve a config from the config file or from cache
   * @return the config object
   */
  public static OjscConfig retrieveConfig() {
    return retrieveConfig(OjscConfig.class);
  }

  /**
   * @see ConfigPropertiesCascadeBase#clearCachedCalculatedValues()
   */
  @Override
  public void clearCachedCalculatedValues() {
    
  }

  /**
   * @see ConfigPropertiesCascadeBase#getHierarchyConfigKey
   */
  @Override
  protected String getHierarchyConfigKey() {
    return "ojsc.config.hierarchy";
  }

  /**
   * @see ConfigPropertiesCascadeBase#getMainConfigClasspath
   */
  @Override
  protected String getMainConfigClasspath() {
    return "openJ2eeSessionCluster.properties";
  }
  
  /**
   * @see ConfigPropertiesCascadeBase#getMainExampleConfigClasspath
   */
  @Override
  protected String getMainExampleConfigClasspath() {
    return "openJ2eeSessionCluster.base.properties";
  }

  /**
   * @see ConfigPropertiesCascadeBase#getSecondsToCheckConfigKey
   */
  @Override
  protected String getSecondsToCheckConfigKey() {
    return "ojsc.config.secondsBetweenUpdateChecks";
  }

}
