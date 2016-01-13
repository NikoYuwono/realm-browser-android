package com.nikoyuwono.realmbrowser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmFieldType;
import io.realm.RealmObject;
import io.realm.RealmObjectSchema;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by nyuwono on 12/2/15.
 */
public class HtmlBuilder {

    public static final String STRUCTURE_VIEW = "structure_view";
    public static final String CONTENT_VIEW = "content_view";

    private static final String CSS = "<style>\n" +
            "html,body,div,ul,li,a { margin: 0;padding: 0;}\n" +
            "h1 { font-size: 1.4em;padding: 10px; }\n" +

            ".container { margin:0;overflow: hidden;width:100vw;height:100vh; }\n" +
            ".sidebar { width: 30vw;height: 100vh;background-color:#0288D1;float:left; }\n" +
            ".mainDisplay { width: 70vw;height: 100vh;float:left;background-color:#E1F5FE;display:table; }\n" +

            ".sideTable { width: 100%;border-collapse: separate;border-spacing: 10px; }\n" +
            ".sideTable th { font-size: 1.4em;padding:5px;color:white; border-bottom: 1px solid white }\n" +
            ".sideTable td { font-size: 1.2em; padding:5px; color:white; }\n" +
            ".sideTable a:link, .sideTable a:visited, .sideTable a:active { color:white; text-decoration: none; }\n" +
            ".sideTable a:hover { color:#B3E5FC; text-decoration: none; }\n" +

            ".dataTable { border-collapse:collapse;background-color:white; margin:20px; }\n" +
            ".dataTable th { font-size: 1.1em;padding:5px;color:white;background-color:#0288D1;border: 1px solid #E0E0E0 }\n" +
            ".dataTable td { font-size: 1em; padding:5px; color:#616161;border: 1px solid #E0E0E0 }\n" +

            ".clear {clear:both;}\n" +
            ".navigation {width:60vw;margin:auto auto;display:table-cell;vertical-align:middle;text-align:center;}\n" +
            ".navigation ul {list-style:none;margin-left:5vw;position:relative; z-index:2;top:1px;display:table;border-left:1px solid #f5ab36;}\n" +
            ".navigation ul li {float:left;}\n" +
            ".navigation ul li a {background:#ffd89b;color:#222;display:block;padding:6px 15px;text-decoration:none;border-right:1px solid #f5ab36;border-top:1px solid #f5ab36;border-right:1px solid #f5ab36;}\n" +
            ".navigation ul li a.selected  {border-bottom:1px solid #fff;color:#344385;background:#fff;}\n" +
            ".content {width:60vw;height:80vh;margin-left:5vw;background:#fff;border:1px solid #f5ab36;z-index:1;text-align:center;padding:10px 0;}\n" +
            "</style>\n";
    private StringBuilder stringBuilder;
    private String selectedTableName;
    private String simpleTableName;

    public HtmlBuilder() {
        stringBuilder = new StringBuilder();
    }

    public String getSelectedTableName() {
        return selectedTableName;
    }

    public void setSelectedTableName(Class clazz) {
        this.selectedTableName = clazz.getName();
        this.simpleTableName = clazz.getSimpleName();
    }

    public void start() {
        stringBuilder.append("<html>")
                .append("<head>")
                .append(CSS)
                .append("</head>")
                .append("<body>")
                .append("<div class=\"container\">");
    }

    public String finish() {
        stringBuilder.append("</div>")
                .append("</body>")
                .append("</html>");
        return stringBuilder.toString();
    }

    public void showSidebar(Set<Class<? extends RealmObject>> modelClasses) {
        stringBuilder.append("<div class=\"sidebar\">")
                .append("<table class=\"sideTable\">")
                .append("<th>Tables</th>");
        for (Class<? extends RealmObject> modelClass : modelClasses) {
            stringBuilder.append("<tr><td><a href=\".?class_name=")
                    .append(modelClass.getName())
                    .append("\">")
                    .append(modelClass.getSimpleName())
                    .append("</a></td></tr>\n");
        }
        stringBuilder.append("</table>")
                .append("</div>");
    }

    public void startMainDisplay() {
        stringBuilder.append("<div class=\"mainDisplay\">");
    }

    public void closeMainDisplay() {
        stringBuilder.append("</div>");
    }

    public void startMainDisplayNavigation(String viewType) {
        String placeholder = "<a %1$s href=\"%2$s\">%3$s</a>";
        String structureStyleClass = "class=\"\"";
        String contentStyleClass = "class=\"\"";
        if (viewType != null && viewType.equals(CONTENT_VIEW)) {
            contentStyleClass = "class=\"selected\"";
        } else {
            structureStyleClass = "class=\"selected\"";
        }
        String structureLink = ".?class_name=" + selectedTableName + "&selected_view=" + STRUCTURE_VIEW;
        String contentLink = ".?class_name=" + selectedTableName + "&selected_view=" + CONTENT_VIEW;
        String structureTab = String.format(placeholder, structureStyleClass, structureLink, "Structure");
        String contentTab = String.format(placeholder, contentStyleClass, contentLink, "Content");
        stringBuilder.append("<div class=\"navigation\"><ul>")
                .append("<li>")
                .append(structureTab)
                .append("</li>")
                .append("<li>")
                .append(contentTab)
                .append("</li>")
                .append("</ul>")
                .append("<div class=\"clear\"></div>");
    }

    public void closeMainDisplayNavigation() {
        stringBuilder.append("</div>");
    }

    public void showEmptyView() {
        stringBuilder.append("<h1>Nothing to show here yet!<br/> Please select a Table. </h1>");
    }

    public void showTableStructure(DynamicRealm dynamicRealm) {
        RealmObjectSchema realmObjectSchema = dynamicRealm.getSchema().get(simpleTableName);
        Set<String> fieldNames = realmObjectSchema.getFieldNames();
        stringBuilder.append("<div class=\"content\">").append("<table class=\"dataTable\">")
                .append("<th>Column Name</th><th>Type</th>");
        for (String fieldName : fieldNames) {
            RealmFieldType realmFieldType = realmObjectSchema.getFieldType(fieldName);
            stringBuilder.append("<tr>")
                    .append("<td>").append(fieldName).append("</td>")
                    .append("<td>").append(realmFieldType.name()).append("</td>")
                    .append("</tr>");
        }
        stringBuilder.append("</table></div>");
    }

    public void showTableContent(DynamicRealm dynamicRealm, HashMap<String, String> queryMap) {
        try {
            RealmObjectSchema realmObjectSchema = dynamicRealm.getSchema().get(simpleTableName);

            Set<String> fieldNames = realmObjectSchema.getFieldNames();
            int columnCount = fieldNames.size();
            String[] fieldNameArray = fieldNames.toArray(new String[columnCount]);
            RealmFieldType[] realmFieldTypes = new RealmFieldType[columnCount];
            stringBuilder.append("<div class=\"content\">");
            showContentSearchBar(fieldNameArray);
            stringBuilder.append("<table class=\"dataTable\">");
            int index = 0;
            for (String fieldName : fieldNameArray) {
                realmFieldTypes[index] = realmObjectSchema.getFieldType(fieldName);
                stringBuilder.append("<th>").append(fieldName).append("</th>");
                index++;
            }

            RealmQuery<DynamicRealmObject> realmQuery = dynamicRealm.where(simpleTableName);
            for (Map.Entry<String, String> entry : queryMap.entrySet()) {
                String fieldName = entry.getKey();
                String value = entry.getValue();
                RealmFieldType realmFieldType = realmObjectSchema.getFieldType(fieldName);
                switch (realmFieldType) {
                    case INTEGER:
                        realmQuery.equalTo(fieldName, Long.parseLong(value));
                        break;
                    case BOOLEAN:
                        realmQuery.equalTo(fieldName, Boolean.parseBoolean(value));
                        break;
                    case FLOAT:
                        realmQuery.equalTo(fieldName, Float.parseFloat(value));
                        break;
                    case DOUBLE:
                        realmQuery.equalTo(fieldName, Double.parseDouble(value));
                        break;
                    case STRING:
                        realmQuery.equalTo(fieldName, value);
                        break;
                }
            }
            RealmResults<DynamicRealmObject> realmResults = realmQuery.findAll();
            int tableSize = realmResults.size();

            for (int i = 0; i < tableSize; i++) {
                stringBuilder.append("<tr>");
                DynamicRealmObject dynamicRealmObject = realmResults.get(i);
                for (int j = 0; j < columnCount; j++) {
                    String columnName = fieldNameArray[j];
                    String value = "";
                    switch (realmFieldTypes[j]) {
                        case INTEGER:
                            value = String.valueOf(dynamicRealmObject.getLong(columnName));
                            break;
                        case BOOLEAN:
                            value = String.valueOf(dynamicRealmObject.getBoolean(columnName));
                            break;
                        case FLOAT:
                            value = String.valueOf(dynamicRealmObject.getFloat(columnName));
                            break;
                        case DOUBLE:
                            value = String.valueOf(dynamicRealmObject.getDouble(columnName));
                            break;
                        case DATE:
                            value = dynamicRealmObject.getDate(columnName).toString();
                            break;
                        case STRING:
                            value = dynamicRealmObject.getString(columnName);
                            break;
                    }
                    stringBuilder.append("<td>").append(value).append("</td>");
                }
                stringBuilder.append("</tr>");
            }
            stringBuilder.append("</table>");
            stringBuilder.append("</div>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showContentSearchBar(String[] fieldNameArray) {
        stringBuilder.append("<form id=\"query_form\">")
                .append("<select name=\"field_name\" form=\"query_form\">");
        for (String fieldName : fieldNameArray) {
            String value = String.format("<option value=\"%s\">%s</option>", fieldName, fieldName);
            stringBuilder.append(value);
        }
        String classNameHiddenInput =
                String.format("<input type=\"hidden\" name=\"class_name\" value=\"%s\">", selectedTableName);
        String selectedViewHiddenInput =
                String.format("<input type=\"hidden\" name=\"selected_view\" value=\"%s\">", CONTENT_VIEW);
        stringBuilder.append("</select>")
                .append("<input type=\"text\" name=\"query_value\">")
                .append(classNameHiddenInput)
                .append(selectedViewHiddenInput)
                .append("</form>");
    }

}
