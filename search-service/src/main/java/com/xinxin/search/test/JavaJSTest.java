package com.xinxin.search.test;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.shell.Main;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * https://github.com/LoseNine/Crack-JS-Spider
 * https://mp.weixin.qq.com/s/Nxh5uR7Dr0ajO_6CdFVBAQ
 *
 * 未实现，具体报错见方法内
 */
public class JavaJSTest {

    /**
     * JDK的原生实现
     * java不支持浏览器本身的方法，只支持自定义的js方法，否则会报错
     * @throws Exception
     */
    public static void test1() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        String jsFileName = "E:\\Code\\LearnCode\\search\\search-service\\src\\main\\resources\\js\\zp_token.js";  //js文件所在目录
        FileReader reader = new FileReader(jsFileName);   // 执行指定脚本
        engine.eval(reader);
        if (engine instanceof Invocable) {
            Invocable invoke = (Invocable) engine;    // 调用merge方法，并传入两个参数
//            Double c = (Double) invoke.invokeFunction("add", 1, 2); //调用了js的add方法
//            System.out.println(c);
            invoke.invokeFunction("getT"); //调用了js的getT方法
            /**
             * 报错：
             * Exception in thread "main" javax.script.ScriptException: ReferenceError: "window" is not defined in <eval> at line number 9
             */

        }
    }

    /**
     * 使用Rhino调用js文件中的函数
     *
     * failure
     */
    public static void testRhino() throws Exception {
        Context ct = Context.enter();

//        ct.setOptimizationLevel(-1);
//        ct.setLanguageVersion(Context.VERSION_1_7);

        // js source为String的情况
        /*Scriptable scope = ct.initStandardObjects();
        ct.evaluateString(scope,
                "function test(name){return 'Successful!' + name;}", null, 1, null);
        Object functionObject = scope.get("test", scope);
        if (!(functionObject instanceof Function)) {
            System.out.println("test is undefined or not a function.");
        } else {
            Object testArgs[] = {"Ceven"};
            Function test = (Function) functionObject;
            Object result = test.call(ct, scope, scope, testArgs);
            System.out.println(Context.toString(result));
        }*/

        Scriptable scope = ct.initStandardObjects();
//        Main.processFile(ct, scope, "");

        // TODO js source为JS文件的情况
        String jsFileName = "E:\\Code\\LearnCode\\search\\search-service\\src\\main\\resources\\js\\zp_token.js";  //js文件所在目录
        FileReader reader = new FileReader(jsFileName);
        // org.mozilla.javascript.EcmaError: ReferenceError: "window" is not defined. (unnamed script#9)
        ct.evaluateReader(scope, reader, null, 1, null);
        Object functionObject2 = scope.get("getT", scope);
        if (!(functionObject2 instanceof Function)) {
            System.out.println("getT is undefined or not a function.");
        } else {
            System.out.println("getT is fine.");
        }

    }

    public static void main(String[] args) throws Exception {
//        test1();
        testRhino();
    }
}
