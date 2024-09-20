package org.example.task;

import org.example.ImportConductorProperties;

import java.util.Map;

@Component
@AllArgsConstructor
public class DynamicForkTask {
    private ImportConductorProperties importConductorProperties;

    /**
     * Generate dynamic subworkflow task based on module
     * @param module
     * @return WorkflowTask
     */
    public WorkflowTask dynamicModuleSubworkflow(FreshsalesModule module) {
        String name = String.join("_", "dynamic", module.getName().toLowerCase(), "import_subworkflow");
        WorkflowTask workflowTask = new WorkflowTask();
        workflowTask.setName(name);
        workflowTask.setTaskReferenceName(name + "_ref");
        workflowTask.setWorkflowTaskType(TaskType.FORK_JOIN_DYNAMIC);
        workflowTask.setInputParameters(Map.ofEntries(mapJsonPath(GENERATE_DYNAMIC_TASKS_OUTPUT, "dynamicTasks"),
                mapJsonPath(GENERATE_DYNAMIC_TASKS_OUTPUT, "dynamicTasksInput")));
        workflowTask.setDynamicForkTasksParam("dynamicTasks");
        workflowTask.setDynamicForkTasksInputParamName("dynamicTasksInput");
        workflowTask.setStartDelay(importConductorProperties.getTaskDelay());
        workflowTask.setOptional(false);
        workflowTask.setAsyncComplete(false);
        return workflowTask;
    }
}
