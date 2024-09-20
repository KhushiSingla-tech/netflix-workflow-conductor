package org.example;

import org.example.task.DynamicForkTask;
import org.example.task.HttpTask;
import org.example.task.JoinTask;
import org.example.task.SimpleTask;
import org.example.task.WaitTask;

import java.util.List;

@Component
@AllArgsConstructor
public class WorkflowTaskDefinition{
    private HttpTask httpTask;
    private JoinTask joinTask;
    private ForkJoinTask forkJoinTask;
    private WaitTask waitTask;
    private SimpleTask simpleTask;
    private DynamicForkTask dynamicForkTask;
    private ImportConductorProperties importConductorProperties;

    /**
     * Returns list of tasks for import bulk workflow
     * @param module
     * @return List of WorkflowTask
     */
    public List<WorkflowTask> getBulkWorkflowTasks(FreshsalesModule module) {
        return List.of(httpTask.parseAndSplitCsv(Task.PARSE_AND_SPLIT_CSV.toLowerCase(),
                        importConductorProperties.getParseAndSplitCsvUrl()),
                simpleTask.generateDynamicFork(Task.GENERATE_DYNAMIC_FORK.toLowerCase()),
                dynamicForkTask.dynamicModuleSubworkflow(module),
                joinTask.joinForkTask(Task.DYNAMIC_SUBWORKFLOW_JOIN.toLowerCase()),
                forkJoinTask.sendResult(Task.SEND_REPORT.toLowerCase(),
                        importConductorProperties.getSendReportUrl(), Task.SEND_TO_RAILS_APP.toLowerCase(),
                        importConductorProperties.getSendToRailsAppUrl()),
                joinTask.joinForkTask(Task.SEND_JOIN.toLowerCase()));
    }

    /**
     * Returns list of tasks as per module
     * @param module
     * @return List of WorkflowTask
     */
    public List<WorkflowTask> getModuleWorkflowTasks(FreshsalesModule module) {
        switch (module) {
            case CONTACT:
                return List.of(
                        httpTask.uploadToUcr(
                                Task.UPLOAD_TO_UCR.toLowerCase(), importConductorProperties.getContactUrls()
                                        .get(Task.UPLOAD_TO_UCR.toLowerCase())),
                        waitTask.waitForUcrEvent(Task.WAIT_FOR_UCR_EVENT.toLowerCase()),
                        httpTask.fetchFromUcr(
                                Task.FETCH_FROM_UCR.toLowerCase(), importConductorProperties.getContactUrls()
                                        .get(Task.FETCH_FROM_UCR.toLowerCase())),
                        httpTask.updateFsaDb(
                                Task.UPDATE_FSA_DB.toLowerCase(), importConductorProperties.getContactUrls()
                                        .get(Task.UPDATE_FSA_DB.toLowerCase())),
                        forkJoinTask.uploadSearch(
                                Task.UPLOAD_ELASTIC_SEARCH.toLowerCase(), importConductorProperties.getContactUrls().get(
                                        Task.UPLOAD_ELASTIC_SEARCH.toLowerCase()),
                                Task.UPLOAD_SEARCH_SERVICE.toLowerCase(), importConductorProperties.getContactUrls().get(
                                        Task.UPLOAD_SEARCH_SERVICE.toLowerCase())),
                        joinTask.joinForkTask(Task.SEARCH_JOIN.toLowerCase()));
            case ACCOUNT:
                return List.of(
                        httpTask.uploadToUcr(
                                Task.UPLOAD_TO_UCR.toLowerCase(), importConductorProperties.getAccountUrls()
                                        .get(Task.UPLOAD_TO_UCR.toLowerCase())),
                        waitTask.waitForUcrEvent(Task.WAIT_FOR_UCR_EVENT.toLowerCase()),
                        httpTask.fetchFromUcr(
                                Task.FETCH_FROM_UCR.toLowerCase(), importConductorProperties.getAccountUrls()
                                        .get(Task.FETCH_FROM_UCR.toLowerCase())),
                        httpTask.updateFsaDb(
                                Task.UPDATE_FSA_DB.toLowerCase(), importConductorProperties.getAccountUrls()
                                        .get(Task.UPDATE_FSA_DB.toLowerCase())),
                        forkJoinTask.uploadSearch(
                                Task.UPLOAD_ELASTIC_SEARCH.toLowerCase(), importConductorProperties.getAccountUrls()
                                        .get(Task.UPLOAD_ELASTIC_SEARCH.toLowerCase()),
                                Task.UPLOAD_SEARCH_SERVICE.toLowerCase(), importConductorProperties.getAccountUrls()
                                        .get(Task.UPLOAD_SEARCH_SERVICE.toLowerCase())),
                        joinTask.joinForkTask(Task.SEARCH_JOIN.toLowerCase()));
            default:
                throw new IllegalArgumentException("Module name not found");
        }
    }
}
