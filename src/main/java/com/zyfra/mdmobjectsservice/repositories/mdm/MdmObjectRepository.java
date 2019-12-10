package com.zyfra.mdmobjectsservice.repositories.mdm;

import com.zyfra.mdmclient.service.MDMAdapter;
import com.zyfra.mdmobjectsservice.common.Action;
import com.zyfra.mdmobjectsservice.common.MdmRequestHelper;
import com.zyfra.mdmobjectsservice.model.Object_;
import com.zyfra.mdmobjectsservice.repositories.ObjectsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Repository
public class MdmObjectRepository implements ObjectsRepository {

    private final MdmRequestHelper requestHelper;
    private final MDMAdapter mdmAdapter;


    public MdmObjectRepository(MdmRequestHelper requestHelper) {
        this.mdmAdapter = new MDMAdapter();
        this.requestHelper = requestHelper;
    }

    @Override
    public CompletionStage<Page<Object_>> getObjects(String id, Pageable pageable, Boolean onlyRoot, Timestamp ts) {

        if (onlyRoot) {
            var configuration = requestHelper.getMdmConfiguration(Action.getRootObjects);
            var parameters = new HashMap<String, Object>();
            var classDescription = requestHelper.getMapping(Object_.class);

            var promise = new CompletableFuture<Page<Object_>>();

            //это корневые объекты модели, атрибутом isPartOf у них заполняется modelId, а parentId обнуляем
            CompletableFuture.supplyAsync(() -> mdmAdapter.<Object_>call(configuration, parameters, classDescription))
                    .thenCompose(objects -> {
                        objects.forEach(object -> object.setParentId(null));
                        var setChildCountFutures = setChildCount(objects);
                        return CompletableFuture.allOf(setChildCountFutures)
                                .whenComplete((aVoid, throwable) -> promise.complete(new PageImpl<>(objects)));

                    });
            return promise;
        } else {
            var configuration = requestHelper.getMdmConfiguration(Action.getAllObjects);
            var parameters = new HashMap<String, Object>();
            var classDescription = requestHelper.getMapping(Object_.class);

            //у объектов с parentId=modelId='PNOS' обнуляем parentId т.к. это корневые объекты
            return CompletableFuture.supplyAsync(() -> mdmAdapter.<Object_>call(configuration, parameters, classDescription))
                    .thenApply(objects -> {
                        objects.stream()
                                .filter(object -> object.getModelId().equals(object.getParentId()))
                                .forEach(object -> object.setParentId(null));
                        return objects;
                    })
                    .thenApply(PageImpl::new);
        }
    }

    @Override
    public CompletionStage<Page<Object_>> getObjectTree(String id, String objId, Boolean isNeedChildCount, Pageable pageable, Timestamp ts) {

        var configuration = requestHelper.getMdmConfiguration(Action.getChildObjects);
        var parameters = new HashMap<String, Object>();
        parameters.put("id", objId);
        var classDescription = requestHelper.getMapping(Object_.class);

        var promise = new CompletableFuture<Page<Object_>>();

        var getObjectsFuture = CompletableFuture.supplyAsync(() -> mdmAdapter.<Object_>call(configuration, parameters, classDescription));

        if (isNeedChildCount) {
            getObjectsFuture
                    .thenCompose(objects -> {
                        objects.forEach(object -> object.setModelId("PNOS"));
                        var setChildCountFutures = setChildCount(objects);
                        return CompletableFuture.allOf(setChildCountFutures)
                                .whenComplete((aVoid, throwable) -> promise.complete(new PageImpl<>(objects)));
                    });
        } else {
            getObjectsFuture
                    .thenApply(objects -> objects.stream().peek(object -> object.setModelId("PNOS")).collect(Collectors.toList()))
                    .whenComplete((objects, throwable) -> promise.complete(new PageImpl<>(objects)));
        }


        return promise;
    }

    @Override
    public CompletionStage<Object_> getObject(String id, Timestamp ts) throws IOException {

        var configuration = requestHelper.getMdmConfiguration(Action.getObject);
        var parameters = new HashMap<String, Object>();
        parameters.put("id", id);
        var classDescription = requestHelper.getMapping(Object_.class);

        var childCountFuture = getChildCount(id);

        return CompletableFuture.supplyAsync(() -> mdmAdapter.<Object_>call(configuration, parameters, classDescription))
                .thenCombine(childCountFuture, (nativeObject, childCount) -> {
                    if (nativeObject.size() > 0) {
                        var object = nativeObject.get(0);
                        object.setModelId("PNOS");
                        object.setChildsCount(childCount);
                        return object;
                    } else
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Object %s not found", id));
                });
    }

    //поиск дочерних объектов, для заполнения поля childCount
    private CompletionStage<Integer> getChildCount(String objId) throws IOException {

        var configuration = requestHelper.getMdmConfiguration(Action.getChildObjects);
        var parameters = new HashMap<String, Object>();
        parameters.put("id", objId);
        var classDescription = requestHelper.getMapping(Object_.class);

        return CompletableFuture.supplyAsync(() -> mdmAdapter.<Object_>call(configuration, parameters, classDescription))
                .thenApply(List::size);
    }

    private CompletableFuture[] setChildCount(List<Object_> objects) {

        return objects.stream().map(object -> {
            try {
                return getChildCount(object.getId())
                        .thenAccept(object::setChildsCount);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }).toArray(CompletableFuture[]::new);
    }

}
