package io.lematech.httprunner4j.test.config;

import io.lematech.httprunner4j.test.common.Constant;
import io.lematech.httprunner4j.test.common.DefinedException;
import io.lematech.httprunner4j.test.utils.JavaIdentifierUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className RunnerConfig
 * @description TODO
 * @created 2021/1/20 4:41 下午
 * @publicWechat lematech
 */

@Data
public class RunnerConfig {
    public List<String> getExecutePaths() {
        if(executePaths.isEmpty()){
            executePaths.add(Constant.DOT_PATH);
        }
        return executePaths;
    }
    private String pkgName;
    private List<String> executePaths;
    private String testCaseExtName;
    private static RunnerConfig instance = new RunnerConfig();

    public void setPkgName(String pkgName) {
        if(JavaIdentifierUtil.isValidJavaFullClassName(pkgName)){
            this.pkgName = pkgName;
        }else{
            String exceptionMsg = String.format("pkc name {} is invalid,not apply java identifier,please modify it",pkgName);
            throw new DefinedException(exceptionMsg);
        }

    }
    private RunnerConfig() {
        executePaths = new ArrayList<>();
        testCaseExtName = Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME;
    }
    public static RunnerConfig getInstance(){
        return instance;
    }

}
