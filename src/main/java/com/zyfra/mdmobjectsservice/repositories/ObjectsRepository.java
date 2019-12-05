package com.zyfra.mdmobjectsservice.repositories;

import com.zyfra.mdmobjectsservice.model.Object_;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.concurrent.CompletionStage;

public interface ObjectsRepository {

     CompletionStage<Page<Object_>> getObjects(String id, Pageable pageable, Boolean onlyRoot, Timestamp ts) throws IOException;
     CompletionStage<Page<Object_>> getObjectTree(String id, String objid, Boolean isNeedChildCount, Pageable pageable,Timestamp ts) throws IOException;
     CompletionStage<Object_> getObject(String id, Timestamp ts) throws IOException;

}
