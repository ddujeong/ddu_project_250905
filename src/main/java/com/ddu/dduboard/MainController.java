package com.ddu.dduboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping(value = "/")
	public String root() {
		// url 기본 루트 url 설정 (-> 서버 start 시 리스트로 이동)
		return "redirect:/question/list";
	}
}
