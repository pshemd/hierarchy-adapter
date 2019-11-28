package com.zyfra.mdmobjectsservice.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zyfra.mdmclient.model.ClassDescription;
import com.zyfra.mdmclient.model.MDMConfiguration;
import com.zyfra.mdmclient.model.SetterDescription;
import com.zyfra.mdmobjectsservice.model.Model;
import com.zyfra.mdmobjectsservice.model.Object_;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

@Component
public class MdmRequestHelper {

    @Value("${mdm.requests:requests.json}")
    private String mdmRequest;

    @Value("${mdm.mapping:mapping.json}")
    private String mdmMapping;

    private ObjectMapper objectMapper;

    public MdmRequestHelper() {
        this.objectMapper = new ObjectMapper();
    }

    public MDMConfiguration getMdmConfiguration(Action action) throws IOException {

        Resource resource = new ClassPathResource(mdmRequest);
        InputStream inputStream = resource.getInputStream();

        try {
            var configurationFromFile = objectMapper.readValue(inputStream, new TypeReference<Map<String, MDMConfiguration>>() {
            });
            switch (action) {
                case getModels:
                    return configurationFromFile.get("GET /models");
                case getObjects:
                    return configurationFromFile.get("GET /models/{id}/objects");
                case getChildObjects:
                    return configurationFromFile.get("GET /models/{id}/objects/{objid}/objects");
                case getObject:
                    return configurationFromFile.get("GET /objects/{id}");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ClassDescription getMapping(Class clazz) throws IOException {

        Resource resource = new ClassPathResource(mdmMapping);
        InputStream inputStream = resource.getInputStream();

        try {
            var mappingFromFile = objectMapper.readTree(inputStream);
            if (clazz.getName().equals(Model.class.getName())) {

                var modelNode = mappingFromFile.path("Model");

                var setters = Arrays.asList(
                        new SetterDescription().setSetterName("setId").setSetterType("java.lang.String").setJsonPath(modelNode.path("id").textValue()),
                        new SetterDescription().setSetterName("setName").setSetterType("java.lang.String").setJsonPath(modelNode.path("name").textValue()),
                        new SetterDescription().setSetterName("setDescription").setSetterType("java.lang.String").setJsonPath(modelNode.path("description").textValue())
                );

                var classDescription = new ClassDescription();

                classDescription.setClassName("com.zyfra.mdmobjectsservice.model.Model");
                classDescription.setSetterDescriptions(setters);
                classDescription.setJsonPathItems(modelNode.path("jsonPathItems").textValue());
                return classDescription;
            }
            if (clazz.getName().equals(Object_.class.getName())) {

                var objectNode = mappingFromFile.path("Object");

                var setters = Arrays.asList(
                        new SetterDescription().setSetterName("setId").setSetterType("java.lang.String").setJsonPath(objectNode.path("id").textValue()),
                        new SetterDescription().setSetterName("setName").setSetterType("java.lang.String").setJsonPath(objectNode.path("name").textValue()),
                        new SetterDescription().setSetterName("setDescription").setSetterType("java.lang.String").setJsonPath(objectNode.path("description").textValue()),
                        new SetterDescription().setSetterName("setModelId").setSetterType("java.lang.String").setJsonPath(objectNode.path("modelId").textValue()),
                        new SetterDescription().setSetterName("setParentId").setSetterType("java.lang.String").setJsonPath(objectNode.path("parentId").textValue())
                );

                var classDescription = new ClassDescription();
                classDescription.setClassName("com.zyfra.mdmobjectsservice.model.Object_");
                classDescription.setSetterDescriptions(setters);
                classDescription.setJsonPathItems(objectNode.path("jsonPathItems").textValue());

                return classDescription;
            } else
                return null;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }


    }

}
