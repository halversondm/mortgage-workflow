package com.halversondm.mortgage;

import com.halversondm.mortgage.config.KieServerProperties;
import com.halversondm.mortgage.service.DecisionService;
import mortgages.mortgages.Applicant;
import mortgages.mortgages.Bankruptcy;
import mortgages.mortgages.IncomeSource;
import mortgages.mortgages.LoanApplication;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MortgageWorkflowApplicationTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(MortgageWorkflowApplicationTests.class);

	@Autowired
	DecisionService decisionService;

	@Autowired
	KieServerProperties kieServerProperties;

	@Test
	public void contextLoads() {
		List<Object> results = decisionService.executeCommands(facts(), "mortgages_1.0.0-SNAPSHOT");

		results.forEach(factOrResult -> {
			if (factOrResult instanceof LoanApplication) {
				LoanApplication loanApplication = (LoanApplication) factOrResult;
				LOGGER.info("{}", ReflectionToStringBuilder.toString(loanApplication));
			}
			if (factOrResult instanceof Applicant) {
				Applicant applicant = (Applicant) factOrResult;
				LOGGER.info("{}", ReflectionToStringBuilder.toString(applicant));
			}
		});
	}

	List<Object> facts() {
		List<Object> facts = new ArrayList<>();
		Applicant applicant = new Applicant();
		applicant.setAge(40);
		applicant.setApplicationDate(new Date());
		applicant.setName("Dan Halverson");
		applicant.setCreditRating("AA");
		facts.add(applicant);
		Bankruptcy bankruptcy = new Bankruptcy();
		facts.add(bankruptcy);
		IncomeSource incomeSource = new IncomeSource();
		incomeSource.setAmount(100000);
		incomeSource.setType("Asset");
		facts.add(incomeSource);
		LoanApplication loanApplication = new LoanApplication();
		loanApplication.setAmount(200000);
		loanApplication.setLengthYears(30);
		loanApplication.setDeposit(19999);
		facts.add(loanApplication);
		return facts;
	}

}
