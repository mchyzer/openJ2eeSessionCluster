package org.openJ2eeSessionCluster.interfaces;

import org.apache.commons.codec.binary.Base64;
import org.openJ2eeSessionCluster.util.OjscUtils;

/**
 * implement things in the encryption interface that dont depend on the encryption
 * @author mchyzer
 *
 */
public abstract class OjscEncryptionBase implements OjscEncryption {

  /**
   * @see OjscEncryption#encryptBase64(String)
   */
  @Override
  public String encryptBase64(String string) {
    byte[] bytes = OjscUtils.stringGetBytes(string);
    bytes = this.encrypt(bytes);
    bytes = Base64.encodeBase64(bytes);
    String result = OjscUtils.stringToString(bytes);
    return result;
  }

  /**
   * @see OjscEncryption#decryptBase64(String)
   */
  @Override
  public String decryptBase64(String string) {
    byte[] bytes = OjscUtils.stringGetBytes(string);
    bytes = Base64.decodeBase64(bytes);
    bytes = this.decrypt(bytes);
    return OjscUtils.stringToString(bytes);
  }

}
