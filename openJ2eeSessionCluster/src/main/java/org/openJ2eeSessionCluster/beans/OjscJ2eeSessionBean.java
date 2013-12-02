package org.openJ2eeSessionCluster.beans;

import java.io.Serializable;

/**
 * bean stored in session for ojsc
 * @author mchyzer
 *
 */
@SuppressWarnings("serial")
public class OjscJ2eeSessionBean implements Serializable {

  /**
   * count how many j2ee beans are too large
   */
  private int countTooLarge = 0;
  
  /**
   * count how many j2ee beans are too large
   * @return count
   */
  public int getCountTooLarge() {
    return this.countTooLarge;
  }

  /**
   * count how many j2ee beans are too large
   * @param countTooLarge1
   */
  public void setCountTooLarge(int countTooLarge1) {
    this.countTooLarge = countTooLarge1;
  }

  /**
   * ojsc session bean in session attribute
   */
  public static final String SESSION_ATTRIBUTE_KEY = "ojscJ2eeSessionBean";

  /**
   * ojsc session bean sent to browser
   */
  private OjscSessionBean ojscSessionBean;

  /**
   * ojsc session bean sent to browser
   * @return ojsc session bean
   */
  public OjscSessionBean getOjscSessionBean() {
    return this.ojscSessionBean;
  }

  /**
   * ojsc session bean sent to browser
   * @param ojscSessionBean1
   */
  public void setOjscSessionBean(OjscSessionBean ojscSessionBean1) {
    this.ojscSessionBean = ojscSessionBean1;
  }
  
}
