package com.zyfra.mdmobjectsservice.controllers;

import com.zyfra.mdmobjectsservice.controllers.api.ObjectsApi;
import com.zyfra.mdmobjectsservice.model.Model;
import com.zyfra.mdmobjectsservice.model.Object_;
import com.zyfra.mdmobjectsservice.repositories.ModelRepository;
import com.zyfra.mdmobjectsservice.repositories.ObjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.concurrent.CompletionStage;

@Controller
@ResponseBody
@CrossOrigin(origins = "*")
public class ObjectsController implements ObjectsApi {

    private ModelRepository modelRepository;
    private ObjectsRepository objectsRepository;

    @Autowired
    public ObjectsController(ModelRepository modelRepository, ObjectsRepository objectsRepository) {
        this.modelRepository = modelRepository;
        this.objectsRepository = objectsRepository;
    }

    @Override
    public CompletionStage<Page<Model>> getModels(@Valid Pageable pageable, @Valid Timestamp ts) throws IOException {

        return modelRepository.getModels(pageable, ts)
                .exceptionally(throwable -> {
                    throw new RuntimeException(throwable.getMessage(), throwable);
                });
    }

    @Override
    public CompletionStage<Page<Object_>> getObjects(@Valid String id, @Valid Pageable pageable, Boolean onlyRoot, @Valid Timestamp ts) throws IOException {

        return objectsRepository.getObjects(id, pageable, false, ts)
                .exceptionally(throwable -> {
                    throw new RuntimeException(throwable.getMessage(), throwable);
                });
    }

    @Override
    public CompletionStage<Page<Object_>> getObjectTree(@Valid String id, String objid, Boolean isNeedChildCount, @Valid Pageable pageable, @Valid Timestamp ts) throws IOException {

        return objectsRepository.getObjectTree(id, objid, isNeedChildCount, pageable, ts)
                .exceptionally(throwable -> {
                    throw new RuntimeException(throwable.getMessage(), throwable);
                });
    }

    @Override
    public CompletionStage<Object_> getObject(String id, Timestamp ts) throws IOException {
        return objectsRepository.getObject(id, ts)
                .exceptionally(throwable -> {
                    throw new RuntimeException(throwable.getMessage(), throwable);
                });
    }
}
