package org.openJ2eeSessionCluster.logic;

import org.openJ2eeSessionCluster.interfaces.OjscEncryptionBase;
import org.openJ2eeSessionCluster.util.OjscUtils;

/**
 * default encryption for OJSC
 * @author mchyzer
 *
 */
public class OjscDefaultEncryption extends OjscEncryptionBase {

  /**
   * @see OjscEncryptionBase#encrypt(byte[])
   */
  @Override
  public byte[] encrypt(byte[] bytes) {
    return OjscUtils.encrypt(bytes);
  }
  
  /**
   * @see OjscEncryptionBase#decrypt(byte[])
   */
  @Override
  public byte[] decrypt(byte[] bytes) {
    return OjscUtils.decrypt(bytes);
  }
  
}
