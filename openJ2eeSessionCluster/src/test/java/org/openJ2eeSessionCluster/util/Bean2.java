package org.openJ2eeSessionCluster.util;

import java.io.Serializable;

/**
 * 
 * @author mchyzer
 *
 */
@SuppressWarnings("serial")
public class Bean2 implements Serializable {

  
  /**
   * 
   */
  public Bean2() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * 
   * @param someString
   * @param someInt
   */
  public Bean2(String someString, int someInt) {
    super();
    this.someString = someString;
    this.someInt = someInt;
  }

  /**
   * 
   */
  private String someString;
  
  /**
   * 
   */
  private int someInt;

  /**
   * 
   * @return the string
   */
  public String getSomeString() {
    return this.someString;
  }

  /**
   * 
   * @param someString1
   */
  public void setSomeString(String someString1) {
    this.someString = someString1;
  }

  /**
   * 
   * @return int
   */
  public int getSomeInt() {
    return this.someInt;
  }

  /**
   * 
   * @param someInt1
   */
  public void setSomeInt(int someInt1) {
    this.someInt = someInt1;
  }
  
  
  
}
