package org.intellij.plugins.junitgen.action;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.apache.velocity.runtime.resource.util.StringResourceRepositoryImpl;
import org.intellij.plugins.junitgen.JUnitGeneratorContext;
import org.intellij.plugins.junitgen.JUnitGeneratorFileCreator;
import org.intellij.plugins.junitgen.bean.MethodComposite;
import org.intellij.plugins.junitgen.bean.Parameter;
import org.intellij.plugins.junitgen.bean.TemplateEntry;
import org.intellij.plugins.junitgen.util.DateTool;
import org.intellij.plugins.junitgen.util.JUnitGeneratorUtil;
import org.intellij.plugins.junitgen.util.LogAdapter;
import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiTypeElement;

/**
 * This is where the magic happens.
 *
 * @author Alex Nazimok (SCI)
 * @author Jon Osborn
 * @author By: Bryan Gilbert, July 18, 2008
 * @since <pre>Sep 3, 2003</pre>
 */
public class JUnitGeneratorActionHandler extends EditorWriteActionHandler
{

  private static final Logger  logger                = JUnitGeneratorUtil.getLogger(JUnitGeneratorActionHandler.class);

  private static final String  VIRTUAL_TEMPLATE_NAME = "junitgenerator.vm";
  public static final String   SET                   = "set";
  public static final String   GET                   = "get";
  public static final String   IS                    = "is";
  public static final String   GIVEN                 = "_Given_";
  public static final String   AND                   = "_and_";
  public static final String   CLASS                 = ".class";

  private final String         templateKey;

  private static final Pattern ISGETSET              = Pattern.compile("^(is|get|set)(.*)");
  public static final String   PUBLIC                = "public";
  private static final String  PARAMETER_SEPARATOR   = ", ";

  public JUnitGeneratorActionHandler(String name)
  {
    this.templateKey = name;
  }

  public String getTemplate(Project project)
  {
    return JUnitGeneratorUtil.getInstance(project).getTemplate(this.templateKey);
  }

  /**
   * Executed upon action in the Editor
   *
   * @param editor      IDEA Editor
   * @param dataContext DataCOntext
   */
  public void executeWriteAction(Editor editor, DataContext dataContext)
  {
    final PsiJavaFile file = JUnitGeneratorUtil.getSelectedJavaFile(dataContext);

    if(file == null) {
      return;
    }
    if((this.templateKey == null) || this.templateKey.trim().isEmpty()
        || (getTemplate(DataKeys.PROJECT.getData(dataContext)) == null)) {
      String title = JUnitGeneratorUtil.getProperty("junit.generator.error.title");
      String noSelectedTemplate = JUnitGeneratorUtil.getProperty("junit.generator.error.noselectedtemplate");
      JOptionPane.showMessageDialog(null, noSelectedTemplate, title, JOptionPane.ERROR_MESSAGE);
      return;
    }

    final PsiClass[] psiClasses = file.getClasses();
    if(psiClasses == null) {
      return;
    }

    for(final PsiClass psiClass : psiClasses) {
      if((psiClass != null) && (psiClass.getQualifiedName() != null)) {
        final JUnitGeneratorContext genCtx = new JUnitGeneratorContext(dataContext, file, psiClass);
        final List<TemplateEntry> entryList = new ArrayList<>();

        try {
          if(!psiClass.isInterface()) {
            List<String> importList = new ArrayList<>();
            List<PsiMethod> publicContructors = new ArrayList<>();
            List<PsiMethod> nonPublicContructors = new ArrayList<>();
            List<PsiMethod> publicMethodList = new ArrayList<>();
            List<PsiMethod> nonPublicMethodList = new ArrayList<>();
            List<String> fieldList = new ArrayList<>();

            buildTest(psiClass, publicContructors, nonPublicContructors, publicMethodList, nonPublicMethodList,
                fieldList);

            for(final PsiClass innerClas : psiClass.getAllInnerClasses()) {
              buildTest(innerClas, publicContructors, nonPublicContructors, publicMethodList, nonPublicMethodList,
                  fieldList);
            }

            List<MethodComposite> methodCompositeList = new ArrayList<>();
            List<MethodComposite> nonPublicMethodCompositeList = new ArrayList<>();
            List<MethodComposite> publicConstructorsCompositeList = new ArrayList<>();
            List<MethodComposite> nonPublicConstructorCompositeList = new ArrayList<>();

            processMethods(genCtx, publicMethodList, methodCompositeList);
            processMethods(genCtx, nonPublicMethodList, nonPublicMethodCompositeList);
            processMethods(genCtx, publicContructors, publicConstructorsCompositeList);
            processMethods(genCtx, nonPublicContructors, nonPublicConstructorCompositeList);

            String[] split = genCtx.getFile().getImportList().getText().split("\n");
            importList = Arrays.asList(split);
            entryList.add(new TemplateEntry(genCtx.getClassName(false), genCtx.getPackageName(), importList,
                publicConstructorsCompositeList, nonPublicConstructorCompositeList, methodCompositeList,
                nonPublicMethodCompositeList, fieldList));
            process(genCtx, entryList);
          }
        }
        catch(Exception e) {
          logger.error(e);
        }
      }
    }
  }

  public void buildTest(final PsiClass psiClass, final List<PsiMethod> publicContructors,
      final List<PsiMethod> nonPublicContructors, final List<PsiMethod> publicMethodList,
      final List<PsiMethod> nonPublicMethodList, final List<String> fieldList)
  {
    boolean getPublic = true;

    buildMethodList(psiClass.getMethods(), publicMethodList, getPublic);
    buildMethodList(psiClass.getMethods(), nonPublicMethodList, !getPublic);
    buildConstructorList(psiClass.getMethods(), publicContructors, getPublic);
    buildConstructorList(psiClass.getMethods(), nonPublicContructors, !getPublic);
    buildFieldList(psiClass.getFields(), fieldList);
  }

  /**
   * Creates a list of methods with set and get methods combined together.
   *
   * @param genCtx              the generator context
   * @param methodList          list of methods to process
   * @param methodCompositeList the composite list
   */
  private void processMethods(JUnitGeneratorContext genCtx, List<PsiMethod> methodList,
      List<MethodComposite> methodCompositeList)
  {
    List<String> methodNames = new ArrayList<>();
    List<MethodComposite> methodComposites = toComposites(genCtx, methodList);

    if(JUnitGeneratorUtil.getInstance(genCtx.getProject()).isGenerateForOverloadedMethods()) {
      methodComposites = updateOverloadedMethods(genCtx, methodComposites);
    }

    for(MethodComposite method : methodComposites) {
      String methodName = method.getName();

      if(JUnitGeneratorUtil.getInstance(genCtx.getProject()).isCombineGetterAndSetter()
          && ISGETSET.matcher(methodName).find()) {
        methodName = parseAccessorMutator(methodName, methodList);
      }

      if(!methodNames.contains(methodName)) {
        methodNames.add(methodName);
        method.setName(methodName);
        methodCompositeList.add(method);
      }
    }
  }

  /**
   * Create a MethodComposite object for each of the methods passed in
   *
   * @param genCtx     the context
   * @param methodList the method list
   * @return the list of methods
   */
  private List<MethodComposite> toComposites(JUnitGeneratorContext genCtx, List<PsiMethod> methodList)
  {
    final List<MethodComposite> compositeList = new ArrayList<>();

    for(final PsiMethod method : methodList) {
      MethodComposite methodComposite = toComposite(genCtx, method);
      compositeList.add(methodComposite);
      String textExceptions = method.getThrowsList().getText();
      if(!textExceptions.isEmpty()) {
        String[] exceptions = textExceptions.replace("throws ", "").split(",");
        for(int j = 0; j < exceptions.length; j++) {
          methodComposite = toComposite(genCtx, method);
          methodComposite.setExpectedException("(expected = " + exceptions[j] + ".class)");
          methodComposite.setName(methodComposite.getName() + "_throws_" + exceptions[j].trim());
          compositeList.add(methodComposite);
        }
      }
    }
    //now that we have the complete list, we want to see if any of the methods are overloaded with each other
    //this will find methods with the same 'name'
    for(final MethodComposite composite : compositeList) {
      composite.setOverloadedMethods(findOverloadedMethods(composite, compositeList));
    }
    return compositeList;
  }

  protected List<MethodComposite> findOverloadedMethods(final MethodComposite source, final List<MethodComposite> list)
  {
    final List<MethodComposite> overloadedMethods = new ArrayList<>();
    for(final MethodComposite method : list) {
      if(!source.equals(method) && source.getName().equals(method.getName())) {
        overloadedMethods.add(method);
      }
    }
    return overloadedMethods;
  }

  /**
   * Generate the method composite class. This method will recurse until we get to the top of the chain
   *
   * @param genCtx the generator context
   *               * @param method the method in question
   * @return the method composite object
   */
  private MethodComposite toComposite(final JUnitGeneratorContext genCtx, final PsiMethod method)
  {
    final List<String> paramClassList = new ArrayList<>();
    for(PsiParameter param : method.getParameterList().getParameters()) {
      paramClassList.add(param.getType().getCanonicalText());
    }

    final List<String> paramNameList = new ArrayList<>();
    for(PsiParameter param : method.getParameterList().getParameters()) {
      paramNameList.add(param.getName());
    }

    String commentSignature = createSignature(method);
    List<String> reflectionCode = createReflectionCode(genCtx, method);

    //create the composite object to send to the template
    final MethodComposite composite = new MethodComposite();
    composite.setSignature(commentSignature);
    composite.setMethod(method);
    composite.setName(method.getName());
    composite.setParamClasses(paramClassList);
    composite.setParamNames(paramNameList);
    composite.setCode(reflectionCode);

    //if the super method is not the same as us, grab the data from that also
    final PsiMethod[] superMethods = method.findSuperMethods();
    if(superMethods.length > 0) {
      composite.setBase(toComposite(genCtx, superMethods[0]));
    }
    return composite;
  }

  private String createSignature(final PsiMethod method)
  {
    final String signature;
    String params = "";
    for(final PsiParameter param : method.getParameterList().getParameters()) {
      params = appendParameter(params, param.getText());
    }
    final PsiTypeElement returnTypeElement = method.getReturnTypeElement();
    signature = (returnTypeElement != null ? returnTypeElement.getText() + " " : "") + method.getName() + "(" + params
        + ") " + method.getThrowsList().getText();
    return signature;
  }

  public Object getDefaultValue(final String className)
  {
    switch(className) {
      case "byte":
        return 0;
      case "short":
        return 0;
      case "int":
        return 0;
      case "long":
        return 0l;
      case "float":
        return 0f;
      case "double":
        return 0d;
      case "char":
        return "''";
      case "boolean":
        return "false";
      case "java.lang.String":
        return "\"\"";
      case "String":
        return "\"\"";
      case "StringBuffer":
        return "new StringBuffer()";
    }
    return null;
  }

  @NotNull
  private List<String> createReflectionCode(@NotNull JUnitGeneratorContext genCtx, @NotNull PsiMethod method)
  {
    final List<String> code = new ArrayList<>();
    String getMethodText = "\"" + method.getName() + "\"";
    String parametersConstructor = "";
    String parametersInstance = "";

    for(final PsiParameter param : method.getParameterList().getParameters()) {
      final String parameterClassName = param.getType().getPresentableText();

      getMethodText = getMethodText.concat(PARAMETER_SEPARATOR).concat(removeDiamonds(parameterClassName))
          .concat(CLASS);

      parametersConstructor = appendParameter(parametersConstructor, getParameterConstructor(parameterClassName));
      code.add(initParameter(param, parameterClassName));
      parametersInstance = appendParameter(parametersInstance, param.getName());
    }

    code.add("\n//When:");
    code.add("// Execute the system under test\n");

    final String className = genCtx.getClassName(false);
    final String constructorName = uncap(className);

    boolean isPublic = method.getModifierList().hasExplicitModifier("public");
    if(method.isConstructor()) {
      code.add("\t" + className + " " + constructorName + "Instance;");
      if(!isPublic) {
        code.add("\ttry {");
        code.add("\t\tfinal Constructor<" + className + "> " + constructorName + " = " + className
            + ".class.getDeclaredConstructor" + "(" + parametersConstructor + ");");
        code.add("\t\t" + constructorName + ".setAccessible(true);");
        code.add(
            "\t\t" + constructorName + "Instance = " + constructorName + ".newInstance(" + parametersInstance + ");");
        code.add("\t\t" + constructorName + ".setAccessible(false);");
        code.add("\t\tAssert.assertNotNull(" + constructorName + "Instance);");
        code.add("\t} catch(NoSuchMethodException |IllegalAccessException | InvocationTargetException e) {");
        code.add("\t}");
      }
      else {
        code.add("\t\t" + constructorName + "Instance = new " + className + "(" + parametersInstance + ")" + ";");
        code.add("\t\tAssert.assertNotNull(" + constructorName + "Instance);");
      }
    }
    else {
      String returnType = method.getReturnType().getPresentableText();
      boolean isVoid = "void".equals(returnType);
      if(!isVoid) {
        code.add("\t" + returnType + " returnObject;");
      }
      if(!isPublic) {
        code.add("\ttry {");
        code.add("\t\tMethod method = " + className + ".class.getClass().getMethod(" + getMethodText + ");");
        code.add("\t\tmethod.setAccessible(true);");
        code.add("\t\t//method.invoke( <Object>, <Parameters>)");
        code.add("\t\t" + (isVoid ? "" : "returnObject = (" + returnType + ")") + "method.invoke(new " + className
            + "() " + (parametersInstance.isEmpty() ? "" : PARAMETER_SEPARATOR + parametersInstance) + ");");
        code.add("\t\tmethod.setAccessible(false);");
        code.add("\t} catch(NoSuchMethodException |IllegalAccessException | InvocationTargetException e) {");
        code.add("\t}");
      }
      else {
        code.add("\t\t" + (isVoid ? "" : "returnObject = (" + returnType + ")") + "new " + className + "()."
            + method.getName() + "(" + parametersInstance + ");");
      }
    }
    return code;
  }

  @NotNull
  private String initParameter(@NotNull final PsiParameter param, @NotNull final String parameterClassName)
  {
    String init;
    if(Parameter.isPrimitiveType(parameterClassName) || "String".equals(parameterClassName)
        || "StringBuffer".equals(parameterClassName)) {
      init = getInitPrimitiveParameter(param, parameterClassName, getDefaultValue(parameterClassName));
    }
    else {
      init = getInitParameter(param, parameterClassName);
    }
    return init;
  }

  @NotNull
  private String appendParameter(@NotNull String parameters, @NotNull final String parameter)
  {
    if(!parameters.isEmpty()) {
      parameters += PARAMETER_SEPARATOR;
    }
    parameters = parameters + parameter;
    return parameters;
  }

  @NotNull
  private String getInitParameter(@NotNull final PsiParameter param, @NotNull final String parameterClassName)
  {
    return " " + parameterClassName + " " + param.getName() + "= Mockito.mock(" + removeDiamonds(parameterClassName)
        + ".class);";
  }

  @NotNull
  private String getInitPrimitiveParameter(@NotNull final PsiParameter param, @NotNull final String parameterClassName,
      final Object init)
  {
    return " " + parameterClassName + " " + param.getName() + "= " + init + ";";
  }

  @NotNull
  private String getParameterConstructor(final String parameterClassName)
  {
    return removeDiamonds(parameterClassName) + ".class";
  }

  @NotNull
  private String removeDiamonds(@NotNull final String parameterClassName)
  {
    String retValue = parameterClassName;
    int endIndex = parameterClassName.indexOf("<");
    if(endIndex > 0) {
      retValue = parameterClassName.substring(0, endIndex);
    }
    return retValue;
  }

  private String uncap(final String className)
  {
    return className.substring(0, 1).toLowerCase() + className.substring(1);
  }

  private List<MethodComposite> updateOverloadedMethods(JUnitGeneratorContext context, List<MethodComposite> methodList)
  {

    HashMap<String, Integer> methodNameMap = new HashMap<>();
    HashMap<String, Integer> overloadMethodNameMap = new HashMap<>();

    for(MethodComposite method : methodList) {
      String methodName = method.getName();
      if(!methodNameMap.containsKey(methodName)) {
        methodNameMap.put(methodName, 1);
      }
      else {
        Integer count = methodNameMap.get(methodName);
        methodNameMap.remove(methodName);
        methodNameMap.put(methodName, count + 1);
      }
    }

    for(String key : methodNameMap.keySet()) {
      if(methodNameMap.get(key) > 1) {
        overloadMethodNameMap.put(key, methodNameMap.get(key));
      }
    }

    for(int i = 0; i < methodList.size(); i++) {
      MethodComposite method = methodList.get(i);
      String methodName = method.getName();
      if(overloadMethodNameMap.containsKey(methodName)) {
        int count = overloadMethodNameMap.get(methodName);
        overloadMethodNameMap.remove(methodName);
        overloadMethodNameMap.put(methodName, count - 1);
        methodList.set(i, mutateOverloadedMethodName(context, method, count));
      }
    }

    return methodList;
  }

  private MethodComposite mutateOverloadedMethodName(JUnitGeneratorContext context, MethodComposite method, int count)
  {
    String stringToAppend = "";
    final String overloadType = JUnitGeneratorUtil.getInstance(context.getProject()).getListOverloadedMethodsBy();

    if(JUnitGeneratorUtil.NUMBER.equalsIgnoreCase(overloadType)) {
      stringToAppend += count;
    }
    else {
      if(JUnitGeneratorUtil.PARAM_CLASS.equalsIgnoreCase(overloadType)) {
        final List<String> parametersList = method.getParamClasses();
        stringToAppend = createSufixMethodName(method, stringToAppend, parametersList);
      }
      else if(JUnitGeneratorUtil.PARAM_NAME.equalsIgnoreCase(overloadType)) {
        List<String> parametersList = method.getParamNames();
        stringToAppend = createSufixMethodName(method, stringToAppend, parametersList);
      }
    }

    method.setName(method.getName() + stringToAppend);
    return method;
  }

  private String createSufixMethodName(final MethodComposite method, String stringToAppend,
      final List<String> parametersList)
  {
    boolean isEmptyParametersList = parametersList.isEmpty();
    if(!isEmptyParametersList) {
      stringToAppend += GIVEN;
    }
    for(String paramName : parametersList) {
      paramName = paramName.substring(0, 1).toUpperCase() + paramName.substring(1, paramName.length());
      stringToAppend += paramName + AND;
    }
    if(!isEmptyParametersList) {
      stringToAppend = stringToAppend.substring(0, stringToAppend.length() - 5);
    }
    return stringToAppend;
  }

  /**
   * This method takes in an accessor or mutator method that is named using get*, set*, or is* and combines
   * the method name to provide one method name: "GetSet<BaseName>"
   *
   * @param methodName - Name of accessor or mutator method
   * @param methodList - Entire list of method using to create test
   * @return String updated method name if list contains both accessor and modifier for base name
   */
  private String parseAccessorMutator(String methodName, List methodList)
  {

    String baseName;

    Matcher matcher = ISGETSET.matcher(methodName);
    if(matcher.find()) {
      baseName = matcher.group(2);
    }
    else {
      baseName = methodName;
    }
    //enumerate the method list to see if we have methods with set and is or get in them
    boolean setter = false;
    boolean getter = false;
    for(PsiMethod method : (List<PsiMethod>) methodList) {
      matcher = ISGETSET.matcher(method.getName());
      if(matcher.find() && baseName.equals(matcher.group(2))) {
        if(SET.equals(matcher.group(1))) {
          setter = true;
        }
        else if(IS.equals(matcher.group(1)) || GET.equals(matcher.group(1))) {
          getter = true;
        }
      }
    }
    //if we have a getter and setter, then fix the method to the same name
    if(getter && setter) {
      return "GetSet" + baseName;
    }

    return methodName;
  }

  /**
   * Builds a list of class scope fields from an array of PsiFields
   *
   * @param fields    an array of fields
   * @param fieldList list to be populated
   */
  private void buildFieldList(final PsiField[] fields, final List<String> fieldList)
  {
    for(final PsiField field : fields) {
      fieldList.add(field.getName());
    }
  }

  /**
   * Builds method List from an array of PsiMethods
   *
   * @param methods    array of methods
   * @param methodList list to be populated
   * @param getPublic boolean value, if true returns only private methods, if false only returns none private methods
   */
  private void buildMethodList(final PsiMethod[] methods, final List<PsiMethod> methodList, final boolean getPublic)
  {
    logger.warn("Start buildMethodList " + methods + "  " + methodList + "  " + getPublic);
    for(PsiMethod method : methods) {
      final PsiModifierList modifiers = method.getModifierList();
      if(!method.isConstructor() && isToAddMethod(getPublic, modifiers)) {
        methodList.add(method);
      }
    }
  }

  /** Check if method was to add
   *
   * @param getPublic
   * @param modifiers
   * @return
   */
  private boolean isToAddMethod(final boolean getPublic, final PsiModifierList modifiers)
  {
    if(modifiers.hasModifierProperty(PUBLIC) && getPublic) {
      return true;
    }
    if(!modifiers.hasModifierProperty(PUBLIC) && !getPublic) {
      return true;
    }
    return false;
  }

  private void buildConstructorList(final PsiMethod[] methods, final List<PsiMethod> methodList,
      final boolean getPublic)
  {
    for(final PsiMethod method : methods) {
      final PsiModifierList modifiers = method.getModifierList();
      if(method.isConstructor() && isToAddMethod(getPublic, modifiers)) {
        logger.warn("buildConstructorList modifiers  " + modifiers);
        methodList.add(method);
      }
    }
  }

  /**
   * Sets all the needed vars in VelocityContext and
   * merges the template
   *
   * @param genCtx    the context
   * @param entryList the list of entries to go into velocity scope
   */
  protected void process(final JUnitGeneratorContext genCtx, final List<TemplateEntry> entryList)
  {
    try {
      final Properties velocityProperties = new Properties();
      //use the 'string' resource loader because the template comes from a 'string'
      velocityProperties.setProperty(VelocityEngine.RESOURCE_LOADER, "string");
      velocityProperties.setProperty("string.resource.loader.class",
          "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
      velocityProperties.setProperty("string.resource.loader.repository.class",
          "org.apache.velocity.runtime.resource.loader.StringResourceRepositoryImpl");
      velocityProperties.setProperty("string.resource.loader.repository.static", "false");
      velocityProperties.setProperty("string.resource.loader.repository.name", "JUnitGenerator");

      //create the velocity engine with an externalized resource template
      final VelocityEngine ve = new VelocityEngine(velocityProperties);
      //set our custom log adapter
      ve.setProperty("runtime.log.logsystem", new LogAdapter());
      //manage the repository and put our template in with a name
      StringResourceRepository repository = new StringResourceRepositoryImpl();
      repository.putStringResource(VIRTUAL_TEMPLATE_NAME, getTemplate(genCtx.getProject()));
      ve.setApplicationAttribute("JUnitGenerator", repository);

      //init the engine
      ve.init();

      final VelocityContext context = new VelocityContext();
      context.put("entryList", entryList);
      context.put("today", JUnitGeneratorUtil.formatDate("MM/dd/yyyy"));
      context.put("date", new DateTool());

      final Template template = ve.getTemplate(VIRTUAL_TEMPLATE_NAME);
      final StringWriter writer = new StringWriter();

      template.merge(context, writer);
      String outputFileName = (String) context.get("testClass");
      if(outputFileName == null || outputFileName.trim().isEmpty()) {
        if(entryList != null && !entryList.isEmpty()) {
          outputFileName = entryList.get(0).getClassName() + "Test";
        }
        else {
          outputFileName = "UnknownTestCaseNameTest";
        }
      }
      ApplicationManager.getApplication().runWriteAction(new JUnitGeneratorFileCreator(
          JUnitGeneratorUtil.resolveOutputFileName(genCtx, outputFileName), writer, genCtx));
    }
    catch(Exception e) {
      logger.error(e);
    }
  }
}
