package com.zyfra.mdmobjectsservice.repositories;

import com.zyfra.mdmobjectsservice.model.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.concurrent.CompletionStage;

public interface ModelRepository {

    CompletionStage<Page<Model>> getModels(Pageable pageable, Timestamp ts) throws IOException;
}
