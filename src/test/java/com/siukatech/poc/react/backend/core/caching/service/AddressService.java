package com.siukatech.poc.react.backend.core.caching.service;

import com.siukatech.poc.react.backend.core.caching.model.AddressModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class AddressService {

    private Map<String, AddressModel> addressModelMap = new HashMap<>();

    public void saveAddressModel(@NonNull AddressModel addressModel) {
        if (addressModel.getId() == null) addressModel.setId(UUID.randomUUID().toString());
        this.addressModelMap.put(addressModel.getId(), addressModel);
    }

    public void printAddressModelMap() {
        log.debug("printAddressModelMap - addressModelMap: [{}]", this.addressModelMap);
    }

    @Cacheable(value = {"default"}, key = "#addressId")
    public AddressModel getAddressModelById(String addressId) {
        log.debug("getAddressModelById - addressId: [{}]", addressId);
        AddressModel addressModel = this.addressModelMap.get(addressId);
        return addressModel;
    }

    @CacheEvict(value = {"default"}, allEntries = true)
    public void evictAllCacheValues() {}

}
