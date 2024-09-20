package org.example.task;

import org.example.ImportConductorProperties;

import java.util.List;

@Component
@AllArgsConstructor
public class ForkJoinTask{
    private HttpTask httpTask;
    private ImportConductorProperties importConductorProperties;

    /**
     * Generates fork task for uploadElasticSearch and uploadSearchService http tasks
     * @param elasticSearchUrl
     * @param searchServiceUrl
     * @return WorkflowTask
     */
    public WorkflowTask uploadSearch(String elasticSearchName, String elasticSearchUrl,
                                     String searchServiceName, String searchServiceUrl) {
        String name = Task.UPLOAD_SEARCH.toLowerCase();
        WorkflowTask workflowTask = new WorkflowTask();
        workflowTask.setName(name);
        workflowTask.setTaskReferenceName(name + "_ref");
        workflowTask.setForkTasks(List.of(List.of(httpTask.uploadElasticSearch(elasticSearchName, elasticSearchUrl)),
                List.of(httpTask.uploadSearchService(searchServiceName, searchServiceUrl))));
        workflowTask.setWorkflowTaskType(TaskType.FORK_JOIN);
        workflowTask.setJoinOn(List.of(httpTask.uploadElasticSearch(elasticSearchName, elasticSearchUrl)
                .getTaskReferenceName(), httpTask.uploadSearchService(searchServiceName, searchServiceUrl)
                .getTaskReferenceName()));
        workflowTask.setStartDelay(importConductorProperties.getTaskDelay());
        workflowTask.setOptional(false);
        workflowTask.setAsyncComplete(false);
        return workflowTask;
    }

    /**
     * Generates fork task for sendReport and sendToRailsApp http tasks
     * @param sendReportUrl
     * @param sendToRailsAppUrl
     * @return WorkflowTask
     */
    public WorkflowTask sendResult(String sendReportName, String sendReportUrl,
                                   String sendToRailsAppName, String sendToRailsAppUrl) {
        String name = Task.SEND_RESULT.toLowerCase();
        WorkflowTask workflowTask = new WorkflowTask();
        workflowTask.setName(name);
        workflowTask.setTaskReferenceName(name + "_ref");
        workflowTask.setForkTasks(List.of(
                List.of(httpTask.sendReport(sendReportName, sendReportUrl)),
                List.of(httpTask.sendToRailsApp(sendToRailsAppName, sendToRailsAppUrl))));
        workflowTask.setWorkflowTaskType(TaskType.FORK_JOIN);
        workflowTask.setJoinOn(List.of(httpTask.sendReport(sendReportName, sendReportUrl).getTaskReferenceName(),
                httpTask.sendToRailsApp(sendToRailsAppName, sendToRailsAppUrl).getTaskReferenceName()));
        workflowTask.setStartDelay(importConductorProperties.getTaskDelay());
        workflowTask.setOptional(false);
        workflowTask.setAsyncComplete(false);
        return workflowTask;
    }
}
