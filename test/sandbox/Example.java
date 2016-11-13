package sandbox;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public class Example
{

  int     anInt;
  boolean bbb;
  String  ssssssssssss;

  public int getAnInt()
  {
    return anInt;
  }

  public void setAnInt(int anInt)
  {
    this.anInt = anInt;
  }

  public boolean isBbb()
  {
    return bbb;
  }

  public void setBbb(boolean bbb)
  {
    this.bbb = bbb;
  }

  public String getSsssssssssss()
  {
    return ssssssssssss;
  }

  public void setSsssssssssss(String ssssssssssss)
  {
    this.ssssssssssss = ssssssssssss;
  }

  public Example()
  {
  }

  public Example(int integerParameter)
  {
  }

  public Example(String s)
  {
  }

  public Example(final StringBuffer string___Buffer)
  {
  }

  protected Example(final List<String> list)
  {
  }

  private Example(final String s, final String b)
  {
  }

  private Example(final StringBuffer stringBuffer, final List<String> stringList)
  {
  }

  public static String iAmStatic(final String input)
  {
    return "iAmStatic " + input;
  }

  static public String firstStaticMethod(String input)
  {
    return "firstStaticMethod " + input;
  }

  static public int secondStaticMethod()
  {
    return 12345;
  }

  public static void thirdStaticMethod(List<String> list)
  {
  }

  public String getDetails()
  {
    return "Mock private method example: " + iAmPrivate();
  }

  private String iAmPrivate()
  {
    return new Date().toString();
  }

  protected String iAmProtected(final String input)
  {
    return "iAmStatic " + input;
  }

  public void execute()
  {
    executePrivate();
  }

  private void executePrivate()
  {
    firstStaticMethod("execute");
    firstStaticMethod("execute");
    secondStaticMethod();
  }

  private String private2String(int input)
  {
    return Integer.toString(input);
  }

  private String private2StringEx(int input) throws NullPointerException
  {
    return Integer.toString(input);
  }

  private String public2StringMultipleEx(int input) throws NullPointerException, RemoteException
  {
    return Integer.toString(input);
  }

}