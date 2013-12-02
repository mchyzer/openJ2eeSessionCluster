package org.openJ2eeSessionCluster.interfaces;

import org.openJ2eeSessionCluster.beans.OjscSessionStorageBean;

/**
 * store the session somewhere
 * 
 * @author mchyzer
 *
 */
public interface OjscSessionStorage {

  /**
   * store the object into the storage
   * @param key
   * @param contents
   */
  public void store(String key, String contents);
  
  /**
   * retrieve a string from storage
   * @param key
   * @return the string from the storage
   */
  public String retrieve(String key);

  /**
   * retrieve the session bean from the session storage, return null if none there...
   * @param request
   * @return the session storage bean
   */
  public OjscSessionStorageBean retrieveSessionStorageBean(String key);
  
  /**
   * store the session bean to the session storage
   * @param request
   * @param httpServletResponse
   * @param ojscSessionBean
   * @return if stored.  false if not stored since too large
   */
  public boolean storeSessionStorageBean(String key, OjscSessionStorageBean ojscSessionStorageBean);

}
