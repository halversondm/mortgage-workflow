package com.halversondm.mortgage.controller;

import com.halversondm.mortgage.service.DecisionService;
import mortgages.mortgages.Applicant;
import mortgages.mortgages.Bankruptcy;
import mortgages.mortgages.IncomeSource;
import mortgages.mortgages.LoanApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value = "/api")
public class MortgageWorkflowController {

    @Autowired
    DecisionService decisionService;

    // { name: "Dan Halverson", age: "40", creditRating: "AA", incomeSource: "", amountOnDeposit: "", incomeAmount: "", amountOwed: "", yearOfOccurrence: "", amountToBorrow: "", lengthInYears: "" }

    @RequestMapping(value = "/submit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> submit(@RequestBody Map<String, String> request) {
        Applicant applicant = new Applicant();
        applicant.setName(request.get("name"));
        applicant.setAge(safeConversion(request.get("age")));
        applicant.setCreditRating(request.get("creditRating"));
        applicant.setApplicationDate(new Date());

        IncomeSource incomeSource = new IncomeSource();
        incomeSource.setType(request.get("incomeSource"));
        incomeSource.setAmount(safeConversion(request.get("incomeAmount")));

        Bankruptcy bankruptcy = new Bankruptcy();
        bankruptcy.setAmountOwed(safeConversion(request.get("amountOwed")));
        bankruptcy.setYearOfOccurrence(safeConversion(request.get("yearOfOccurrence")));

        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setDeposit(safeConversion(request.get("amountOnDeposit")));
        loanApplication.setLengthYears(safeConversion(request.get("lengthInYears")));
        loanApplication.setAmount(safeConversion(request.get("amountToBorrow")));

        List<Object> results = decisionService.executeCommands(Arrays.asList(applicant, incomeSource, bankruptcy, loanApplication), "mortgages_1.0.0-SNAPSHOT");
        Map<String, Object> response = new HashMap<>();
        results.forEach(result -> {
            if (result instanceof LoanApplication) {
                LoanApplication loanApp = (LoanApplication) result;
                response.put("approved", loanApp.getApproved());
                response.put("approvedRate", loanApp.getApprovedRate());
                response.put("explanation", loanApp.getExplanation());
                response.put("insuranceCost", loanApp.getInsuranceCost());
            }
        });
        return response;
    }

    Integer safeConversion(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        return Integer.valueOf(source);
    }
}
