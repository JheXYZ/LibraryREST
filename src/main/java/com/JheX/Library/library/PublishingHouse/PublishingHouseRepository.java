package com.JheX.Library.library.PublishingHouse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface PublishingHouseRepository extends CrudRepository<PublishingHouse, Long>, JpaRepository<PublishingHouse, Long> {
    PublishingHouse findByName(String name);
}
