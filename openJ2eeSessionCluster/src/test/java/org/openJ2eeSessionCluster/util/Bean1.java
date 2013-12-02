package org.openJ2eeSessionCluster.util;

import java.io.Serializable;

/**
 * 
 * @author mchyzer
 *
 */
@SuppressWarnings("serial")
public class Bean1 implements Serializable {

  /**
   * 
   */
  private String string1;
  
  /**
   * 
   */
  private boolean someBoolean;

  /**
   * bean 2s
   */
  private Bean2[] beans2s;
  
  /**
   * bean 2s
   * @return bean 2s
   */
  public Bean2[] getBeans2s() {
    return this.beans2s;
  }

  /**
   * bean 2s
   * @param beans2s1
   */
  public void setBeans2s(Bean2[] beans2s1) {
    this.beans2s = beans2s1;
  }

  /**
   * 
   * @return string
   */
  public String getString1() {
    return this.string1;
  }

  /**
   * 
   * @param string1
   * @param someBoolean
   * @param beans2s
   */
  public Bean1(String string1, boolean someBoolean, Bean2[] beans2s) {
    super();
    this.string1 = string1;
    this.someBoolean = someBoolean;
    this.beans2s = beans2s;
  }
  
  /**
   * 
   */
  public Bean1() {
    super();
  }

  /**
   * 
   * @param string1
   */
  public void setString1(String string1) {
    this.string1 = string1;
  }

  /**
   * 
   * @return some boolean
   */
  public boolean isSomeBoolean() {
    return this.someBoolean;
  }

  /**
   * 
   * @param someBoolean1
   */
  public void setSomeBoolean(boolean someBoolean1) {
    this.someBoolean = someBoolean1;
  }
  
  
  
}
