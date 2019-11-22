package com.zyfra.mdmobjectsservice.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zyfra.mdmclient.model.ClassDescription;
import com.zyfra.mdmclient.model.SetterDescription;
import com.zyfra.mdmobjectsservice.model.Model;
import com.zyfra.mdmobjectsservice.model.Object_;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

@Component
public class MdmRequestHelper {

    private ObjectMapper objectMapper;

    public MdmRequestHelper() {
        this.objectMapper = new ObjectMapper();
    }

    public Pair<String, String> getMdmConfiguration(Action action) {

        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(Paths.get("src", "main", "resources", "requests.json").toFile());
            if (rootNode != null) {
                switch (action) {
                    case getModels:
                        return Pair.of(rootNode.path("GET /models").path("url").textValue(), rootNode.path("GET /models").path("request").textValue());
                    case getObjects:
                        return Pair.of(rootNode.path("GET /models/{id}/objects").path("url").textValue(), rootNode.path("GET /models/{id}/objects").path("request").textValue());
                    case getObject:
                        return Pair.of(rootNode.path("GET /objects/{id}").path("url").textValue(), rootNode.path("GET /objects/{id}").path("request").textValue());
                    case getChildObjects:
                        return Pair.of(rootNode.path("GET /models/{id}/objects/{objid}/objects").path("url").textValue(), rootNode.path("GET /models/{id}/objects/{objid}/objects").path("request").textValue());
                    default:
                        return null;
                }
            } else
                throw new RuntimeException("Bad json-configuration");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ClassDescription getMapping (Class clazz) {

        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(Paths.get("src", "main", "resources", "mapping.json").toFile());
            if (rootNode != null) {
                if (clazz.getName().equals(Model.class.getName())) {

                    var modelNode = rootNode.path("Model");

                    var setters = Arrays.asList(
                            new SetterDescription().setSetterName("setId").setSetterType("java.lang.String").setJsonPath(modelNode.path("jsonPathInItemForId").textValue()),
                            new SetterDescription().setSetterName("setName").setSetterType("java.lang.String").setJsonPath(modelNode.path("jsonPathInItemForName").textValue()),
                            new SetterDescription().setSetterName("setDescription").setSetterType("java.lang.String").setJsonPath(modelNode.path("jsonPathInItemForDescription").textValue())
                    );

                    var classDescription = new ClassDescription();

                    classDescription.setClassName("com.zyfra.mdmobjectsservice.model.Model");
                    classDescription.setSetterDescriptions(setters);
                    classDescription.setJsonPathItems(modelNode.path("jsonPathItems").textValue());
                    return classDescription;
                }
                if (clazz.getName().equals(Object_.class.getName())) {

                    var objectNode = rootNode.path("Object");

                    var setters = Arrays.asList(
                            new SetterDescription().setSetterName("setId").setSetterType("java.lang.String").setJsonPath(objectNode.path("jsonPathInItemForId").textValue()),
                            new SetterDescription().setSetterName("setName").setSetterType("java.lang.String").setJsonPath(objectNode.path("jsonPathInItemForName").textValue()),
                            new SetterDescription().setSetterName("setDescription").setSetterType("java.lang.String").setJsonPath(objectNode.path("jsonPathInItemForDescription").textValue()),
                            new SetterDescription().setSetterName("setParentId").setSetterType("java.lang.String").setJsonPath(objectNode.path("jsonPathInItemForParentId").textValue())
                    );

                    var classDescription = new ClassDescription();
                    classDescription.setClassName("com.zyfra.mdmobjectsservice.model.Object_");
                    classDescription.setSetterDescriptions(setters);
                    classDescription.setJsonPathItems(objectNode.path("jsonPathItems").textValue());

                    return classDescription;
                } else
                    return null;

            } else
                throw new RuntimeException("Bad json-configuration");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }


    }

}
