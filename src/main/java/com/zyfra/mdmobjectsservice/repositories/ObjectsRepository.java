package com.zyfra.mdmobjectsservice.repositories;

import com.zyfra.mdmobjectsservice.model.Object_;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.concurrent.CompletionStage;

public interface ObjectsRepository {

     CompletionStage<Page<Object_>> getObjects(String id, Pageable pageable, Boolean onlyRoot, Timestamp ts);
     CompletionStage<Page<Object_>> getObjectTree(String id, String objid, Pageable pageable,Timestamp ts);
     CompletionStage<Object_> getObject(String id, Timestamp ts);

}
