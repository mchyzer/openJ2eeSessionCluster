/**
 * 
 */
package org.openJ2eeSessionCluster.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openJ2eeSessionCluster.beans.OjscSessionBean;


/**
 * implementers should store and retrieve the session storage.  Should extend OjscSessionBase
 * 
 * @author mchyzer
 *
 */
public interface OjscSession {

  /**
   * retrieve the session bean from the session storage, return null if none there...
   * @param request
   * @return the session bean
   */
  public OjscSessionBean retrieveSessionBean(HttpServletRequest request);
  
  /**
   * store the session bean to the session storage
   * @param request
   * @param httpServletResponse
   * @param ojscSessionBean
   */
  public void storeSessionBean(HttpServletRequest request, HttpServletResponse httpServletResponse, OjscSessionBean ojscSessionBean);
  
  /**
   * retrieve session string from session
   * @param request
   */
  public String retrieveSessionString(HttpServletRequest request);
  
  /**
   * store the session string to the session storage
   * @param request
   * @param httpServletResponse
   * @param string
   */
  public void storeSessionString(HttpServletRequest request, HttpServletResponse httpServletResponse, String string);
  
  
}
