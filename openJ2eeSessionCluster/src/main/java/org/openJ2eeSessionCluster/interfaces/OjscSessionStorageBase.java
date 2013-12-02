package org.openJ2eeSessionCluster.interfaces;

import org.apache.commons.lang.StringUtils;
import org.openJ2eeSessionCluster.beans.OjscSessionStorageBean;
import org.openJ2eeSessionCluster.config.OjscConfig;
import org.openJ2eeSessionCluster.logger.OjscLog;
import org.openJ2eeSessionCluster.util.OjscUtils;

/**
 * reusable methods for session storage
 * @author mchyzer
 *
 */
public abstract class OjscSessionStorageBase implements OjscSessionStorage {

  /**
   * @see OjscSessionStorage#retrieveSessionStorageBean(String)
   */
  @Override
  public OjscSessionStorageBean retrieveSessionStorageBean(String key) {

    if (OjscUtils.isBlank(key)) {
      new RuntimeException("Why is key null????");
    }

    String sessionStorageString = this.retrieve(key);
    
    if (StringUtils.isBlank(sessionStorageString)) {
      return null;
    }
    
    //unencrypt
    sessionStorageString = OjscConfig.retrieveConfig().ojscEncryption().decryptBase64(sessionStorageString);
    
    //get the bean back
    OjscSessionStorageBean ojscSessionStorageBean = OjscConfig.retrieveConfig().ojscSerialization().unserialize(
        sessionStorageString, OjscSessionStorageBean.class);
    
    return ojscSessionStorageBean;

  }

  /**
   * @see OjscSessionStorage#storeSessionStorageBean(String, OjscSessionStorageBean)
   */
  @Override
  public boolean storeSessionStorageBean(String key,
      OjscSessionStorageBean ojscSessionStorageBean) {

    //convert to json
    String beanString = "";
    
    if (ojscSessionStorageBean != null) {
      
      //serialize to string
      beanString = OjscConfig.retrieveConfig().ojscSerialization().serialize(ojscSessionStorageBean);
      
      //encrypt and base 64
      beanString = OjscConfig.retrieveConfig().ojscEncryption().encryptBase64(beanString);
    }
    
    OjscLog.addItemToLogMap("sessionStorageSize", beanString.length());

    
    if (beanString.length() > OjscConfig.retrieveConfig().propertyValueInt("ojsc.maxSessionSizeToPersist", 1000000)) {
      
      OjscLog.addItemToLogMap("sessionStorageSizeTooLarge", true);
      return false;
      
    }
    
    this.store(key, beanString);
    
    return true;
    
  }


}
