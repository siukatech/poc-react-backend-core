package com.siukatech.poc.react.backend.core.caching;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.siukatech.poc.react.backend.core.AbstractUnitTests;
import com.siukatech.poc.react.backend.core.caching.model.AddressModel;
import com.siukatech.poc.react.backend.core.caching.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public abstract class AbstractCachingManagerTests extends AbstractUnitTests {

    @Autowired
    protected CacheManager cacheManager;

    @Autowired
    protected AddressService addressService;

    protected void setup_cacheManager() {
        this.addressService.evictAllCacheValues();
    }

    protected void test_xxxCacheManager_basic(String cacheManagerName) {
        log.debug("test_xxxCacheManager_basic - getCacheNames: [{}]"
                , this.cacheManager.getCacheNames());
        log.debug("test_xxxCacheManager_basic - cacheManager: [{}]"
                , this.cacheManager);

        List<ILoggingEvent> loggingEventList = this.getMemoryAppender().search(cacheManagerName, Level.DEBUG);
        Assertions.assertTrue((!loggingEventList.isEmpty()), "%s not found".formatted(cacheManagerName));
    }

    protected void test_getAddressModelById_basic() {
        this.test_getAddressModelById(false, 1000L);
    }

    protected void test_getAddressModelById_ttl_exceeded_1s(long definedTtl) {
        this.test_getAddressModelById(true, definedTtl);
    }

//    @Test
    private void test_getAddressModelById(boolean isTtlExceeded, long definedTtl) {
        // given
        String addressId = "address-01";
        AddressModel addressModel01 = new AddressModel(
                addressId
                , "location-01"
                , "street-01"
                , "district-01"
        );
        this.addressService.saveAddressModel(addressModel01);

        // when
        AddressModel addressCache01 = this.addressService.getAddressModelById(addressId);
        log.debug("test_getAddressModelById - addressCache01: [{}]", addressCache01);
        //
        Cache cacheDefault = this.cacheManager.getCache("default");
        Object object01 = cacheDefault == null ? null : cacheDefault.get(addressId);
        log.debug("test_getAddressModelById - object01: [{}]", object01);
        //
        AddressModel addressModel02 = new AddressModel(
                addressId
                , "location-02"
                , "street-02"
                , "district-02"
        );
        this.addressService.saveAddressModel(addressModel02);
        AddressModel addressCache02 = this.addressService.getAddressModelById(addressId);
        log.debug("test_getAddressModelById - addressCache02: [{}]", addressCache02);
        //
        this.addressService.printAddressModelMap();
        //
        if (!isTtlExceeded) {
            this.addressService.evictAllCacheValues();
        }
        else {
            try {
                Thread.sleep(definedTtl + 500);
            }
            catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
        //
        AddressModel addressCache02b = this.addressService.getAddressModelById(addressId);
        log.debug("test_getAddressModelById - addressCache02b: [{}]", addressCache02b);
        //
        this.addressService.printAddressModelMap();

        // then
        // assertThat(actual).isEqualTo(expected)
        assertThat(addressCache01.toString()).isEqualTo(addressCache02.toString());
        assertThat(addressCache02.toString()).isEqualTo(addressModel01.toString());
//        assertThat(addressCache02b.toString()).isEqualTo(addressCache01.toString());
        assertThat(addressCache02b.toString()).isEqualTo(addressModel02.toString());
    }

}
