package io.element.feign;

import io.element.feign.callback.ProviderRemoteClientFallbackFactory;
import io.element.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(fallbackFactory = ProviderRemoteClientFallbackFactory.class, value = "provider",
        path = "/test")
public interface ProviderRemoteClient {


    @GetMapping(value = "/date", produces = {"application/json"})
    Result currentDate();


}
