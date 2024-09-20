package org.example.task;

import org.example.ImportConductorProperties;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class HttpTask{
    private ImportConductorProperties importConductorProperties;
    private static final String S3_LOCATION_INPUT_PARAM = "s3Location";

    /**
     * Generates http task for parse and split csv
     * @param url
     * @return WorkflowTask
     */
    public WorkflowTask parseAndSplitCsv(String name, String url) {
        return getHttpTask(name + "_ref", name, url, HttpMethod.POST, getS3LocationInput());
    }

    /**
     * Generates http task for send report to client
     * @param url
     * @return WorkflowTask
     */
    public WorkflowTask sendReport(String name, String url) {
        return getHttpTask(name + "_ref", name, url, HttpMethod.POST, getS3LocationInput());
    }

    /**
     * Generates http task for updating fsa db
     * @param url
     * @return WorkflowTask
     */
    public WorkflowTask updateFsaDb(String name, String url) {
        return getHttpTask(name + "_ref", name, url, HttpMethod.POST, getS3LocationInput());
    }

    /**
     * Generates http task for send to rails application
     * @param url
     * @return WorkflowTask
     */
    public WorkflowTask sendToRailsApp(String name, String url) {
        return getHttpTask(name + "_ref", name, url, HttpMethod.POST, getS3LocationInput());
    }

    /**
     * Generates http task for upload records to ucr
     * @param url
     * @return WorkflowTask
     */
    public WorkflowTask uploadToUcr(String name, String url) {
        Map<String, Object> input = Map.ofEntries(mapJsonPath(WORKFLOW_INPUT, S3_LOCATION_INPUT_PARAM),
                Map.entry("subWorkflowId", "${workflow.workflowId}"));
        return getHttpTask(name + "_ref", name, url, HttpMethod.POST, input);
    }

    /**
     * Generates http task for fetch records from ucr
     * @param url
     * @return WorkflowTask
     */
    public WorkflowTask fetchFromUcr(String name, String url) {
        Map<String, Object> input = Map.ofEntries(mapJsonPath(WORKFLOW_INPUT, S3_LOCATION_INPUT_PARAM),
                mapJsonPath(UPLOAD_TO_UCR_OUTPUT, "jobId"));
        return getHttpTask(name + "_ref", name, url, HttpMethod.POST, input);
    }

    /**
     * Generates http task for upload to elastic search
     * @param url
     * @return WorkflowTask
     */
    public WorkflowTask uploadElasticSearch(String name, String url) {
        return getHttpTask(name + "_ref", name, url, HttpMethod.POST, getS3LocationInput());
    }

    /**
     * Generates http task for upload to search service
     * @param url
     * @return WorkflowTask
     */
    public WorkflowTask uploadSearchService(String name, String url) {
        return getHttpTask(name + "_ref", name, url, HttpMethod.POST, getS3LocationInput());
    }

    private Map<String, Object> inputHttpRequest(String url, HttpMethod httpMethod,
                                                 Map<String, Object> inputParameters) {
        Map<String, Object> input = new HashMap<>();
        input.put("method", httpMethod.toString());
        input.put("uri", url);
        input.put("body", inputParameters);
        input.put("readTimeOut", importConductorProperties.getHttpReadTimeOut());
        return input;
    }

    private WorkflowTask getHttpTask(String taskReferenceName, String name, String url,
                                     HttpMethod httpMethod, Map<String, Object> inputParameters) {
        WorkflowTask workflowTask = new WorkflowTask();
        workflowTask.setName(name);
        workflowTask.setTaskReferenceName(taskReferenceName);
        workflowTask.setWorkflowTaskType(TaskType.HTTP);
        workflowTask.setInputParameters(Map.of("http_request",
                inputHttpRequest(url, httpMethod, inputParameters)));
        workflowTask.setRetryCount(importConductorProperties.getTaskRetryCount());
        workflowTask.setStartDelay(importConductorProperties.getTaskDelay());
        workflowTask.setOptional(false);
        workflowTask.setAsyncComplete(false);
        return workflowTask;
    }

    private Map<String, Object> getS3LocationInput() {
        return Map.ofEntries(mapJsonPath(WORKFLOW_INPUT, S3_LOCATION_INPUT_PARAM));
    }
}
