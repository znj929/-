package IO;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @Author: znj
 * @Date: 2021/2/27 0027 22:09
 */
public class Calculator {
    private final static ScriptEngine ise = new ScriptEngineManager().getEngineByName("JavaScript");

    public static Object cal(String expression) throws ScriptException {
        return ise.eval(expression);
    }

    public static void main(String[] args) {
        try{
            Object cal = Calculator.cal("200/0.95");
            System.out.println(cal);
        }catch (Exception e){

        }
    }

}
