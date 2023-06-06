package io.element.conifg;

import io.element.feign.callback.ProviderRemoteClientFallbackFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenFeignConfiguration {


    @Bean
    public ProviderRemoteClientFallbackFactory providerRemoteClientFallbackFactory() {
        return new ProviderRemoteClientFallbackFactory();
    }


}
