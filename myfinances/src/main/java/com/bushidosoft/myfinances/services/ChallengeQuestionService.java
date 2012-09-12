package com.bushidosoft.myfinances.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bushidosoft.myfinances.dal.ChallengeQuestionRepository;
import com.bushidosoft.myfinances.entities.BankLogin;
import com.bushidosoft.myfinances.entities.ChallengeQuestion;

@Service
public class ChallengeQuestionService {
	
	@Autowired(required=true)
	private ChallengeQuestionRepository challengeQuestionRepository;
	
	public List<ChallengeQuestion> findChallengeQuestions(BankLogin bankLogin) {
		return challengeQuestionRepository.listByAttribute("bankLogin.id", bankLogin.getId());
	}

}
