package com.zyfra.mdmobjectsservice.repositories.mdm;

import com.zyfra.mdmclient.service.MDMAdapter;
import com.zyfra.mdmobjectsservice.common.Action;
import com.zyfra.mdmobjectsservice.common.MdmRequestHelper;
import com.zyfra.mdmobjectsservice.model.Model;
import com.zyfra.mdmobjectsservice.repositories.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Repository
public class MdmModelRepository implements ModelRepository {

    private final MDMAdapter mdmAdapter;
    private final MdmRequestHelper requestHelper;

    @Autowired
    public MdmModelRepository(MdmRequestHelper requestHelper) {
        this.requestHelper = requestHelper;
        this.mdmAdapter = new MDMAdapter();
    }

    @Override
    public CompletionStage<Page<Model>> getModels(Pageable pageable, Timestamp ts) {

        var configuration = requestHelper.getMdmConfiguration(Action.getModels);
        var parameters = new HashMap<String, Object>();
        var classDescription = requestHelper.getMapping(Model.class);

        return CompletableFuture.supplyAsync(() -> mdmAdapter.<Model>call(configuration, parameters, classDescription))
                .thenApply(PageImpl::new);
    }

}
