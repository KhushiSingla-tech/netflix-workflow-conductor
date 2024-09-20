package org.example;

@Component
@AllArgsConstructor
public class BulkImportWorkflow{
    private WorkflowTaskDefinition moduleWorkflowTask;
    private static final String OWNER_EMAIL = "workflow.owner@freshworks.com";

    /**
     * Generates final import bulk workflow for given account id and module
     * @param metadataClient
     * @param accountId
     * @param module
     * @return WorkflowDef
     */
    public WorkflowDef createBulkWorkflow(MetadataClient metadataClient, Long accountId, FreshsalesModule module) {
        WorkflowDef bulkWorkflow = new WorkflowDef();
        bulkWorkflow.setName(String.join("_","import_bulk_workflow", accountId.toString(),
                module.getName().toLowerCase()));
        bulkWorkflow.setDescription(String.join("_","import_bulk_workflow", accountId.toString(),
                module.getName().toLowerCase()));
        bulkWorkflow.setVersion(1);
        bulkWorkflow.setTasks(moduleWorkflowTask.getBulkWorkflowTasks(module));
        bulkWorkflow.setOwnerEmail(OWNER_EMAIL);
        bulkWorkflow.setTimeoutPolicy(WorkflowDef.TimeoutPolicy.ALERT_ONLY);
        bulkWorkflow.setRestartable(true);
        metadataClient.registerWorkflowDef(bulkWorkflow);
        return bulkWorkflow;
    }

    /**
     * Generates Workflow based on given module
     * @param metadataClient
     * @param module
     * @return WorkflowDef
     */
    public WorkflowDef createModuleWorkflow(MetadataClient metadataClient, FreshsalesModule module) {
        WorkflowDef moduleWorkflow = new WorkflowDef();
        moduleWorkflow.setName(String.join("_","import", module.getName().toLowerCase(), "workflow"));
        moduleWorkflow.setDescription(String.join("_","Import", module.getName().toLowerCase(),
                "workflow"));
        moduleWorkflow.setVersion(1);
        moduleWorkflow.setTasks(moduleWorkflowTask.getModuleWorkflowTasks(module));
        moduleWorkflow.setOwnerEmail(OWNER_EMAIL);
        moduleWorkflow.setTimeoutPolicy(WorkflowDef.TimeoutPolicy.ALERT_ONLY);
        moduleWorkflow.setRestartable(true);
        metadataClient.registerWorkflowDef(moduleWorkflow);
        return moduleWorkflow;
    }
}
