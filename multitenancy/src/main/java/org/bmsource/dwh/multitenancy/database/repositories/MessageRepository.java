package org.bmsource.dwh.multitenancy.database.repositories;

import org.bmsource.dwh.multitenancy.database.entities.Message;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MessageRepository extends PagingAndSortingRepository<Message,String> {

}
