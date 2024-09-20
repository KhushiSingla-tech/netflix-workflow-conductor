package org.example.util;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConductorUtil{
    public static final String PARSE_SPLIT_CSV_OUTPUT = "${parse_and_split_csv_ref.output.response.body.%s}";
    public static final String WORKFLOW_INPUT = "${workflow.input.%s}";
    public static final String UPLOAD_TO_UCR_OUTPUT = "${upload_to_ucr_ref.output.response.body.%s}";
    public static final String GENERATE_DYNAMIC_TASKS_OUTPUT = "${generate_dynamic_fork.output.%s}";

    public static Map.Entry<String, Object> mapJsonPath(String path, String field) {
        return Map.entry(field, String.format(path, field));
    }
}

