package org.openJ2eeSessionCluster.logic;

import junit.framework.TestCase;
import junit.textui.TestRunner;


public class OjscDefaultEncryptionTest extends TestCase {

  /**
   * 
   * @param args
   */
  public static void main(String[] args) {
    TestRunner.run(new OjscDefaultEncryptionTest("testEncryption"));
  }
  
  /**
   * 
   */
  public OjscDefaultEncryptionTest() {
    super();
  }

  /**
   * 
   * @param name
   */
  public OjscDefaultEncryptionTest(String name) {
    super(name);
  }

  /**
   * 
   */
  public void testEncryption() {
    
    OjscDefaultEncryption ojscDefaultEncryption = new OjscDefaultEncryption();
    
    String string = "some string";
    String encryptedString = ojscDefaultEncryption.encryptBase64(string);
    
    System.out.println(encryptedString);
    
    String decryptedString = ojscDefaultEncryption.decryptBase64(encryptedString);
    
    assertEquals(decryptedString, string);
    
    
  }
  
}
