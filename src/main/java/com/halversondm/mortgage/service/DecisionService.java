package com.halversondm.mortgage.service;

import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.server.api.model.KieServiceResponse;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.RuleServicesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class DecisionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DecisionService.class);

    @Autowired
    private KieServicesClient kieServicesClient;

    RuleServicesClient rulesClient;

    @PostConstruct
    public void initialize() {
        rulesClient = kieServicesClient.getServicesClient(RuleServicesClient.class);
    }

    public List<Object> executeCommands(List<Object> facts, String container) {
        LOGGER.debug("== Sending commands to the server ==");
        List<Object> results = new ArrayList<>();
        KieCommands commandsFactory = KieServices.Factory.get().getCommands();
        Command<?> batchCommand = commandsFactory.newBatchExecution(buildCommands(commandsFactory, facts));
        ServiceResponse<ExecutionResults> executeResponse = rulesClient.executeCommandsWithResults(container, batchCommand);
        if (executeResponse.getType() == KieServiceResponse.ResponseType.SUCCESS) {
            LOGGER.debug("Commands executed with success! Response: ");
            for (String s : executeResponse.getResult().getIdentifiers()) {
                results.add(executeResponse.getResult().getValue(s));
            }
        } else {
            LOGGER.error("Error executing rules. Message: ");
            LOGGER.error(executeResponse.getMsg());
        }
        return results;
    }

    List<Command<?>> buildCommands(KieCommands commandsFactory, List<Object> facts) {
        List<Command<?>> commands = new ArrayList<>();
        facts.forEach(s ->
                commands.add(commandsFactory.newInsert(s, UUID.randomUUID().toString()))
        );
        commands.add(commandsFactory.newFireAllRules());
        commands.add(commandsFactory.newDispose());
        return commands;
    }

}
