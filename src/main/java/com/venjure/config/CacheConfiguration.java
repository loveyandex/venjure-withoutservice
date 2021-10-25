package com.venjure.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
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
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.venjure.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.venjure.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.venjure.domain.User.class.getName());
            createCache(cm, com.venjure.domain.Authority.class.getName());
            createCache(cm, com.venjure.domain.User.class.getName() + ".authorities");
            createCache(cm, com.venjure.domain.Address.class.getName());
            createCache(cm, com.venjure.domain.Administrator.class.getName());
            createCache(cm, com.venjure.domain.Administrator.class.getName() + ".historyEntries");
            createCache(cm, com.venjure.domain.Asset.class.getName());
            createCache(cm, com.venjure.domain.Channel.class.getName());
            createCache(cm, com.venjure.domain.Channel.class.getName() + ".paymentMethods");
            createCache(cm, com.venjure.domain.Channel.class.getName() + ".products");
            createCache(cm, com.venjure.domain.Channel.class.getName() + ".promotions");
            createCache(cm, com.venjure.domain.Channel.class.getName() + ".shippingMethods");
            createCache(cm, com.venjure.domain.Channel.class.getName() + ".customers");
            createCache(cm, com.venjure.domain.Channel.class.getName() + ".facets");
            createCache(cm, com.venjure.domain.Channel.class.getName() + ".facetValues");
            createCache(cm, com.venjure.domain.Channel.class.getName() + ".jorders");
            createCache(cm, com.venjure.domain.Channel.class.getName() + ".productVariants");
            createCache(cm, com.venjure.domain.Collection.class.getName());
            createCache(cm, com.venjure.domain.Collection.class.getName() + ".collectionTranslations");
            createCache(cm, com.venjure.domain.Collection.class.getName() + ".productvariants");
            createCache(cm, com.venjure.domain.CollectionAsset.class.getName());
            createCache(cm, com.venjure.domain.CollectionTranslation.class.getName());
            createCache(cm, com.venjure.domain.Country.class.getName());
            createCache(cm, com.venjure.domain.Country.class.getName() + ".countryTranslations");
            createCache(cm, com.venjure.domain.Country.class.getName() + ".zones");
            createCache(cm, com.venjure.domain.CountryTranslation.class.getName());
            createCache(cm, com.venjure.domain.Customer.class.getName());
            createCache(cm, com.venjure.domain.Customer.class.getName() + ".channels");
            createCache(cm, com.venjure.domain.Customer.class.getName() + ".customerGroups");
            createCache(cm, com.venjure.domain.Customer.class.getName() + ".addresses");
            createCache(cm, com.venjure.domain.Customer.class.getName() + ".historyEntries");
            createCache(cm, com.venjure.domain.Customer.class.getName() + ".jorders");
            createCache(cm, com.venjure.domain.CustomerGroup.class.getName());
            createCache(cm, com.venjure.domain.CustomerGroup.class.getName() + ".customers");
            createCache(cm, com.venjure.domain.ExampleEntity.class.getName());
            createCache(cm, com.venjure.domain.Facet.class.getName());
            createCache(cm, com.venjure.domain.Facet.class.getName() + ".channels");
            createCache(cm, com.venjure.domain.Facet.class.getName() + ".facetTranslations");
            createCache(cm, com.venjure.domain.Facet.class.getName() + ".facetValues");
            createCache(cm, com.venjure.domain.FacetTranslation.class.getName());
            createCache(cm, com.venjure.domain.FacetValue.class.getName());
            createCache(cm, com.venjure.domain.FacetValue.class.getName() + ".channels");
            createCache(cm, com.venjure.domain.FacetValue.class.getName() + ".products");
            createCache(cm, com.venjure.domain.FacetValue.class.getName() + ".facetValueTranslations");
            createCache(cm, com.venjure.domain.FacetValue.class.getName() + ".productVariants");
            createCache(cm, com.venjure.domain.FacetValueTranslation.class.getName());
            createCache(cm, com.venjure.domain.Fulfillment.class.getName());
            createCache(cm, com.venjure.domain.Fulfillment.class.getName() + ".orderItems");
            createCache(cm, com.venjure.domain.GlobalSettings.class.getName());
            createCache(cm, com.venjure.domain.HistoryEntry.class.getName());
            createCache(cm, com.venjure.domain.JobRecord.class.getName());
            createCache(cm, com.venjure.domain.Jorder.class.getName());
            createCache(cm, com.venjure.domain.Jorder.class.getName() + ".channels");
            createCache(cm, com.venjure.domain.Jorder.class.getName() + ".promotions");
            createCache(cm, com.venjure.domain.Jorder.class.getName() + ".historyEntries");
            createCache(cm, com.venjure.domain.Jorder.class.getName() + ".orderLines");
            createCache(cm, com.venjure.domain.Jorder.class.getName() + ".orderModifications");
            createCache(cm, com.venjure.domain.Jorder.class.getName() + ".payments");
            createCache(cm, com.venjure.domain.Jorder.class.getName() + ".shippingLines");
            createCache(cm, com.venjure.domain.Jorder.class.getName() + ".surcharges");
            createCache(cm, com.venjure.domain.OrderItem.class.getName());
            createCache(cm, com.venjure.domain.OrderItem.class.getName() + ".fulfillments");
            createCache(cm, com.venjure.domain.OrderItem.class.getName() + ".orderModifications");
            createCache(cm, com.venjure.domain.OrderItem.class.getName() + ".stockMovements");
            createCache(cm, com.venjure.domain.OrderLine.class.getName());
            createCache(cm, com.venjure.domain.OrderLine.class.getName() + ".orderItems");
            createCache(cm, com.venjure.domain.OrderLine.class.getName() + ".stockMovements");
            createCache(cm, com.venjure.domain.OrderModification.class.getName());
            createCache(cm, com.venjure.domain.OrderModification.class.getName() + ".surcharges");
            createCache(cm, com.venjure.domain.OrderModification.class.getName() + ".orderItems");
            createCache(cm, com.venjure.domain.Payment.class.getName());
            createCache(cm, com.venjure.domain.Payment.class.getName() + ".refunds");
            createCache(cm, com.venjure.domain.PaymentMethod.class.getName());
            createCache(cm, com.venjure.domain.PaymentMethod.class.getName() + ".channels");
            createCache(cm, com.venjure.domain.Product.class.getName());
            createCache(cm, com.venjure.domain.Product.class.getName() + ".productVariants");
            createCache(cm, com.venjure.domain.Product.class.getName() + ".channels");
            createCache(cm, com.venjure.domain.Product.class.getName() + ".facetValues");
            createCache(cm, com.venjure.domain.ProductAsset.class.getName());
            createCache(cm, com.venjure.domain.ProductOption.class.getName());
            createCache(cm, com.venjure.domain.ProductOption.class.getName() + ".productOptionTranslations");
            createCache(cm, com.venjure.domain.ProductOption.class.getName() + ".productVariants");
            createCache(cm, com.venjure.domain.ProductOptionGroup.class.getName());
            createCache(cm, com.venjure.domain.ProductOptionGroup.class.getName() + ".productOptions");
            createCache(cm, com.venjure.domain.ProductOptionGroup.class.getName() + ".productOptionGroupTranslations");
            createCache(cm, com.venjure.domain.Pogt.class.getName());
            createCache(cm, com.venjure.domain.ProductOptionTranslation.class.getName());
            createCache(cm, com.venjure.domain.ProductTranslation.class.getName());
            createCache(cm, com.venjure.domain.ProductVariant.class.getName());
            createCache(cm, com.venjure.domain.ProductVariant.class.getName() + ".channels");
            createCache(cm, com.venjure.domain.ProductVariant.class.getName() + ".productVariants");
            createCache(cm, com.venjure.domain.ProductVariant.class.getName() + ".facetValues");
            createCache(cm, com.venjure.domain.ProductVariant.class.getName() + ".productOptions");
            createCache(cm, com.venjure.domain.ProductVariant.class.getName() + ".productVariantAssets");
            createCache(cm, com.venjure.domain.ProductVariant.class.getName() + ".productVariantPrices");
            createCache(cm, com.venjure.domain.ProductVariant.class.getName() + ".productVariantTranslations");
            createCache(cm, com.venjure.domain.ProductVariant.class.getName() + ".stockMovements");
            createCache(cm, com.venjure.domain.ProductVariantAsset.class.getName());
            createCache(cm, com.venjure.domain.ProductVariantPrice.class.getName());
            createCache(cm, com.venjure.domain.ProductVariantTranslation.class.getName());
            createCache(cm, com.venjure.domain.Promotion.class.getName());
            createCache(cm, com.venjure.domain.Promotion.class.getName() + ".jorders");
            createCache(cm, com.venjure.domain.Promotion.class.getName() + ".channels");
            createCache(cm, com.venjure.domain.Refund.class.getName());
            createCache(cm, com.venjure.domain.Refund.class.getName() + ".orderItems");
            createCache(cm, com.venjure.domain.ShippingLine.class.getName());
            createCache(cm, com.venjure.domain.ShippingMethod.class.getName());
            createCache(cm, com.venjure.domain.ShippingMethod.class.getName() + ".shippingMethodTranslations");
            createCache(cm, com.venjure.domain.ShippingMethod.class.getName() + ".channels");
            createCache(cm, com.venjure.domain.ShippingMethodTranslation.class.getName());
            createCache(cm, com.venjure.domain.StockMovement.class.getName());
            createCache(cm, com.venjure.domain.Surcharge.class.getName());
            createCache(cm, com.venjure.domain.Tag.class.getName());
            createCache(cm, com.venjure.domain.TaxCategory.class.getName());
            createCache(cm, com.venjure.domain.TaxCategory.class.getName() + ".taxRates");
            createCache(cm, com.venjure.domain.TaxRate.class.getName());
            createCache(cm, com.venjure.domain.Zone.class.getName());
            createCache(cm, com.venjure.domain.Zone.class.getName() + ".countries");
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
