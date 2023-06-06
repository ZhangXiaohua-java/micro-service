package io.element.feign.callback;

import io.element.feign.ProviderRemoteClient;
import io.element.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

public class ProviderRemoteClientFallbackFactory implements FallbackFactory<ProviderRemoteClient> {


    private static Logger logger = LoggerFactory.getLogger(ProviderRemoteClientFallbackFactory.class);

    @Override
    public ProviderRemoteClient create(Throwable cause) {
        return reject(cause);
    }


    public ProviderRemoteClient reject(Throwable throwable) {
        return new ProviderRemoteClient() {
            @Override
            public Result currentDate() {
                return callback(throwable);
            }
        };
    }


    public Result callback(Throwable throwable) {
        logger.error("feign的远程调用出错了,消息:{}", throwable.getMessage());
        return Result.error();
    }


}
