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
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import rwos.exchange.portal.Entity.Menu;
import rwos.exchange.portal.Entity.PappuPassHogya;
import rwos.exchange.portal.Entity.ToMap;
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
                                        res.put(resKey, filterRedundedData(contVal.getSchema().getProperties(),
                                                isNull(contVal.getSchema().getType())));
                                        response.setSuccess(res);
                                        response.setSuccessDetails(contVal.getSchema().getProperties());
                                    });
                                }
                            } else {
                                if (resVal.getContent() != null) {
                                    resVal.getContent().forEach((contKey, contVal) -> {
                                        // res.put(resKey, contVal.getSchema().getProperties()); //without
                                        // FilterRedundantData()
                                        res.put(resKey, filterRedundedData(contVal.getSchema().getProperties(),
                                                isNull(contVal.getSchema().getType())));
                                        response.setFailure(res);
                                        response.setFailureDetails(formatTableData2(contVal.getSchema().getProperties(),
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
                        Map<String, String> parameter = new HashMap<>();

                        val.getParameters().forEach(parVal -> {
                            Map<String, Object> parameterMap = new HashMap<>();
                            // Map<String, String> eachValidation = new HashMap<>();
                            parameter.put(parVal.getName(), parVal.getSchema().getType());
                            parameterMap.put("Level", ++id);
                            parameterMap.put("description",
                                    parVal.getDescription() == null ? "-" : parVal.getDescription());
                            parameterMap.put("parentId", -1);
                            parameterMap.put("Mandate", parVal.getRequired());
                            parameterMap.put("parameter", parVal.getName());
                            parameterDetails.add(parameterMap);

                            // if (method.toString().equals("POST")) {
                            // val.getParameters().forEach(x -> {

                            // });
                            // }

                            // can get the pattern here
                            // System.out.print(method);
                            // System.out.println(
                            // " - pattern ---> " + parVal.getName() + " | " +
                            // parVal.getSchema().getPattern());

                            // if (parVal.getSchema().getEnum() != null) {
                            // System.out.print(method);
                            // System.out.println(
                            // " - enum ---> " + parVal.getName() + " | " + parVal.getSchema().getEnum());
                            // }
                            // eachValidation.put("pattern", parVal.getSchema().getPattern());
                            // eachValidation.put("enum", ((parVal.getSchema().getEnum() == null) ? null
                            // : parVal.getSchema().getEnum().toString()));
                            // eachValidation.put("maxLength", ((parVal.getSchema().getMaxLength() == null)
                            // ? null
                            // : parVal.getSchema().getMaxLength().toString()));
                            // eachValidation.put("minLength", ((parVal.getSchema().getMinLength() == null)
                            // ? null
                            // : parVal.getSchema().getMinLength().toString()));

                            // validation.put(parVal.getName(), eachValidation);
                            // }
                        });

                        yamlParser.setParameterPayloadDetails(parameterDetails);
                        yamlParser.setParameterPayload(parameter);
                        // yamlParser.setValidation(formatTableData2(validation, -1, new ArrayList<>(),
                        // "array"));
                    }
                    if (!Objects.isNull(val.getRequestBody())) {
                        id = 100;
                        // MediaType contentVal =
                        // val.getRequestBody().getContent().get("application/json");
                        val.getRequestBody().getContent().forEach((contentKey, contentVal) -> {
                            // System.out.println("contentVal: " + contentVal.getSchema());

                            // System.out.println(contentVal.getSchema().getName() + " | MaxLength: "
                            // + contentVal.getSchema().getMaxLength());

                            yamlParser.setRequestPayloadExample(contentVal.getSchema().getExample());
                            // yamlParser.setRequestPayload((contentVal.getSchema())); //without
                            // FilterRedundantData()
                            yamlParser.setRequestPayload(filterRedundedData(contentVal.getSchema(),
                                    isNull(contentVal.getSchema().getType())));
                            yamlParser.setRequestPayloadDetails(formatTableData2(contentVal.getSchema().getProperties(),
                                    -1, isNull(contentVal.getSchema().getRequired()),
                                    isNull(contentVal.getSchema().getType())));
                            yamlParser.setRequestValidation(
                                    formatRequestValidationTableData(contentVal.getSchema().getProperties(), -1,
                                            isNull(contentVal.getSchema().getRequired()),
                                            isNull(contentVal.getSchema().getType())));
                        });

                        // if (contentVal != null) {

                        // }

                    }
                    if (!Objects.isNull(val.getSecurity())) {
                        id = 100;
                        // MediaType contentVal = val.getSecurity()
                        // .getContent().get("application/json");
                        yamlParser.setSecurity(val.getSecurity());
                    }
                    menu.setSchema(yamlParser);
                    // menu.setValidation(validation);
                    data.add(menu);
                } catch (Exception e) {
                    // System.out.println("exception in " + method.name() + "|" + e.getMessage());
                    // System.out.println(val.getRequestBody().getContent());
                }
            });
        });
        return data;
    }

    //
    @SuppressWarnings("unchecked")
    public Object filterRedundedData(Object obj, String type) {

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
                                // System.out.println(" mapper testing: -> "
                                // + Objects.isNull(mapper.convertValue(value, Map.class).get("properties")));
                                map.put(key, filterRedundedData(flag.get("properties"), "object"));
                            } else if (!flagOK && !typeOK && flag.get("type").equals("array")) {
                                map.put(key, filterRedundedData(flag.get("items"), "array"));
                            } else {
                                if (!flagOK)
                                    map.put(key, flag.get("type"));
                            }
                        } catch (Exception e) {
                            // System.out.println("Mapper Dusra If |--> " + e.getMessage());
                        }

                    });
                }
                return map;
            } else if (type.equals("array")) {
                List<Object> list = new ArrayList<>();
                try {
                    if (!Objects.isNull(objMap) && !Objects.isNull(objMap.get("type"))) {
                        if (objMap.get("type").equals("object") && !Objects.isNull(objMap.get("properties"))) {
                            list.add(filterRedundedData(objMap.get("properties"), "object"));
                        } else if (objMap.get("type").equals("array")) {
                            list.add(filterRedundedData(objMap.get("items"), "array"));
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
    public List<Object> formatRequestValidationTableData(Object obj, int pId, List<Object> requiredFileds,
            String type) {

        List<Object> tableData = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> objMap = mapper.convertValue(obj, Map.class);
            if (type.equals("object")) {
                if (!Objects.isNull(objMap)) {
                    objMap.forEach((key, value) -> {

                        Map<String, Object> table = new HashMap<>();
                        try {
                            table.put("Level", ++id);
                            table.put("parentId", pId);
                            table.put("parameter", key);
                        } catch (Exception e) {
                            // System.out.println("Mapper Initialization |--> " + e.getMessage());

                        }
                        try {
                            if (mapper.convertValue(value, Map.class).get("type").equals("object")) {
                                tableData.addAll(formatRequestValidationTableData(
                                        mapper.convertValue(value, Map.class).get("properties"), id, requiredFileds,
                                        "object"));
                            } else if (mapper.convertValue(value, Map.class).get("type").equals("array")) {
                                tableData.addAll(formatRequestValidationTableData(
                                        mapper.convertValue(value, Map.class).get("items"), id, requiredFileds,
                                        "array"));
                            } else {
                                // if( mapper.convertValue(value, Map.class).containsKey("type") )
                                System.out.println(
                                        key + "maxLength: " + mapper.convertValue(value, Map.class).get("maxLength"));
                                table.put("enum", mapper.convertValue(value, Map.class).get("enum"));
                                table.put("maxLength", mapper.convertValue(value, Map.class).get("maxLength"));
                                table.put("minLength", mapper.convertValue(value, Map.class).get("minLength"));
                                table.put("pattern", mapper.convertValue(value, Map.class).get("pattern"));
                            }
                        } catch (Exception e) {
                            System.out.println("Mapper If line: 283 |--> " + e.getMessage());
                        }
                        tableData.add(table);
                    });
                }

            } else if (type.equals("array")) {
                List<Object> list = new ArrayList<>();
                try {
                    if (!Objects.isNull(objMap) && !Objects.isNull(objMap.get("type"))) {
                        if (objMap.get("type").equals("object") && !Objects.isNull(objMap.get("properties"))) {
                            tableData.addAll(formatRequestValidationTableData(objMap.get("properties"), id,
                                    requiredFileds, "object"));
                        } else if (objMap.get("type").equals("array")) {
                            tableData.addAll(
                                    formatRequestValidationTableData(objMap.get("items"), id, requiredFileds, "array"));
                        }
                        // else {
                        // list.add(objMap.get("enum"));
                        // list.add(objMap.get("maxLength"));
                        // list.add(objMap.get("minLength"));
                        // list.add(objMap.get("pattern"));
                        // }
                    }
                } catch (Exception e) {
                    // System.out.println("Mapper Else If |--> " + e.getMessage());
                }
                // return list;
            }

        } catch (Exception e) {
            // System.out.println("Exception at Formating Table | " + e.getMessage());
        }
        return tableData;
    }

    @SuppressWarnings("unchecked")
    public List<Object> formatTableData2(Object obj, int pId, List<Object> requiredFileds, String type) {

        List<Object> tableData = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> objMap = mapper.convertValue(obj, Map.class);
            if (type.equals("object")) {
                if (!Objects.isNull(objMap)) {
                    objMap.forEach((key, value) -> {

                        Map<String, Object> table = new HashMap<>();
                        try {
                            table.put("Level", ++id);
                            table.put("parentId", pId);
                            table.put("parameter", key);
                            table.put("Mendate", requiredFileds.contains(key));
                            table.put("Description",
                                    Objects.isNull(mapper.convertValue(value, Map.class).get("description")) ? "-"
                                            : mapper.convertValue(value, Map.class).get("description"));
                        } catch (Exception e) {
                            System.out.println("Mapper Initialization |--> " + e.getMessage());

                        }
                        try {
                            if (mapper.convertValue(value, Map.class).get("type").equals("object")) {
                                tableData.addAll(
                                        formatTableData2(mapper.convertValue(value, Map.class).get("properties"), id,
                                                requiredFileds, "object"));
                            } else if (mapper.convertValue(value, Map.class).get("type").equals("array")) {
                                tableData.addAll(formatTableData2(mapper.convertValue(value, Map.class).get("items"),
                                        id, requiredFileds, "array"));
                            } else {
                                table.put("Type", mapper.convertValue(value, Map.class).get("type"));
                            }
                        } catch (Exception e) {
                            System.out.println("Mapper If line: 283 |--> " + e.getMessage());
                        }
                        tableData.add(table);
                    });
                }

            } else if (type.equals("array")) {

                try {
                    if (!Objects.isNull(objMap) && !Objects.isNull(objMap.get("type"))) {
                        if (objMap.get("type").equals("object") && !Objects.isNull(objMap.get("properties"))) {
                            tableData.addAll(formatTableData2(objMap.get("properties"), id, requiredFileds, "object"));
                        } else if (objMap.get("type").equals("array")) {
                            tableData.addAll(formatTableData2(objMap.get("items"), id, requiredFileds, "array"));
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Mapper Else If |--> " + e.getMessage());
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
        Map<String, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        Object obj = store.getPaths().get("/mplace/selleritems").getPost().getRequestBody().getContent().get("*/*")
                .getSchema();
        Map<String, Object> objMap = mapper.convertValue(obj, Map.class);

        // Object itemVal = objMap.get("items");
        // Map<String, Object> item = mapper.convertValue(itemVal, Map.class);
        // Object propVal = item.get("properties");
        // Map<String, Object> property = mapper.convertValue(propVal, Map.class);
        // property.forEach((x, y)->{
        // System.out.println(x + " " + y.get("maxLength"));
        // });
        // objMap.forEach((x, y) -> {
        // if (x.equals("items")) {

        // Map<String, Object> item = mapper.convertValue(y, Map.class);
        // item.forEach((a, b) -> {
        // if (a.equals("properties")) {

        // Map<String, Object> property = mapper.convertValue(b, Map.class);
        // property.forEach((prop, propVal) -> {
        // // Map<String, String> propValMap = mapper.convertValue(propVal, Map.class);
        // // System.out.println(propVal);
        // // propValMap.forEach((key, value) -> {
        // // System.out.println("-->" + key);
        // // });

        // // propVal.get("maxLength");

        // });

        // }

        // });

        // }
        // });

        return nullFieldFilter(store.getPaths().get("/mplace/selleritems").getPost().getRequestBody().getContent()
                .get("*/*").getSchema());
        // return
        // formatTableData2(nullFieldFilter(store.getPaths().get("/mplace/selleritems").getPost().getRequestBody()
        // .getContent().get("*/*").getSchema()), id, new ArrayList<>(), "array");
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
