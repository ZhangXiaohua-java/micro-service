package io.element;


import io.element.vo.Result;
import org.junit.Test;

import java.util.Collections;

public class AppTest {


    @Test
    public void testVo() {
        Result result = Result.success();
        result.put("key", Collections.singletonList("abc"));
        System.out.println(result);
    }


}
