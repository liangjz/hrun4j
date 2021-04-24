package io.lematech.httprunner4j.core.engine;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.core.loader.HotLoader;
import io.lematech.httprunner4j.core.validator.SchemaValidator;
import io.lematech.httprunner4j.widget.utils.FilesUtil;
import io.lematech.httprunner4j.widget.log.MyLog;
import lombok.Data;
import org.apache.velocity.VelocityContext;
import org.testng.ITestListener;
import org.testng.TestNG;
import org.testng.reporters.JUnitXMLReporter;
import org.uncommons.reportng.HTMLReporter;

import java.io.File;
import java.util.*;


/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TestCaseExecutorEngine
 * @description testnge engine
 * @created 2021/4/6 11:30 下午
 * @publicWechat lematech
 */

@Data
public class TestNGEngine {
    private static TestNG testNG;
    private static String suiteName;
    private static SchemaValidator schemaValidator = new SchemaValidator();
    public static Map<String, Set<String>> testCasePkgGroup = new HashMap<>();

    public static TestNG getInstance() {
        if (testNG == null) {
            testNG = new TestNG();
            setDefaultProperties();
        }
        return testNG;
    }

    private static void setDefaultProperties(){
        testNG.setDefaultSuiteName("httprunner4j");
        HTMLReporter htmlReporter = new HTMLReporter();
        JUnitXMLReporter jUnitXMLReporter = new JUnitXMLReporter();
        testNG.addListener(htmlReporter);
        testNG.addListener(jUnitXMLReporter);
    }

    /**
     * self-defined add listener
     * @param testListenerList
     * @return
     */
    public static TestNG addListener(List<ITestListener> testListenerList) {
        getInstance();
        if (testListenerList.size() > 0) {
            for (ITestListener testListener : testListenerList) {
                testNG.addListener(testListener);
            }
        }
        return testNG;
    }

    /**
     * init testng classes and testng  run
     */
    public static void run() {
        List<File> testCasePaths = RunnerConfig.getInstance().getTestCasePaths();
        testCasePkgGroup = FilesUtil.fileList2TestClass(testCasePaths);
        if (MapUtil.isEmpty(testCasePkgGroup)) {
            MyLog.warn("in path [{}] not found valid testcases", testCasePaths);
        }
        addTestClasses();
        runNG();
    }

    private static void runNG() {
        getInstance().run();
    }

    private static void addTestClasses(){
        List<Class> classes = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : testCasePkgGroup.entrySet()) {
            String fullTestClassName = entry.getKey();
            Set methodNameList = entry.getValue();
            String pkgName = StrUtil.subBefore(fullTestClassName, ".", true);
            String className = StrUtil.upperFirst(StrUtil.subAfter(fullTestClassName, ".", true));
            VelocityContext ctx = new VelocityContext();
            ctx.put("pkgName", pkgName);
            ctx.put("className", className);
            ctx.put("methodList", methodNameList);
            String templateRenderContent = TemplateEngine.getTemplateRenderContent(Constant.TEST_TEMPLATE_FILE_PATH, ctx);
            MyLog.debug("test case content:{}", templateRenderContent);
            Class<?> clazz = HotLoader.hotLoadClass(pkgName,className,templateRenderContent);
            classes.add(clazz);
            MyLog.debug("class full path：[{}],pkg path：[{}],class name：{} added done.", fullTestClassName, pkgName, className);
        }
        Class [] execClass = classes.toArray(new Class[0]);
        getInstance().setTestClasses(execClass);
    }

}
