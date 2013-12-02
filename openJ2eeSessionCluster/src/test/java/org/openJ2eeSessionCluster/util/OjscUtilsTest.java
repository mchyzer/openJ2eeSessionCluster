package org.openJ2eeSessionCluster.util;

import org.apache.commons.codec.binary.Base64;

import junit.framework.TestCase;
import junit.textui.TestRunner;


public class OjscUtilsTest extends TestCase {

  /**
   * 
   * @param args
   */
  public static void main(String[] args) {
    TestRunner.run(new OjscUtilsTest("testEncrypt"));
    //TestRunner.run(new OjscUtilsTest("testJson"));

    
  }

  /**
   * 
   */
  public OjscUtilsTest() {
    super();
  }

  /**
   * 
   * @param name
   */
  public OjscUtilsTest(String name) {
    super(name);
  }
  
  /**
   * 
   */
  public void testEncrypt() {

    String originalString = "something something";
    
    String encryptedString = Base64.encodeBase64String(OjscUtils.encrypt(OjscUtils.stringGetBytes(originalString)));
    
    System.out.println(encryptedString);
    
    byte[] bytes = Base64.decodeBase64(encryptedString);
    String decryptedString = OjscUtils.stringToString(OjscUtils.decrypt(bytes));

    assertEquals(originalString, decryptedString);
  }

  /**
   * 
   */
  public void testJson() {
    Bean2 bean2a = new Bean2("abc", 123);
    Bean2 bean2b = new Bean2("def", 456);
    Bean1 bean1 = new Bean1("ghi", true, new Bean2[]{bean2a, bean2b});
    
    String json = OjscUtils.jsonConvertToNoWrap(bean1);
    
    //System.out.println(json);
    
    Bean1 newBean1 = OjscUtils.jsonConvertFrom(json, Bean1.class);
    
    assertEquals(bean1.getString1(), newBean1.getString1());
    assertEquals(bean1.isSomeBoolean(), newBean1.isSomeBoolean());
    assertEquals(OjscUtils.length(bean1.getBeans2s()), OjscUtils.length(newBean1.getBeans2s()));
    assertEquals(bean1.getBeans2s()[0].getSomeInt(), newBean1.getBeans2s()[0].getSomeInt());
    assertEquals(bean1.getBeans2s()[0].getSomeString(), newBean1.getBeans2s()[0].getSomeString());
    assertEquals(bean1.getBeans2s()[1].getSomeInt(), newBean1.getBeans2s()[1].getSomeInt());
    assertEquals(bean1.getBeans2s()[1].getSomeString(), newBean1.getBeans2s()[1].getSomeString());
    
    String json2 = "{\"beans2s\":[{\"someInt\":123,\"someString\":\"abc\",\"someStringNotExist\":\"qrs\"},{\"someInt\":456,\"someString\":\"def\"}],\"someBoolean\":true,\"string1\":\"ghi\"}";

    newBean1 = OjscUtils.jsonConvertFrom(json2, Bean1.class);
    
    assertEquals(bean1.getString1(), newBean1.getString1());
    assertEquals(bean1.isSomeBoolean(), newBean1.isSomeBoolean());
    assertEquals(OjscUtils.length(bean1.getBeans2s()), OjscUtils.length(newBean1.getBeans2s()));
    assertEquals(bean1.getBeans2s()[0].getSomeInt(), newBean1.getBeans2s()[0].getSomeInt());
    assertEquals(bean1.getBeans2s()[0].getSomeString(), newBean1.getBeans2s()[0].getSomeString());
    assertEquals(bean1.getBeans2s()[1].getSomeInt(), newBean1.getBeans2s()[1].getSomeInt());
    assertEquals(bean1.getBeans2s()[1].getSomeString(), newBean1.getBeans2s()[1].getSomeString());

//    System.out.println("\n" + json);
//    System.out.println(OjscUtils.encrypt(json));
    
  }
  
}
