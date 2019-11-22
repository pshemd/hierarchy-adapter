package com.zyfra.mdmobjectsservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Объект
 */
@ApiModel(description = "Объект")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-11-19T14:25:00.902Z")
public class Object_ {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("parentid")
    private String parentId = null;

    @JsonProperty("objectprototypeid")
    private String objectPrototypeId = null;

    @JsonProperty("childscount")
    private Integer childsCount = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object_ id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Количество дочерних объектов
     **/
    @ApiModelProperty(value = "Количество дочерних объектов")
    public Integer getChildsCount() {
        return childsCount;
    }

    public void setChildsCount(Integer childsCount) {
        this.childsCount = childsCount;
    }

    @JsonProperty("description")
    private String description = null;


    @JsonProperty("modelid")
    private String modelId = null;

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public Object_ name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Наименование Объекта
     *
     * @return name
     **/
    @ApiModelProperty(required = true, value = "Наименование Объекта")
    @NotNull
    @Size(min = 1, max = 255)


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object_ parentId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    /**
     * Идентификатор ресурса родителя
     *
     * @return parentId
     **/
    @ApiModelProperty(value = "Идентификатор ресурса родителя")

    @Valid

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Object_ objectPrototypeId(String prototypeId) {
        this.objectPrototypeId = prototypeId;
        return this;
    }

    /**
     * Идентификатор прототипа
     *
     * @return objectPrototypeId
     **/
    @ApiModelProperty(value = "Идентификатор прототипа")

    @Valid

    public String getObjectPrototypeId() {
        return objectPrototypeId;
    }

    public void setObjectPrototypeId(String objectPrototypeId) {
        this.objectPrototypeId = objectPrototypeId;
    }


    public Object_ description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Описание
     *
     * @return description
     **/
    @ApiModelProperty(value = "Описание")


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Object_ object = (Object_) o;
        return Objects.equals(this.id, object.getId()) &&
                Objects.equals(this.name, object.name) &&
                Objects.equals(this.parentId, object.parentId) &&
                Objects.equals(this.description, object.description) &&
                Objects.equals(this.modelId, object.modelId) &&
                Objects.equals(this.objectPrototypeId, object.objectPrototypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), name, parentId, description);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Object {\n");

        sb.append("    id: ").append(toIndentedString(getId())).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
        sb.append("    modelId: ").append(toIndentedString(modelId)).append("\n");
        sb.append("    objectPrototypeId: ").append(toIndentedString(objectPrototypeId)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

