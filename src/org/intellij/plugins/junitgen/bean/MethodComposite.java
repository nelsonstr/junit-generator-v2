package org.intellij.plugins.junitgen.bean;

import java.util.ArrayList;
import java.util.List;

import com.intellij.psi.PsiMethod;

/**
 * A holder for the dissection of the methods
 *
 * @author Jon Osborn
 * @since 1/3/12 4:37 PM
 */
public class MethodComposite
{

  private PsiMethod             method;
  private String                name;
  private String                signature;


  private String          expectedException = "";
  private List<String>    paramClasses;
  private List<String>    paramNames;
  private List<String>    code;
  private MethodComposite base;
  private List<MethodComposite> overloadedMethods = new ArrayList<>();

  public PsiMethod getMethod()
  {
    return method;
  }

  public void setMethod(PsiMethod method)
  {
    this.method = method;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getSignature()
  {
    return signature;
  }

  public void setSignature(String signature)
  {
    this.signature = signature;
  }

  public List<String> getParamClasses()
  {
    return paramClasses;
  }

  public void setParamClasses(List<String> paramClasses)
  {
    this.paramClasses = paramClasses;
  }

  public List<Parameter> getParameters()
  {
    List<Parameter> parameters = new ArrayList<>(getParamClasses().size());
    for(int i = 0; i < getParamClasses().size(); i++) {
      Parameter parameter = new Parameter();
      parameter.setClassName(getParamClasses().get(i));
      parameter.setName(getParamNames().get(i));
      parameters.add(parameter);
    }
    return parameters;
  }

  public List<String> getParamNames()
  {
    return paramNames;
  }

  public void setParamNames(List<String> paramNames)
  {
    this.paramNames = paramNames;
  }

  public List<String> getCode()
  {
    return code;
  }

  public void setCode(List<String> code)
  {
    this.code = code;
  }

  public MethodComposite getBase()
  {
    return base;
  }

  public void setBase(MethodComposite base)
  {
    this.base = base;
  }

  public List<MethodComposite> getOverloadedMethods()
  {
    return overloadedMethods;
  }

  public void setOverloadedMethods(List<MethodComposite> overloadedMethods)
  {
    this.overloadedMethods = overloadedMethods;
  }

  public String getExpectedException()
  {
    return expectedException;
  }

  public void setExpectedException(String expectedException)
  {
    this.expectedException = expectedException;
  }

  @Override
  public String toString()
  {
    return "MethodComposite{" + "base=" + base + ", method=" + method + ", name='" + name + '\'' + ", signature='"
        + signature + '\'' + ", paramClasses=" + paramClasses + ", paramNames=" + paramNames + ", code=" + code
        + ", overloadedMethods=" + overloadedMethods + '}';
  }

}
