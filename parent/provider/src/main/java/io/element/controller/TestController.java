package io.element.controller;

import io.element.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

@RestController
@RequestMapping("/test")
public class TestController {

    private static Logger logger = LoggerFactory.getLogger(TestController.class);


    @GetMapping(value = "/date", produces = "application/json")
    public Result currentDate(@RequestHeader("Host") String host, HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            logger.info("请求头名为: {},值为: {}", headerName, request.getHeader(headerName));
        }
        String ip = request.getRemoteAddr();
        logger.info("接收到请求，请求头Host值为:{},请求地址:{}", host, ip);
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        return Result.success().put("date", dateStr);
    }


}
