package edu.spr19cs210.drill09;

import java.util.ArrayList;
import java.util.List;

public class Drill09 {

    /*
     * This function should return a lambda function that
     * takes an integer as input and returns that integer plus 2.
     */
    public static Int2IntFunction returnAddTwoLambda() {
        Int2IntFunction func = (int x) -> x + 2;
        return func;
    }

    /*
     * This function should return a lambda function that takes a
     * String as input and returns that String concatenated to the
     * "hello" string.
     */
    public static Str2StrFunction returnConcatToHelloLambda() {
        Str2StrFunction func = (String s) -> "hello" + s;
        return func;
    }

    /*
     * This function should return a lambda function that takes an
     * integer as input and returns that integer averaged with 3.
     */
    public static Int2IntFunction returnAvgWithThreeLambda() {
        Int2IntFunction func = (int x) -> (x + 3) / 2;
        return func;
    }

    /*
     * This function should return a lambda function that takes a List as
     * input and returns a List with the numbers 1 and 2 and then the given
     * list concatenated afterwards.
     */
    public static List2ListFunction returnConcatWith1n2ListLambda() {
        List2ListFunction func = (List<Integer> list) -> {
            List<Integer> list2 = new ArrayList<Integer>();
            list2.add(1);
            list2.add(2);
            list2.addAll(list);
            return list2;
        };
        return func;
    }

}
