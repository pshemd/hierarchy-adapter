package com.zyfra.mdmobjectsservice.controllers.api;

import com.zyfra.mdmobjectsservice.model.Model;
import com.zyfra.mdmobjectsservice.model.Object_;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.concurrent.CompletionStage;

@Api(value = "models", description = "the objects API", tags = {"objects",})
public interface ObjectsApi {

    @ApiOperation(value = "Получить страницу моделей", nickname = "getModels", notes = "", tags = {"objects",}, authorizations = {
            @Authorization(value = "oauth2", scopes = {

            }),
            @Authorization(value = "token")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query", value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query", value = "Number of records per page.")
//            @ApiImplicitParam(name = "sort", dataType = "string",  paramType = "query", value = "Sorting criteria in the format: property(asc|desc). " )
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Список моделей."),
            @ApiResponse(code = 401, message = "Неавторизованный доступ."),
            @ApiResponse(code = 500, message = "Не удалось получить все модели.")})
    @RequestMapping(value = "/models",
            method = RequestMethod.GET)
    CompletionStage<Page<Model>> getModels(@ApiIgnore @Valid Pageable pageable
            , @ApiParam(value = "ts") @Valid @RequestParam(value = "ts", required = false) Timestamp ts) throws IOException;


    @ApiOperation(value = "Получить страницу всех объектов модели", nickname = "getRootObjects", notes = "", tags = {"objects",}, authorizations = {
            @Authorization(value = "oauth2", scopes = {

            }),
            @Authorization(value = "token")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query", value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query", value = "Number of records per page.")
//            @ApiImplicitParam(name = "sort", dataType = "string",  paramType = "query", value = "Sorting criteria in the format: property(asc|desc). " )
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Список объектов модели."),
            @ApiResponse(code = 401, message = "Неавторизованный доступ."),
            @ApiResponse(code = 500, message = "Не удалось получить все корневые объекты модели.")})
    @RequestMapping(value = "/models/{id}/objects",
            method = RequestMethod.GET)
    CompletionStage<Page<Object_>> getObjects(@ApiParam(value = "id модели", required = true) @Valid @PathVariable("id") String modelId
            , @ApiIgnore @Valid Pageable pageable
            , @ApiParam(value = "Признак получения только корневых элементов", required = false, defaultValue = "true") @RequestParam(value = "onlyRoot", required = false, defaultValue = "true") Boolean onlyRoot
            , @ApiParam(value = "ts") @Valid @RequestParam(value = "ts", required = false) Timestamp ts) throws IOException;

    @ApiOperation(value = "Получить страницу всех дочерних объектов указанного объекта модели", nickname = "getObjectTree", notes = "", tags = {"objects",}, authorizations = {
            @Authorization(value = "oauth2", scopes = {

            }),
            @Authorization(value = "token")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query", value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query", value = "Number of records per page.")
//            @ApiImplicitParam(name = "sort", dataType = "string",  paramType = "query", value = "Sorting criteria in the format: property(asc|desc). " )
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Список дочерних объектов указанного объекта."),
            @ApiResponse(code = 401, message = "Неавторизованный доступ."),
            @ApiResponse(code = 500, message = "Не удалось получить все дочерние объекты указанного объекта.")})
    @RequestMapping(value = "/models/{id}/objects/{objid}/objects",
            method = RequestMethod.GET)
    CompletionStage<Page<Object_>> getObjectTree(@ApiParam(value = "id модели", required = true) @Valid @PathVariable("id") String modelId
            , @ApiParam(value = "id объекта модели", required = true) @PathVariable("objid") String objectId
            , @ApiParam(value = "признак необходимости расчета childCount", defaultValue = "true") @RequestParam(value = "isNeedChildrenCount", required = false, defaultValue = "true") Boolean isNeedChildCount
            , @ApiIgnore @Valid Pageable pageable
            , @ApiParam(value = "ts") @Valid @RequestParam(value = "ts", required = false) Timestamp ts) throws IOException;

    @ApiOperation(value = "Получить объект", nickname = "getObject", tags = {"objects"}, authorizations = {
            @Authorization(value = "oauth2", scopes = {

            }),
            @Authorization(value = "token")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BadRequest"),
            @ApiResponse(code = 404, message = "NotFound")})
    @RequestMapping(value = "/objects/{id}", method = RequestMethod.GET)
    CompletionStage<Object_> getObject(
            @ApiParam(value = "id объекта", required = true) @PathVariable("id") String objectId,
            @ApiParam(value = "ts") @RequestParam(value = "ts", required = false) Timestamp ts) throws IOException;

}
