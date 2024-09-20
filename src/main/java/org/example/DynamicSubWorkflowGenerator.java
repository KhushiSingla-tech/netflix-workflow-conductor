package org.example;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class DynamicSubWorkflowGenerator
        implements Worker {
    private String taskDefName;

    @Override
    public String getTaskDefName() {
        return taskDefName;
    }

    /**
     * Method will be called when generateDynamicFork simple task is invoked in workflow.
     * To invoke, worker is assigned to the task with the required task ref name
     * and then using Task Runner Configuration class worker is assigned thread and initiated.
     * Example: -
     *      Worker worker1 = new DynamicSubWorkflowGenerator("generateDynamicFork");
     *         TaskRunnerConfigurer configurer =
     *                 new TaskRunnerConfigurer.Builder(taskClient, List.of(worker1))
     *                         .withThreadCount(threadCount)
     *                         .build();
     *         configurer.init();
     * @param task
     * @return TaskResult
     */
    @Override
    public TaskResult execute(Task task) {
        TaskResult result = new TaskResult(task);
        try {
            List<String> s3Locations = Optional.ofNullable(task.getInputData())
                    .map(in -> in.get("s3Locations")).map(String::valueOf).map(List::of)
                    .orElseThrow(() ->  new IllegalArgumentException("S3 locations not found in task input"));
            String module = Optional.ofNullable(task.getInputData().get("module")).map(String::valueOf)
                    .orElseThrow(() -> new IllegalArgumentException("Module name not found in task input"));
            List<WorkflowTask> dynamicTasks = new ArrayList<>();
            Map<String, Object> dynamicTasksInput = new HashMap<>();
            for (int i = 0; i < s3Locations.size(); i++) {
                String name = String.join("_", module.toLowerCase(), "import_subworkflow", String.valueOf(i));
                String referenceName = String.join("_", name, "ref");
                WorkflowTask dynamicTask = new WorkflowTask();
                dynamicTask.setName(name);
                dynamicTask.setTaskReferenceName(referenceName);
                dynamicTask.setWorkflowTaskType(TaskType.SUB_WORKFLOW);
                SubWorkflowParams subWorkflowParams = new SubWorkflowParams();
                subWorkflowParams.setName(String.join("_", "import", module.toLowerCase(), "workflow"));
                dynamicTask.setSubWorkflowParam(subWorkflowParams);
                dynamicTasks.add(dynamicTask);
                dynamicTasksInput.put(referenceName, Map.of("s3Location", s3Locations.get(i)));
            }
            result.setStatus(TaskResult.Status.COMPLETED);
            Map<String, Object> outputData = Map.of("dynamicTasks", dynamicTasks,
                    "dynamicTasksInput", dynamicTasksInput);
            result.setOutputData(outputData);
        }  catch (Exception e) {
            result.setStatus(TaskResult.Status.FAILED);
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw, true);
            log.error(pw.toString());
            result.log(sw.getBuffer().toString());
        }
        return result;
    }
}
