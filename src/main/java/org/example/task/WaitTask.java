package org.example.task;

import org.example.ImportConductorProperties;

import java.util.Map;

@Component
@AllArgsConstructor
public class WaitTask{
    private ImportConductorProperties importConductorConfiguration;

    /**
     * Generates Wait Task
     * @return WorkflowTask
     */
    public WorkflowTask waitForUcrEvent(String name) {
        WorkflowTask workflowTask = new WorkflowTask();
        workflowTask.setName(name);
        workflowTask.setTaskReferenceName(name + "_ref");
        workflowTask.setWorkflowTaskType(TaskType.WAIT);
        workflowTask.setInputParameters(Map.of("duration", importConductorConfiguration.getWaitTime()));
        workflowTask.setStartDelay(importConductorConfiguration.getTaskDelay());
        workflowTask.setOptional(false);
        workflowTask.setAsyncComplete(false);
        return workflowTask;
    }
}

