package com.example.blps.security;

import com.example.blps.repositories.AuthTokenRepository;
import com.example.blps.services.AuthService;
import com.example.blps.util.XmlReader;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.jaas.AbstractJaasAuthenticationProvider;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.authentication.jaas.DefaultJaasAuthenticationProvider;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;

import javax.security.auth.login.AppConfigurationEntry;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class JaasConfig {
    private final XmlReader xml;

    @Bean
    public InMemoryConfiguration configuration(XmlReader xml){
        Map<String, XmlReader> options = new HashMap<>();
        options.put("xmlReader", xml);
        AppConfigurationEntry[] configurationEntries = new AppConfigurationEntry[]{
                new AppConfigurationEntry(JaasLoginModule.class.getCanonicalName(), AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options)
        };
        Map<String, AppConfigurationEntry[]> map = new HashMap<>();
        map.put("SPRINGSECURITY", configurationEntries);
        return new InMemoryConfiguration(map);
    }

    @Bean
    public AbstractJaasAuthenticationProvider jaasAuthenticationProvider(javax.security.auth.login.Configuration configuration) {
        DefaultJaasAuthenticationProvider provider = new DefaultJaasAuthenticationProvider();
        provider.setConfiguration(configuration);
        provider.setAuthorityGranters(new AuthorityGranter[]{new CustomAuthorityGranter(xml)});
        return provider;
    }
}
