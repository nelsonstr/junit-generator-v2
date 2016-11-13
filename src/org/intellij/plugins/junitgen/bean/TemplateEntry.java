package org.intellij.plugins.junitgen.bean;

import java.util.List;

/**
 * DataHolder class. Needs to be public since velocity is using it in the
 * template.
 *
 * @author Jon Osborn
 */
public class TemplateEntry
{

  private final List<MethodComposite> methodList;
  private final List<MethodComposite> privateMethodList;
  private final List<String>          fieldList;
  private final List<MethodComposite> publicConstructorsCompositeList;

  private final List<MethodComposite> nonPublicConstructorCompositeList;


  private final List<String> importList;

  private String className;
  private String packageName;

  public TemplateEntry(final String className,
      final String packageName,
      final List<String> importList,
      final List<MethodComposite> publicConstructorsCompositeList,
      final List<MethodComposite> nonPublicConstructorCompositeList,
      final List<MethodComposite> methodList,
      final List<MethodComposite> privateMethodList,
      final List<String> fieldList)
  {
    this.className = className;
    this.packageName = packageName;
    this.importList = importList;
    this.publicConstructorsCompositeList = publicConstructorsCompositeList;
    this.nonPublicConstructorCompositeList = nonPublicConstructorCompositeList;
    this.methodList = methodList;
    this.privateMethodList = privateMethodList;
    this.fieldList = fieldList;
  }

  public String getClassName()
  {
    return className;
  }

  public String getPackageName()
  {
    return packageName;
  }

  public List<String> getFieldList()
  {
    return fieldList;
  }

  public List<MethodComposite> getMethodList()
  {
    return methodList;
  }

  public List<MethodComposite> getPrivateMethodList()
  {
    return privateMethodList;
  }

  public List<MethodComposite> getPublicConstructorsCompositeList()
  {
    return publicConstructorsCompositeList;
  }

  public List<MethodComposite> getNonPublicConstructorCompositeList()
  {
    return nonPublicConstructorCompositeList;
  }

  public List<String> getImportList()
  {
    return importList;
  }

}
