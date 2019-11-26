package com.zyfra.mdmobjectsservice.repositories.mdm;

import com.zyfra.mdmclient.service.MDMAdapter;
import com.zyfra.mdmobjectsservice.common.Action;
import com.zyfra.mdmobjectsservice.common.MdmRequestHelper;
import com.zyfra.mdmobjectsservice.model.Object_;
import com.zyfra.mdmobjectsservice.repositories.ObjectsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Repository
public class MdmObjectRepository implements ObjectsRepository {

    private final MdmRequestHelper requestHelper;
    private final MDMAdapter mdmAdapter;


    public MdmObjectRepository(MdmRequestHelper requestHelper) {
        this.mdmAdapter = new MDMAdapter();
        this.requestHelper = requestHelper;
    }

    @Override
    public CompletionStage<Page<Object_>> getObjects(String id, Pageable pageable, Boolean onlyRoot, Timestamp ts) throws IOException {

        var configuration = requestHelper.getMdmConfiguration(Action.getObjects);
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
    }

    @Override
    public CompletionStage<Page<Object_>> getObjectTree(String id, String objId, Pageable pageable, Timestamp ts) throws IOException {

        var configuration = requestHelper.getMdmConfiguration(Action.getChildObjects);
        var parameters = new HashMap<String, Object>();
        parameters.put("id", objId);
        var classDescription = requestHelper.getMapping(Object_.class);

        var promise = new CompletableFuture<Page<Object_>>();

        //это объекты внутри дерева, атрибутом isPartOf у них заполняется parentId, а modelId зашиваем как "PNOS"
        CompletableFuture.supplyAsync(() -> mdmAdapter.<Object_>call(configuration, parameters, classDescription))
                .thenCompose(objects -> {
                    objects.forEach(object -> object.setModelId("PNOS"));
                    var setChildCountFutures = setChildCount(objects);
                    return CompletableFuture.allOf(setChildCountFutures)
                            .whenComplete((aVoid, throwable) -> promise.complete(new PageImpl<>(objects)));
                });
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
                    var object = nativeObject.get(0);
                    object.setModelId("PNOS");
                    object.setChildsCount(childCount);
                    return object;
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
