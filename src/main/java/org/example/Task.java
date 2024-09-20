package org.example;

public class Task {
    UPLOAD_TO_UCR,
    WAIT_FOR_UCR_EVENT,
    FETCH_FROM_UCR,
    UPDATE_FSA_DB,
    UPLOAD_SEARCH,
    SEARCH_JOIN,
    UPLOAD_ELASTIC_SEARCH,
    UPLOAD_SEARCH_SERVICE,
    PARSE_AND_SPLIT_CSV,
    SEND_RESULT,
    SEND_REPORT,
    SEND_TO_RAILS_APP,
    GENERATE_DYNAMIC_FORK,
    DYNAMIC_SUBWORKFLOW_JOIN,
    SEND_JOIN;

    public String toLowerCase() {
        return this.toString().toLowerCase();
    }
}
