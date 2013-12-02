package org.openJ2eeSessionCluster.logic;

import org.openJ2eeSessionCluster.util.Bean1;
import org.openJ2eeSessionCluster.util.Bean2;
import org.openJ2eeSessionCluster.util.OjscUtils;

import junit.framework.TestCase;
import junit.textui.TestRunner;


public class OjscJavaSerializationTest extends TestCase {

  /**
   * @param args
   */
  public static void main(String[] args) {
    TestRunner.run(new OjscJavaSerializationTest("testSerialization"));

  }

  /**
   * 
   */
  public OjscJavaSerializationTest() {
    super();
  }

  /**
   * 
   * @param name
   */
  public OjscJavaSerializationTest(String name) {
    super(name);
  }

  /**
   * 
   */
  public void testSerialization() {
    Bean2 bean2a = new Bean2("abc", 123);
    Bean2 bean2b = new Bean2("def", 456);
    Bean1 bean1 = new Bean1("ghi", true, new Bean2[]{bean2a, bean2b});

    OjscJavaSerialization ojscJavaSerialization = new OjscJavaSerialization();
    
    String string = ojscJavaSerialization.serialize(bean1);
    
    System.out.println(string);
        
    Bean1 newBean1 = ojscJavaSerialization.unserialize(string, Bean1.class);

    assertEquals(bean1.getString1(), newBean1.getString1());
    assertEquals(bean1.isSomeBoolean(), newBean1.isSomeBoolean());
    assertEquals(OjscUtils.length(bean1.getBeans2s()), OjscUtils.length(newBean1.getBeans2s()));
    assertEquals(bean1.getBeans2s()[0].getSomeInt(), newBean1.getBeans2s()[0].getSomeInt());
    assertEquals(bean1.getBeans2s()[0].getSomeString(), newBean1.getBeans2s()[0].getSomeString());
    assertEquals(bean1.getBeans2s()[1].getSomeInt(), newBean1.getBeans2s()[1].getSomeInt());
    assertEquals(bean1.getBeans2s()[1].getSomeString(), newBean1.getBeans2s()[1].getSomeString());
    
  }
  
}
