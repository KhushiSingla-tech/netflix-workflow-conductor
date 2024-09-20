package org.example.task;

import org.example.ImportConductorProperties;

import java.util.Map;

@Component
@AllArgsConstructor
public class SimpleTask{
    private ImportConductorProperties importConductorProperties;

    /**
     * Generates Simple Task
     * @return WorkflowTask
     */
    public WorkflowTask generateDynamicFork(String name) {
        WorkflowTask workflowTask = new WorkflowTask();
        workflowTask.setName(name);
        workflowTask.setTaskReferenceName(name);
        workflowTask.setWorkflowTaskType(TaskType.SIMPLE);
        workflowTask.setInputParameters(Map.ofEntries(mapJsonPath(PARSE_SPLIT_CSV_OUTPUT, "s3Locations"),
                mapJsonPath(WORKFLOW_INPUT, "module")));
        workflowTask.setStartDelay(importConductorProperties.getTaskDelay());
        workflowTask.setOptional(false);
        workflowTask.setAsyncComplete(false);
        return workflowTask;
    }
}
