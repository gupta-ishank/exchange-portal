package rwos.exchange.portal.Service;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import rwos.exchange.portal.Entity.Menu;
import rwos.exchange.portal.Entity.ResponseStatus;
import rwos.exchange.portal.Entity.YamlParser;

@Service
public class MenuService {

    private int id = 100;

    private FileFilter filter = (file) -> file.isDirectory() || !file.isHidden() || file.getName().endsWith(".json")
            || file.getName().endsWith(".yaml");

    public int getType(File file) {
        if (file.isDirectory())
            return 1;
        else if (file.getName().endsWith(".json"))
            return 2;
        else
            return 3;
    }

    public List<Object> isNull(List<Object> data) {
        if (Objects.isNull(data))
            return new ArrayList<>();
        else
            return data;
    }

    public String isNull(String data) {
        if (Objects.isNull(data))
            return "";
        else
            return data;
    }

    // returns folder structure along with file method with schemas (excluding the
    // empty folders) and
    public List<Menu> getAllMenu(File folder) {
        List<Menu> menus = new ArrayList<>();
        if (folder.isDirectory()) {
            for (File file : folder.listFiles(filter)) {
                List<Menu> subDirectory = getAllMenu(file);
                if (subDirectory.isEmpty() == false)
                    menus.add(new Menu(file.getName(), file.getAbsolutePath(), getType(file), subDirectory));
            }
        } else {
            menus.addAll(getAllApis(folder.getAbsolutePath()));
        }
        return menus;
    }

    // return file methods with their schemas in a particular JSON or YAML file
    @SuppressWarnings("unchecked")
    public List<Menu> getAllApis(String path) { // parameter: file path

        List<Menu> data = new ArrayList<>();

        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);
        OpenAPI store = new OpenAPIV3Parser().read(path, null, parseOptions);

        store.getPaths().forEach((api, value) -> {
            value.readOperationsMap().forEach((method, val) -> {

                try {
                    Menu menu = new Menu(method.name(), api, val.getSummary());
                    menu.setSubDescription(val.getDescription());
                    YamlParser yamlParser = new YamlParser();

                    if (!Objects.isNull(val.getResponses())) {
                        id = 100;
                        val.getResponses().forEach((resKey, resVal) -> {
                            Map<String, Object> res = new HashMap<>();
                            ResponseStatus response = new ResponseStatus();
                            if (resKey.equalsIgnoreCase("200")) {
                                if (resVal.getContent() != null) {
                                    resVal.getContent().forEach((contKey, contVal) -> {

                                        res.put(resKey, getSchema(contVal.getSchema()));
                                        response.setSuccess(res);
                                        response.setSuccessDetails(
                                                schemaToTable(contVal.getSchema(), -1,
                                                        isNull(contVal.getSchema().getRequired())));
                                    });
                                }
                            } else {
                                if (resVal.getContent() != null) {
                                    resVal.getContent().forEach((contKey, contVal) -> {

                                        res.put(resKey, getSchema(contVal.getSchema()));
                                        response.setFailure(res);
                                        response.setFailureDetails(
                                                schemaToTable(contVal.getSchema(), -1,
                                                        isNull(contVal.getSchema().getRequired())));
                                    });
                                }
                            }
                            yamlParser.setResponsePayload(response);
                        });

                    }
                    if (!Objects.isNull(val.getParameters())) {
                        id = 100;
                        List<Object> parameterDetails = new ArrayList<>();
                        List<Object> parameterValidation = new ArrayList<>();
                        Map<String, String> parameter = new HashMap<>();

                        val.getParameters().forEach(parVal -> {
                            Map<String, Object> parameterMap = new HashMap<>();
                            Map<String, Object> eachValidation = new HashMap<>();
                            parameter.put(parVal.getName(), parVal.getSchema().getType());
                            parameterMap.put("Type", parVal.getSchema().getType());
                            parameterMap.put("Level", ++id);
                            parameterMap.put("Description",
                                    parVal.getDescription() == null ? "-" : parVal.getDescription());
                            parameterMap.put("parameterType", parVal.getIn());
                            parameterMap.put("parentId", -1);
                            parameterMap.put("Mendate", parVal.getRequired() == null ? false : true);
                            parameterMap.put("parameter", parVal.getName());
                            parameterDetails.add(parameterMap);

                            eachValidation.put("Level", ++id);
                            eachValidation.put("parameter", parVal.getName());
                            eachValidation.put("parentId", -1);
                            eachValidation.put("pattern",
                                    parVal.getSchema().getPattern() == null ? "-" : parVal.getSchema().getPattern());
                            eachValidation.put("enum", ((parVal.getSchema().getEnum() == null) ? "-"
                                    : parVal.getSchema().getEnum().toString()));
                            eachValidation.put("maxLength", ((parVal.getSchema().getMaxLength() == null) ? "-"
                                    : parVal.getSchema().getMaxLength().toString()));
                            eachValidation.put("minLength", ((parVal.getSchema().getMinLength() == null) ? "-"
                                    : parVal.getSchema().getMinLength().toString()));

                            parameterValidation.add(eachValidation);
                        });

                        yamlParser.setParameterPayloadDetails(parameterDetails);
                        yamlParser.setParameterPayload(parameter);
                        yamlParser.setParameterValidation(parameterValidation);

                    }
                    if (!Objects.isNull(val.getRequestBody())) {
                        id = 100;
                        val.getRequestBody().getContent().forEach((contentKey, contentVal) -> {
                            yamlParser.setRequestPayloadExample(contentVal.getSchema().getExample());

                            yamlParser.setRequestPayload(getSchema(nullFieldFilter(contentVal.getSchema())));

                            yamlParser.setRequestPayloadDetails(
                                    schemaToTable(nullFieldFilter(contentVal.getSchema()), -1,
                                            isNull(contentVal.getSchema().getRequired())));

                            yamlParser.setRequestValidation(schemaToTable(nullFieldFilter(contentVal.getSchema()),
                                    -1, isNull(contentVal.getSchema().getRequired())));
                        });

                    }
                    if (!Objects.isNull(val.getSecurity())) {
                        yamlParser.setSecurity(val.getSecurity());
                    }
                    menu.setSchema(yamlParser);
                    data.add(menu);
                } catch (Exception e) {
                }
            });
        });
        return data;
    }

    Set<String> ignoreFileds = new HashSet<>(Arrays.asList("exampleSetFlag", "extensions", "type", "items", "properties"));
    
    @SuppressWarnings("unchecked")
    public Object getSchema(Object obj) {
        Map<String, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> objMap = mapper.convertValue(obj, Map.class);
        try {
            for(String key : objMap.keySet()){
                if(objMap.containsKey("properties")){
                    map.put("schema", getSchema(objMap.get("properties")));
                } else if(objMap.containsKey("items")){
                    map.put("schema", getSchema(objMap.get("items")));
                }
                if(!ignoreFileds.contains(key)){
                    Map<String, Object> newObj = mapper.convertValue(objMap.get(key), Map.class);
                    if(newObj.get("type").equals("array") || newObj.get("type").equals("object")){
                        map.put(key, getSchema(objMap.get(key)));
                    }else{
                        map.put(key, newObj.get("type"));
                    }
                }
                
            };
        } catch (Exception e) {
            System.out.println("getSchema() - Body" + e.getMessage());
        }
        if(map.containsKey("schema")) return map.get("schema");
        return map;
    }
    @SuppressWarnings("unchecked")
    public List<Object> schemaToTable(Object obj, int pId, List<Object> requiredFileds) {
        List<Object> tableData = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> objMap = mapper.convertValue(obj, Map.class);
        try {
            for(String key : objMap.keySet()){
                if(objMap.containsKey("properties")){
                    return schemaToTable(objMap.get("properties"), pId, requiredFileds);
                } else if(objMap.containsKey("items")){
                    return schemaToTable(objMap.get("items"), pId, requiredFileds);
                }
                else if(!ignoreFileds.contains(key)){
                    Map<String, Object> table = new HashMap<>();
                    Map<String, Object> newObj = mapper.convertValue(objMap.get(key), Map.class);
                    table.put("Level", ++id);
                    table.put("parentId", pId);
                    table.put("parameter", key);
                    table.put("Mendate", requiredFileds.contains(key));
                    table.put("Description",
                            Objects.isNull(newObj.get("description")) ? "-" : newObj.get("description"));
                    table.put("Type", newObj.get("type"));
                    table.put("enum", newObj.get("enum") == null ? "-"
                            : newObj.get("enum"));
                    table.put("maxLength", newObj.get("maxLength") == null ? "-"
                            : newObj.get("maxLength"));
                    table.put("minLength", newObj.get("minLength") == null ? "-"
                            : newObj.get("minLength"));
                    table.put("pattern", newObj.get("pattern") == null ? "-"
                            : newObj.get("pattern"));
                    if(newObj.get("type").equals("array") || newObj.get("type").equals("object")){
                        tableData.addAll(schemaToTable(objMap.get(key), id, requiredFileds));
                    }
                    tableData.add(table);
                }
                
            };
        } catch (Exception e) {
            System.out.println("schemaToTable() - Body | " + e.getMessage());
        }
        return tableData;
    }

    public Object nullFieldFilter(Object schema) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(Include.NON_NULL);
            String filteredSchema = mapper.writeValueAsString(schema);
            return new ObjectMapper().readTree(filteredSchema);
        } catch (Exception e) {
            // System.out.println("nullFieldFilter - Body | " + e.getMessage());
        }
        return null;
    }

    ///////////////////////////////////////////////////////// Only For
    ///////////////////////////////////////////////////////// Testing////////////////////////////////////////////////////////////////////////////////////////
    public Object testing(String path) {

        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);
        OpenAPI store = new OpenAPIV3Parser().read(path, null, parseOptions);

        return getSchema(nullFieldFilter(store.getPaths().get("/purchase/orders").getPost().getResponses().get("200").getContent()
        .get("*/*").getSchema())) ;
    }


}
