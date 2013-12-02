package org.openJ2eeSessionCluster.interfaces;

/**
 * encrypt a String and back.  You should extend OjscEncryptionBase
 * @author mchyzer
 *
 */
public interface OjscEncryption {

  /**
   * encrypt some bytes
   * @param bytes
   * @return the encrypted bytes
   */
  public byte[] encrypt(byte[] bytes);
  
  /**
   * decrypt some bytes
   * @param bytes
   * @return the bytes
   */
  public byte[] decrypt(byte[] bytes);
  
  /**
   * encrypt some bytes
   * @param bytes
   * @return the encrypted bytes
   */
  public String encryptBase64(String string);
  
  /**
   * decrypt some bytes
   * @param bytes
   * @return the bytes
   */
  public String decryptBase64(String string);
  
}
