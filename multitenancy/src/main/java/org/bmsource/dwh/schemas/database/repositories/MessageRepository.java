package org.bmsource.dwh.schemas.database.repositories;

import org.bmsource.dwh.schemas.database.entities.Message;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MessageRepository extends PagingAndSortingRepository<Message,String> {

}
