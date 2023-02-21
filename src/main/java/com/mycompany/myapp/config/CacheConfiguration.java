package com.mycompany.myapp.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, Constants.CAPTCHA_KEY);
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.mycompany.myapp.domain.User.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Authority.class.getName());
            createCache(cm, com.mycompany.myapp.domain.User.class.getName() + ".authorities");
            createCache(cm, com.mycompany.myapp.domain.Authority.class.getName() + ".viewPermissions");
            createCache(cm, com.mycompany.myapp.domain.Authority.class.getName() + ".children");
            createCache(cm, com.mycompany.myapp.domain.Authority.class.getName() + ".users");
            createCache(cm, com.mycompany.myapp.domain.Authority.class.getName() + ".apiPermissions");
            createCache(cm, com.mycompany.myapp.domain.DepartmentAuthority.class.getName());
            createCache(cm, com.mycompany.myapp.domain.DepartmentAuthority.class.getName() + ".users");
            createCache(cm, com.mycompany.myapp.domain.DepartmentAuthority.class.getName() + ".apiPermissions");
            createCache(cm, com.mycompany.myapp.domain.DepartmentAuthority.class.getName() + ".viewPermissions");
            createCache(cm, com.mycompany.myapp.system.domain.SmsMessage.class.getName());
            createCache(cm, com.mycompany.myapp.domain.OssConfig.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ApiPermission.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ApiPermission.class.getName() + ".children");
            createCache(cm, com.mycompany.myapp.domain.ApiPermission.class.getName() + ".authorities");
            createCache(cm, com.mycompany.myapp.domain.UploadImage.class.getName());
            createCache(cm, com.mycompany.myapp.domain.SmsConfig.class.getName());
            createCache(cm, com.mycompany.myapp.domain.UReportFile.class.getName());
            createCache(cm, com.mycompany.myapp.system.domain.AnnouncementRecord.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Department.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Department.class.getName() + ".children");
            createCache(cm, com.mycompany.myapp.domain.Department.class.getName() + ".authorities");
            createCache(cm, com.mycompany.myapp.domain.Department.class.getName() + ".users");
            createCache(cm, com.mycompany.myapp.domain.Department.class.getName() + ".departmentAuthorities");
            createCache(cm, com.mycompany.myapp.system.domain.SmsTemplate.class.getName());
            createCache(cm, com.mycompany.myapp.system.domain.SiteConfig.class.getName());
            createCache(cm, com.mycompany.myapp.system.domain.Announcement.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Authority.class.getName() + ".departments");
            createCache(cm, com.mycompany.myapp.domain.BusinessType.class.getName());
            createCache(cm, com.mycompany.myapp.system.domain.SysLog.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ViewPermission.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ViewPermission.class.getName() + ".children");
            createCache(cm, com.mycompany.myapp.domain.ViewPermission.class.getName() + ".authorities");
            createCache(cm, com.mycompany.myapp.system.domain.DataPermissionRule.class.getName());
            createCache(cm, com.mycompany.myapp.domain.SysFillRule.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ResourceCategory.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ResourceCategory.class.getName() + ".files");
            createCache(cm, com.mycompany.myapp.domain.ResourceCategory.class.getName() + ".children");
            createCache(cm, com.mycompany.myapp.domain.ResourceCategory.class.getName() + ".images");
            createCache(cm, com.mycompany.myapp.domain.UploadFile.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Position.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Position.class.getName() + ".users");
            createCache(cm, com.mycompany.myapp.domain.DataDictionary.class.getName());
            createCache(cm, com.mycompany.myapp.domain.DataDictionary.class.getName() + ".children");
            createCache(cm, com.mycompany.myapp.domain.RegionCode.class.getName());
            createCache(cm, com.mycompany.myapp.domain.RegionCode.class.getName() + ".children");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
