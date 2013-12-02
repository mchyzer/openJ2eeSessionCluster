package org.openJ2eeSessionCluster.beans;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * what is stored about the session 
 * @author mchyzer
 *
 */
@SuppressWarnings("serial")
public class OjscSessionStorageBean implements Serializable {

  /**
   * loginId the user used to login to the app
   */
  private String loginId;

  /**
   * loginId the user used to login to the app
   * @return loginid
   */
  public String getLoginId() {
    return this.loginId;
  }

  /**
   * loginId the user used to login to the app
   * @param loginId1
   */
  public void setLoginId(String loginId1) {
    this.loginId = loginId1;
  }

  /**
   * session bean has timestamp, user, and key
   */
  private OjscSessionBean ojscSessionBean = null;

  /**
   * session bean has timestamp, user, and key
   * @return session bean
   */
  public OjscSessionBean getOjscSessionBean() {
    return this.ojscSessionBean;
  }

  /**
   * session bean has timestamp, user, and key
   * @param ojscSessionBean1
   */
  public void setOjscSessionBean(OjscSessionBean ojscSessionBean1) {
    this.ojscSessionBean = ojscSessionBean1;
  }

  /**
   * attributes from the users session
   * @return the map
   */
  public Map<String, Object> getSessionAttributes() {
    return this.sessionAttributes;
  }

  /**
   * attributes from the users session
   * @param sessionAttributes1
   */
  public void setSessionAttributes(Map<String, Object> sessionAttributes1) {
    this.sessionAttributes = sessionAttributes1;
  }

  /**
   * attributes from the users session
   */
  private Map<String, Object> sessionAttributes = new LinkedHashMap<String, Object>();
  
}
