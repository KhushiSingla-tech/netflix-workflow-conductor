package org.example.task;

import org.example.ImportConductorProperties;

@Component
@AllArgsConstructor
public class JoinTask{
    private ImportConductorProperties importConductorProperties;

    /**
     * Generates join task to added after fork join tasks
     * @param name
     * @return WorkflowTask
     */
    public WorkflowTask joinForkTask(String name) {
        WorkflowTask workflowTask = new WorkflowTask();
        workflowTask.setName(name);
        workflowTask.setTaskReferenceName(name + "_ref");
        workflowTask.setWorkflowTaskType(TaskType.JOIN);
        workflowTask.setStartDelay(importConductorProperties.getTaskDelay());
        workflowTask.setOptional(false);
        workflowTask.setAsyncComplete(false);
        return workflowTask;
    }
}
