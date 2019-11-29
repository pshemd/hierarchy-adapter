package com.zyfra.mdmobjectsservice.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zyfra.mdmclient.model.ClassDescription;
import com.zyfra.mdmclient.model.MDMConfiguration;
import com.zyfra.mdmclient.model.SetterDescription;
import com.zyfra.mdmobjectsservice.model.Model;
import com.zyfra.mdmobjectsservice.model.Object_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Component
public class MdmRequestHelper {

    private ObjectMapper objectMapper;

    private Map<String, MDMConfiguration> mdmConfigurationMap;
    private JsonNode mdmMapping;

    @Autowired
    public MdmRequestHelper(Map<String, MDMConfiguration> mdmConfigurationMap, JsonNode mdmMapping) {
        this.mdmConfigurationMap = mdmConfigurationMap;
        this.mdmMapping = mdmMapping;
        this.objectMapper = new ObjectMapper();
    }

    public MDMConfiguration getMdmConfiguration(Action action) {

        switch (action) {
            case getModels:
                return mdmConfigurationMap.get("GET /models");
            case getObjects:
                return mdmConfigurationMap.get("GET /models/{id}/objects");
            case getChildObjects:
                return mdmConfigurationMap.get("GET /models/{id}/objects/{objid}/objects");
            case getObject:
                return mdmConfigurationMap.get("GET /objects/{id}");
            default:
                return null;
        }
    }

    public ClassDescription getMapping(Class clazz) {

        if (clazz.getName().equals(Model.class.getName())) {

            var modelNode = mdmMapping.path("Model");

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

            var objectNode = mdmMapping.path("Object");

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
    }

}
