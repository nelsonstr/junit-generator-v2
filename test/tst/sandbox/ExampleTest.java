package sandbox;

import org.junit.Assert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.mockito.Mockito;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

/** 
* Example Tester. 
* 
* @author <Authors name> 
* @since <pre>Nov 7, 2016</pre> 
* @version 1.0 
*/
public class ExampleTest
{

  @Before
  public void before() throws Exception
  {
  }

  @After
  public void after() throws Exception
  {
  }

  /** 
  * 
  * Constructor: Example()  
  * 
  */
  @Test
  public void testExample() throws Exception
  {
    //Given:
    // Setup the system under test

    //When: 
    // Execute the system under test

    Example exampleInstance;
    exampleInstance = new Example();
    Assert.assertNotNull(exampleInstance);
    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Constructor: Example(int integerParameter)  
  * 
  */
  @Test
  public void testExample_Given_IntegerParameter() throws Exception
  {
    //Given:
    // Setup the system under test

    int integerParameter = 0;

    //When: 
    // Execute the system under test

    Example exampleInstance;
    exampleInstance = new Example(integerParameter);
    Assert.assertNotNull(exampleInstance);
    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Constructor: Example(String s)  
  * 
  */
  @Test
  public void testExample_Given_S() throws Exception
  {
    //Given:
    // Setup the system under test

    String s = "";

    //When: 
    // Execute the system under test

    Example exampleInstance;
    exampleInstance = new Example(s);
    Assert.assertNotNull(exampleInstance);
    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Constructor: Example(final StringBuffer string___Buffer)  
  * 
  */
  @Test
  public void testExample_Given_String___Buffer() throws Exception
  {
    //Given:
    // Setup the system under test

    StringBuffer string___Buffer = new StringBuffer();

    //When: 
    // Execute the system under test

    Example exampleInstance;
    exampleInstance = new Example(string___Buffer);
    Assert.assertNotNull(exampleInstance);
    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Method: Example(final List<String> list)  
  * 
  */
  @Test
  public void testExample_Given_List() throws Exception
  {
    //Given:
    // Setup the system under test

    List<String> list = Mockito.mock(List.class);

    //When: 
    // Execute the system under test

    Example exampleInstance;
    try {
      final Constructor<Example> example = Example.class.getDeclaredConstructor(List.class);
      example.setAccessible(true);
      exampleInstance = example.newInstance(list);
      example.setAccessible(false);
      Assert.assertNotNull(exampleInstance);
    }
    catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
    }

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Method: Example(final String s, final String b)  
  * 
  */
  @Test
  public void testExample_Given_S_and_B() throws Exception
  {
    //Given:
    // Setup the system under test

    String s = "";
    String b = "";

    //When: 
    // Execute the system under test

    Example exampleInstance;
    try {
      final Constructor<Example> example = Example.class.getDeclaredConstructor(String.class, String.class);
      example.setAccessible(true);
      exampleInstance = example.newInstance(s, b);
      example.setAccessible(false);
      Assert.assertNotNull(exampleInstance);
    }
    catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
    }

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Method: Example(final StringBuffer stringBuffer, final List<String> stringList)  
  * 
  */
  @Test
  public void testExample_Given_StringBuffer_and_StringList() throws Exception
  {
    //Given:
    // Setup the system under test

    StringBuffer stringBuffer = new StringBuffer();
    List<String> stringList = Mockito.mock(List.class);

    //When: 
    // Execute the system under test

    Example exampleInstance;
    try {
      final Constructor<Example> example = Example.class.getDeclaredConstructor(StringBuffer.class, List.class);
      example.setAccessible(true);
      exampleInstance = example.newInstance(stringBuffer, stringList);
      example.setAccessible(false);
      Assert.assertNotNull(exampleInstance);
    }
    catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
    }

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Constructor: int getAnInt()  
  * 
  */
  @Test
  public void testGetSetAnInt() throws Exception
  {
    //Given:
    // Setup the system under test

    //When: 
    // Execute the system under test

    int returnObject;
    returnObject = (int) new Example().getAnInt();

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Constructor: boolean isBbb()  
  * 
  */
  @Test
  public void testGetSetBbb() throws Exception
  {
    //Given:
    // Setup the system under test

    //When: 
    // Execute the system under test

    boolean returnObject;
    returnObject = (boolean) new Example().isBbb();

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Constructor: String getSsssssssssss()  
  * 
  */
  @Test
  public void testGetSetSsssssssssss() throws Exception
  {
    //Given:
    // Setup the system under test

    //When: 
    // Execute the system under test

    String returnObject;
    returnObject = (String) new Example().getSsssssssssss();

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Constructor: String iAmStatic(final String input)  
  * 
  */
  @Test
  public void testIAmStatic() throws Exception
  {
    //Given:
    // Setup the system under test

    String input = "";

    //When: 
    // Execute the system under test

    String returnObject;
    returnObject = (String) new Example().iAmStatic(input);

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Constructor: String firstStaticMethod(String input)  
  * 
  */
  @Test
  public void testFirstStaticMethod() throws Exception
  {
    //Given:
    // Setup the system under test

    String input = "";

    //When: 
    // Execute the system under test

    String returnObject;
    returnObject = (String) new Example().firstStaticMethod(input);

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Constructor: int secondStaticMethod()  
  * 
  */
  @Test
  public void testSecondStaticMethod() throws Exception
  {
    //Given:
    // Setup the system under test

    //When: 
    // Execute the system under test

    int returnObject;
    returnObject = (int) new Example().secondStaticMethod();

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Constructor: void thirdStaticMethod(List<String> list)  
  * 
  */
  @Test
  public void testThirdStaticMethod() throws Exception
  {
    //Given:
    // Setup the system under test

    List<String> list = Mockito.mock(List.class);

    //When: 
    // Execute the system under test

    new Example().thirdStaticMethod(list);

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Constructor: String getDetails()  
  * 
  */
  @Test
  public void testGetDetails() throws Exception
  {
    //Given:
    // Setup the system under test

    //When: 
    // Execute the system under test

    String returnObject;
    returnObject = (String) new Example().getDetails();

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Constructor: void execute()  
  * 
  */
  @Test
  public void testExecute() throws Exception
  {
    //Given:
    // Setup the system under test

    //When: 
    // Execute the system under test

    new Example().execute();

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Method: String iAmPrivate()  
  * 
  */
  @Test
  public void testIAmPrivate() throws Exception
  {
    //Given:
    // Setup the system under test

    //When: 
    // Execute the system under test

    String returnObject;
    try {
      Method method = Example.class.getClass().getMethod("iAmPrivate");
      method.setAccessible(true);
      //method.invoke( <Object>, <Parameters>) 
      returnObject = (String) method.invoke(new Example());
      method.setAccessible(false);
    }
    catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
    }

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Method: String iAmProtected(final String input)  
  * 
  */
  @Test
  public void testIAmProtected() throws Exception
  {
    //Given:
    // Setup the system under test

    String input = "";

    //When: 
    // Execute the system under test

    String returnObject;
    try {
      Method method = Example.class.getClass().getMethod("iAmProtected", String.class);
      method.setAccessible(true);
      //method.invoke( <Object>, <Parameters>) 
      returnObject = (String) method.invoke(new Example(), input);
      method.setAccessible(false);
    }
    catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
    }

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Method: void executePrivate()  
  * 
  */
  @Test
  public void testExecutePrivate() throws Exception
  {
    //Given:
    // Setup the system under test

    //When: 
    // Execute the system under test

    try {
      Method method = Example.class.getClass().getMethod("executePrivate");
      method.setAccessible(true);
      //method.invoke( <Object>, <Parameters>) 
      method.invoke(new Example());
      method.setAccessible(false);
    }
    catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
    }

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Method: String private2String(int input)  
  * 
  */
  @Test
  public void testPrivate2String() throws Exception
  {
    //Given:
    // Setup the system under test

    int input = 0;

    //When: 
    // Execute the system under test

    String returnObject;
    try {
      Method method = Example.class.getClass().getMethod("private2String", int.class);
      method.setAccessible(true);
      //method.invoke( <Object>, <Parameters>) 
      returnObject = (String) method.invoke(new Example(), input);
      method.setAccessible(false);
    }
    catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
    }

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Method: String private2StringEx(int input) throws NullPointerException 
  * 
  */
  @Test
  public void testPrivate2StringEx() throws Exception
  {
    //Given:
    // Setup the system under test

    int input = 0;

    //When: 
    // Execute the system under test

    String returnObject;
    try {
      Method method = Example.class.getClass().getMethod("private2StringEx", int.class);
      method.setAccessible(true);
      //method.invoke( <Object>, <Parameters>) 
      returnObject = (String) method.invoke(new Example(), input);
      method.setAccessible(false);
    }
    catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
    }

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Method: String private2StringEx(int input) throws NullPointerException 
  * 
  */
  @Test(expected = NullPointerException.class)
  public void testPrivate2StringEx_throws_NullPointerException() throws Exception
  {
    //Given:
    // Setup the system under test

    int input = 0;

    //When: 
    // Execute the system under test

    String returnObject;
    try {
      Method method = Example.class.getClass().getMethod("private2StringEx", int.class);
      method.setAccessible(true);
      //method.invoke( <Object>, <Parameters>) 
      returnObject = (String) method.invoke(new Example(), input);
      method.setAccessible(false);
    }
    catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
    }

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Method: String public2StringMultipleEx(int input) throws NullPointerException, RemoteException 
  * 
  */
  @Test
  public void testPublic2StringMultipleEx() throws Exception
  {
    //Given:
    // Setup the system under test

    int input = 0;

    //When: 
    // Execute the system under test

    String returnObject;
    try {
      Method method = Example.class.getClass().getMethod("public2StringMultipleEx", int.class);
      method.setAccessible(true);
      //method.invoke( <Object>, <Parameters>) 
      returnObject = (String) method.invoke(new Example(), input);
      method.setAccessible(false);
    }
    catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
    }

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Method: String public2StringMultipleEx(int input) throws NullPointerException, RemoteException 
  * 
  */
  @Test(expected = NullPointerException.class)
  public void testPublic2StringMultipleEx_throws_NullPointerException() throws Exception
  {
    //Given:
    // Setup the system under test

    int input = 0;

    //When: 
    // Execute the system under test

    String returnObject;
    try {
      Method method = Example.class.getClass().getMethod("public2StringMultipleEx", int.class);
      method.setAccessible(true);
      //method.invoke( <Object>, <Parameters>) 
      returnObject = (String) method.invoke(new Example(), input);
      method.setAccessible(false);
    }
    catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
    }

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

  /** 
  * 
  * Method: String public2StringMultipleEx(int input) throws NullPointerException, RemoteException 
  * 
  */
  @Test(expected = RemoteException.class)
  public void testPublic2StringMultipleEx_throws_RemoteException() throws Exception
  {
    //Given:
    // Setup the system under test
    int input = 0;

    //When: 
    // Execute the system under test

    String returnObject;
    try {
      Method method = Example.class.getClass().getMethod("public2StringMultipleEx", int.class);
      method.setAccessible(true);
      //method.invoke( <Object>, <Parameters>) 
      returnObject = (String) method.invoke(new Example(), input);
      method.setAccessible(false);
    }
    catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
    }

    //Then:
    // Assert that the expected change has occurred
    Assert.fail("Missing assertions");
  }

}
