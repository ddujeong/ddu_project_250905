package com.ddu.dduboard.answer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ddu.dduboard.question.Question;
import com.ddu.dduboard.question.QuestionService;

@RequestMapping(value = "/answer")
@Controller
public class AnswerController {
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private AnswerService answerService;
	
	@PostMapping(value = "/create/{id}")
	public String createAnswer(Model model, @PathVariable("id") Integer id, @RequestParam("content") String content) {
		Question question = questionService.getQuestion(id);

		answerService.create(question, content);
		
		return String.format("redirect:/question/detail/%s", id);
	}
}

