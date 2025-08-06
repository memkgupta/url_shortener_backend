package org.url_shortener_mp.analytics_service.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.url_shortener_mp.analytics_service.entities.Entry;

public interface EntryRepository extends MongoRepository<Entry,String> {
}
