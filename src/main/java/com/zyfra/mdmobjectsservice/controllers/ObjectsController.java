package com.zyfra.mdmobjectsservice.controllers;

import com.zyfra.mdmobjectsservice.controllers.api.ObjectsApi;
import com.zyfra.mdmobjectsservice.model.Model;
import com.zyfra.mdmobjectsservice.model.Object_;
import com.zyfra.mdmobjectsservice.repositories.ModelRepository;
import com.zyfra.mdmobjectsservice.repositories.ObjectsRepositoty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.concurrent.CompletionStage;

@Controller
@ResponseBody
@CrossOrigin(origins = "*")
public class ObjectsController implements ObjectsApi {

    private ModelRepository modelRepository;
    private ObjectsRepositoty objectsRepositoty;

    @Autowired
    public ObjectsController(ModelRepository modelRepository, ObjectsRepositoty objectsRepositoty) {
        this.modelRepository = modelRepository;
        this.objectsRepositoty = objectsRepositoty;
    }

    @Override
    public CompletionStage<Page<Model>> getModels(@Valid Pageable pageable, @Valid Timestamp ts) {

        return modelRepository.getModels(pageable, ts)
                .exceptionally(throwable -> {
                    throw new RuntimeException(throwable.getMessage());
                });
    }

    @Override
    public CompletionStage<Page<Object_>> getObjects(@Valid String id, @Valid Pageable pageable, Boolean onlyRoot, @Valid Timestamp ts) {

        return objectsRepositoty.getObjects(id, pageable, false, ts)
                .exceptionally(throwable -> {
                    throw new RuntimeException(throwable.getMessage());
                });
    }

    @Override
    public CompletionStage<Page<Object_>> getObjectTree(@Valid String id, String objid, @Valid Pageable pageable, @Valid Timestamp ts) {

        return objectsRepositoty.getObjectTree(id, objid, pageable, ts)
                .exceptionally(throwable -> {
                    throw new RuntimeException(throwable.getMessage());
                });
    }

    @Override
    public CompletionStage<Object_> getObject(String id, Timestamp ts) {
        return objectsRepositoty.getObject(id, ts)
                .exceptionally(throwable -> {
                    throw new RuntimeException(throwable.getMessage());
                });
    }
}
