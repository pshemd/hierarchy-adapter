package com.zyfra.mdmobjectsservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.UUID;

/**
 * Модель
 */
@ApiModel(description = "Модель")
@Validated
public class Model {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("modelprototypeid")
    private UUID modelPrototypeId = null;

    @JsonProperty("description")
    private String description = null;

    /**
     * id модели
     *
     * @return id
     **/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Model id(String id) {
        this.id = id;
        return this;
    }

    public Model name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Наименование модели
     *
     * @return name
     **/
    @ApiModelProperty(required = true, value = "Наименование модели")
    @NotNull
    @Size(min = 1, max = 255)


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Model modelPrototypeId(UUID modelPrototypeId) {
        this.modelPrototypeId = modelPrototypeId;
        return this;
    }

    /**
     * Идентификатор прототипа модели
     *
     * @return modelPrototypeId
     **/
    @ApiModelProperty(required = true, value = "Идентификатор прототипа модели")
    @NotNull

    @Valid

    public UUID getModelPrototypeId() {
        return modelPrototypeId;
    }

    public void setModelPrototypeId(UUID modelPrototypeId) {
        this.modelPrototypeId = modelPrototypeId;
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
        Model model = (Model) o;
        return Objects.equals(this.id, model.getId()) &&
                Objects.equals(this.name, model.name) &&
                Objects.equals(this.description, model.description) &&
                Objects.equals(this.modelPrototypeId, model.modelPrototypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), name, description, modelPrototypeId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model {\n");
        sb.append("    id: ").append(toIndentedString(getId())).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    modelprototypeid: ").append(toIndentedString(modelPrototypeId)).append("\n");
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

