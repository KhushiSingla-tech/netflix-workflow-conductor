package org.example;

import java.util.Map;

@ConfigurationProperties(prefix = "fsa-import-service.conductor")
@Component
@Getter
@Setter
public class ImportConductorProperties{
    private String waitTime;
    private Integer httpReadTimeOut;
    private Integer taskRetryCount;
    private Integer taskDelay;
    private Map<String, String> contactUrls;
    private Map<String, String> accountUrls;
    private Map<String, String> dealUrls;
    private String parseAndSplitCsvUrl;
    private String sendReportUrl;
    private String sendToRailsAppUrl;
}