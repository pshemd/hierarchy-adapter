package com.zyfra.mdmobjectsservice.repositories.mdm;

import com.zyfra.mdmclient.model.MDMConfiguration;
import com.zyfra.mdmclient.service.MDMAdapter;
import com.zyfra.mdmobjectsservice.common.Action;
import com.zyfra.mdmobjectsservice.common.MdmRequestHelper;
import com.zyfra.mdmobjectsservice.model.Object_;
import com.zyfra.mdmobjectsservice.repositories.ObjectsRepositoty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Repository
public class MdmObjectRepository implements ObjectsRepositoty {

    private final MdmRequestHelper requestHelper;
    private final MDMAdapter mdmAdapter;


    public MdmObjectRepository(MdmRequestHelper requestHelper) {
        this.mdmAdapter = new MDMAdapter();
        this.requestHelper = requestHelper;
    }

    @Override
    public CompletionStage<Page<Object_>> getObjects(String id, Pageable pageable, Boolean onlyRoot, Timestamp ts) {

        var configuration = requestHelper.getMdmConfiguration(Action.getObjects);

        var requestConfiguration = new MDMConfiguration().setUrl(configuration.getFirst()).setRequest(configuration.getSecond());
        var parameters = new HashMap<String, Object>();
        var classDescription = requestHelper.getMapping(Object_.class);

        var nativeObjects = mdmAdapter.<Object_>call(requestConfiguration, parameters, classDescription);
        var result = nativeObjects.stream().peek(object_ -> object_.setModelId("PNOS")).collect(Collectors.toList());
        return CompletableFuture.completedStage(new PageImpl<>(result));

    }

    @Override
    public CompletionStage<Page<Object_>> getObjectTree(String id, String objId, Pageable pageable, Timestamp ts) {

        var configuration = requestHelper.getMdmConfiguration(Action.getChildObjects);

        var requestConfiguration = new MDMConfiguration().setUrl(configuration.getFirst()).setRequest(configuration.getSecond());
        var parameters = new HashMap<String, Object>();
        parameters.put("id", objId);
        var classDescription = requestHelper.getMapping(Object_.class);

        var nativeObjects = mdmAdapter.<Object_>call(requestConfiguration, parameters, classDescription);
        var result = nativeObjects.stream().peek(object_ -> object_.setModelId("PNOS")).collect(Collectors.toList());
        return CompletableFuture.completedStage(new PageImpl<>(result));
    }

    @Override
    public CompletionStage<Object_> getObject(String id, Timestamp ts) {

        var configuration = requestHelper.getMdmConfiguration(Action.getObject);

        var requestConfiguration = new MDMConfiguration().setUrl(configuration.getFirst()).setRequest(configuration.getSecond());
        var parameters = new HashMap<String, Object>();
        parameters.put("id", id);
        var classDescription = requestHelper.getMapping(Object_.class);

        var result = mdmAdapter.<Object_>call(requestConfiguration, parameters, classDescription);
        Objects.requireNonNull(result.stream().findFirst().orElse(null)).setModelId("PNOS");
        return CompletableFuture.completedStage(result.stream().findFirst().orElse(null));

    }

    private boolean hasChild(String objId) {

        var configuration = requestHelper.getMdmConfiguration(Action.getChildObjects);

        var requestConfiguration = new MDMConfiguration().setUrl(configuration.getFirst()).setRequest(configuration.getSecond());
        var parameters = new HashMap<String, Object>();
        parameters.put("id", objId);
        var classDescription = requestHelper.getMapping(Object_.class);

        var childs = mdmAdapter.<Object_>call(requestConfiguration, parameters, classDescription);

        return childs != null && !childs.isEmpty();
    }

}
