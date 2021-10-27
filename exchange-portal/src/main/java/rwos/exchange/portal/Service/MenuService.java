package rwos.exchange.portal.Service;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import rwos.exchange.portal.Entity.Menu;
import rwos.exchange.portal.Entity.PappuPassHogya;
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

        // System.out.println(store.getPaths());

        store.getPaths().forEach((api, value) -> {
            // System.out.println(api);
            value.readOperationsMap().forEach((method, val) -> {

                // System.out.println("-->" + method);
                // Map<String, Object> validation = new HashMap<>();
                try {
                    Menu menu = new Menu(method.name(), api, val.getSummary());
                    menu.setSubDescription(val.getDescription());
                    YamlParser yamlParser = new YamlParser();

                    if (!Objects.isNull(val.getResponses())) {
                        id = 100;
                        val.getResponses().forEach((resKey, resVal) -> {
                            Map<String, Object> res = new HashMap<>();
                            PappuPassHogya response = new PappuPassHogya();
                            if (resKey.equalsIgnoreCase("200")) {
                                if (resVal.getContent() != null) {
                                    resVal.getContent().forEach((contKey, contVal) -> {
                                        // res.put(resKey, contVal.getSchema().getProperties()); //without
                                        // FilterRedundantData()
                                        res.put(resKey, getSchema(contVal.getSchema().getProperties(),
                                                isNull(contVal.getSchema().getType())));
                                        response.setSuccess(res);
                                        response.setSuccessDetails(formTableFromSchema(contVal.getSchema().getProperties(),
                                                -1, isNull(contVal.getSchema().getRequired()),
                                                isNull(contVal.getSchema().getType())));
                                    });
                                }
                            } else {
                                if (resVal.getContent() != null) {
                                    resVal.getContent().forEach((contKey, contVal) -> {
                                        // res.put(resKey, contVal.getSchema().getProperties()); //without
                                        // FilterRedundantData()
                                        res.put(resKey, getSchema(contVal.getSchema().getProperties(),
                                                isNull(contVal.getSchema().getType())));
                                        response.setFailure(res);
                                        response.setFailureDetails(formTableFromSchema(contVal.getSchema().getProperties(),
                                                -1, isNull(contVal.getSchema().getRequired()),
                                                isNull(contVal.getSchema().getType())));
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
                            eachValidation.put("pattern", parVal.getSchema().getPattern() == null ? "-" : parVal.getSchema().getPattern());
                            eachValidation.put("enum", ((parVal.getSchema().getEnum() == null) ? "-"
                            : parVal.getSchema().getEnum().toString()));
                            eachValidation.put("maxLength", ((parVal.getSchema().getMaxLength() == null)
                            ? "-"
                            : parVal.getSchema().getMaxLength().toString()));
                            eachValidation.put("minLength", ((parVal.getSchema().getMinLength() == null)
                            ? "-"
                            : parVal.getSchema().getMinLength().toString()));

                            parameterValidation.add(eachValidation);
                            }
                        );

                        yamlParser.setParameterPayloadDetails(parameterDetails);
                        yamlParser.setParameterPayload(parameter);
                        yamlParser.setParameterValidation(parameterValidation);
                        
                    }
                    if (!Objects.isNull(val.getRequestBody())) {
                        id = 100;
                        // MediaType contentVal =
                        // val.getRequestBody().getContent().get("application/json");
                        val.getRequestBody().getContent().forEach((contentKey, contentVal) -> {
                            yamlParser.setRequestPayloadExample(contentVal.getSchema().getExample());

                            yamlParser.setRequestPayload(getSchema(nullFieldFilter(contentVal.getSchema()),
                                    isNull(contentVal.getSchema().getType())));

                            yamlParser.setRequestPayloadDetails(formTableFromSchema(nullFieldFilter(contentVal.getSchema()),
                                    -1, isNull(contentVal.getSchema().getRequired()),
                                    isNull(contentVal.getSchema().getType())));
                                    
                            yamlParser.setRequestValidation(
                                    formTableFromSchema(nullFieldFilter(contentVal.getSchema()), -1,
                                            isNull(contentVal.getSchema().getRequired()),
                                            isNull(contentVal.getSchema().getType())));
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

    //
    @SuppressWarnings("unchecked")
    public Object getSchema(Object obj, String type) {

        try {

            Map<String, Object> map = new HashMap<>();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> objMap = mapper.convertValue(obj, Map.class);

            if (type.equals("object")) {
                if (!Objects.isNull(objMap)) {
                    objMap.forEach((key, value) -> {

                        try {
                            Map<String, Object> flag = mapper.convertValue(value, Map.class);
                            Boolean flagOK = Objects.isNull(flag);
                            Boolean typeOK = false;
                            if (!flagOK)
                                typeOK = Objects.isNull(flag.get("type"));

                            if (!typeOK && !flagOK && flag.get("type").equals("object")) {
                                
                                map.put(key, getSchema(flag.get("properties"), "object"));
                            } else if (!flagOK && !typeOK && flag.get("type").equals("array")) {
                                map.put(key, getSchema(flag.get("items"), "array"));
                            } else {
                                if (!flagOK)
                                    map.put(key, flag.get("type"));
                            }
                        } catch (Exception e) {
                            
                        }

                    });
                }
                return map;
            } else if (type.equals("array")) {
                List<Object> list = new ArrayList<>();
                try {
                    if (!Objects.isNull(objMap) && !Objects.isNull(objMap.get("type"))) {
                        if (objMap.get("type").equals("object") && !Objects.isNull(objMap.get("properties"))) {
                            list.add(getSchema(objMap.get("properties"), "object"));
                        } else if (objMap.get("type").equals("array")) {
                            list.add(getSchema(objMap.get("items"), "array"));
                        } else {
                            list.add(objMap.get("type"));
                        }
                    }
                } catch (Exception e) {
                    // System.out.println("Mapper Dusra Else if |--> " + e.getMessage());
                }
                return list;
            }

        } catch (Exception e) {
            System.out.println("Exception |->>>>>" + e.getMessage());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Object> formTableFromSchema(Object obj, int pId, List<Object> requiredFileds, String type) {

        List<Object> tableData = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> objMap = mapper.convertValue(obj, Map.class);
            if (type.equals("object")) {
                if (!Objects.isNull(objMap)) {
                    objMap.forEach((key, value) -> {

                        Map<String, Object> table = new HashMap<>();
                        table.put("Level", ++id);
                        table.put("parentId", pId);
                        table.put("parameter", key);
                        table.put("Mendate", requiredFileds.contains(key));
                        table.put("Description",
                                Objects.isNull(mapper.convertValue(value, Map.class).get("description")) ? "-"
                                        : mapper.convertValue(value, Map.class).get("description"));
                        if (mapper.convertValue(value, Map.class).get("type").equals("object")) {
                            tableData.addAll(
                                    formTableFromSchema(mapper.convertValue(value, Map.class).get("properties"), id,
                                            requiredFileds, "object"));
                        } else if (mapper.convertValue(value, Map.class).get("type").equals("array")) {
                            tableData.addAll(formTableFromSchema(mapper.convertValue(value, Map.class).get("items"),
                                    id, requiredFileds, "array"));
                        } else {
                            table.put("Type", mapper.convertValue(value, Map.class).get("type"));
                            table.put("enum", mapper.convertValue(value, Map.class).get("enum") == null ? "-":  mapper.convertValue(value, Map.class).get("enum"));
                            table.put("maxLength", mapper.convertValue(value, Map.class).get("maxLength") == null ? "-": mapper.convertValue(value, Map.class).get("maxLength") );
                            table.put("minLength", mapper.convertValue(value, Map.class).get("minLength") == null ? "-" : mapper.convertValue(value, Map.class).get("minLength"));
                            table.put("pattern", mapper.convertValue(value, Map.class).get("pattern") == null ? "-" : mapper.convertValue(value, Map.class).get("pattern"));
                        }
                        tableData.add(table);
                    });
                }

            } else if (type.equals("array")) {

                if (!Objects.isNull(objMap) && !Objects.isNull(objMap.get("type"))) {
                    if (objMap.get("type").equals("object") && !Objects.isNull(objMap.get("properties"))) {
                        tableData.addAll(formTableFromSchema(objMap.get("properties"), id, requiredFileds, "object"));
                    } else if (objMap.get("type").equals("array")) {
                        tableData.addAll(formTableFromSchema(objMap.get("items"), id, requiredFileds, "array"));
                    }
                }
            }

        } catch (Exception e) {
            // System.out.println("Exception at Formating Table | " + e.getMessage());
        }
        return tableData;
    }

    public Object testing(String path) {

        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);
        OpenAPI store = new OpenAPIV3Parser().read(path, null, parseOptions);

        // return nullFieldFilter(
        // store.getPaths().get("/mplace/selleritems").getPost().getResponses().get("200").getContent().get("*/*").getSchema());
        // return nullFieldFilter(
        // store.getPaths().get("/mplace/selleritems").getPost().getRequestBody().getContent().get("application/json").getSchema())
        // ;
        
        
        return nullFieldFilter(store.getPaths().get("/purchase/orders").getPost().getResponses().get("200").getContent().get("*/*").getSchema());
        // return
        // getSchema(nullFieldFilter(store.getPaths().get("/purchase/orders").getPost().getRequestBody()
        // .getContent().get("application/json").getSchema()), "array");
    }

    public Object nullFieldFilter(Object schema) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(Include.NON_NULL);
            String filteredSchema = mapper.writeValueAsString(schema);
            return new ObjectMapper().readTree(filteredSchema);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
