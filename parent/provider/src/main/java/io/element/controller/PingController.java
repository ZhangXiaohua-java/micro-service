/*
 * Copyright(c) 张晓华 $date
 * ******张晓华拥有本软件的版权2023并保留所有权力*******
 * 温馨提示：
 * 适度编码益脑，沉迷编码伤身，合理安排时间，享受快乐生活。
 */

package io.element.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张晓华
 * @date 2023-06-06 23:04
 * @usage 当前类的用途描述
 */
@RestController
public class PingController {


    @GetMapping("/ping")
    public String ping() {
        return "ok";
    }


}
