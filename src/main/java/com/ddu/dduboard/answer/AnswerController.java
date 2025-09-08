package com.ddu.dduboard.answer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ddu.dduboard.question.Question;
import com.ddu.dduboard.question.QuestionService;

import jakarta.validation.Valid;

@RequestMapping(value = "/answer")
@Controller
public class AnswerController {
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private AnswerService answerService;
	
	@PostMapping(value = "/create/{id}")
	public String createAnswer(Model model, @PathVariable("id") Integer id,@Valid AnswerForm answerForm, BindingResult bindingResult) {
		Question question = questionService.getQuestion(id);
		if (bindingResult.hasErrors()) {
			model.addAttribute("question", question);
			return "question_detail";
		}
		answerService.create(question, answerForm.getContent());
		
		return String.format("redirect:/question/detail/%s", id);
	}
}

