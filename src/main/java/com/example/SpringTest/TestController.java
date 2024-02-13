package com.example.SpringTest;

import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class TestController {
	
	private gRecordMapper mapper;
	
	public TestController(gRecordMapper mapper) {
		super();
		this.mapper = mapper;
	}
	//홈페이지
	@RequestMapping("/homepage")
	public String homepage() {

		System.out.println("\n\n\thomepage Connected!\n\n");
		return "index.html";
		//return "redirect:/write";
	}
	//글쓰기
	@RequestMapping("/write")
	public String write() {

		System.out.println("\n\n\twrite Connected!\n\n");
		return "write.html";
	}
	//글쓰기저장
	@RequestMapping(value = "/write_db", method = RequestMethod.POST)
	public String write_db(Model model, @RequestParam String title, @RequestParam String content,
			@RequestParam String writer) {
		
		System.out.printf("\n\n\t[%s][%s][%s]\n\n", title, content, writer);

		model.addAttribute("title", title);
		model.addAttribute("content", content);
		model.addAttribute("writer", writer);
		
		mapper.writeRecord(title, content, writer);
		System.out.println("\n\n\tprint_record Connected!\n\n");
		return "write_db";
	}
	//가져올 글번호 입력
	@RequestMapping("/read")
	public String read() {

		System.out.println("\n\n\tread_db Connected!\n\n");
		return "read.html";
	}
	//글 1개 가져오기
	//@RequestMapping(value = "/read_db", method = RequestMethod.POST)
	@RequestMapping(value = "/read_db", method = RequestMethod.GET)
	public String read_db(Model model, @RequestParam int no) {
		
		System.out.printf("\n\n\t[%d]\n\n", no);

		gRecord tmp = mapper.getRecord(no);
		System.out.println("\n\n\tread_db Connected!\n\n");
		
		model.addAttribute("_no", tmp.getNo());
		model.addAttribute("_title", tmp.getTitle());
		model.addAttribute("_content", tmp.getContent());
		model.addAttribute("_writer", tmp.getWriter());
		model.addAttribute("_date", tmp.getDate());
		model.addAttribute("_view", tmp.getView());
		return "read_db";
	}
	
	@RequestMapping("/list")
	public String list(Model model) {

		ArrayList<gRecord> tmp = mapper.getAllRecords();
		//리턴받은것들을 동적홈페이지에 전달하는 부분을 코딩해야함
		model.addAttribute("html_data_pack", tmp);
		System.out.println("\n\n\tlist Connected!\n\n");
		return "list";
	}
	
//	@RequestMapping("/homepage/{no}")
//	public String homepage2(Model model, @PathVariable("no") String no) {
//
//		System.out.println("\n\n\t[homepage2] Connected!\n\n");
//		System.out.printf("[%s]", no);
//		model.addAttribute("data", no);
//		return "test";
//	}
	@RequestMapping("/login")
	public String login() {

		System.out.println("\n\n\tlogin Connected!\n\n");
		return "login.html";
	}
	
	@RequestMapping(value = "/login_db", method = RequestMethod.POST)
	public String login_db(Model model, @RequestParam String m_id, @RequestParam String m_pw, 
			HttpServletRequest request) {
		
		//m_pw 는 현재 원래암호
		//이걸 md5() 로 암호화시켜서 다시 저장한다.
		//그걸 맵퍼한테 전달한다.
		//자바에서 문자열을 md5화 시키는것을 찾아서 암호화시킬것
		md5 md5 = new md5();
		String md5_m_pw = md5.testMD5(m_pw);
		
		gUserInfo tmp = null;
		tmp = mapper.getUserInfo(m_id, md5_m_pw);
		//검색 결과가 없으면 에러메시지를 로그로 출력해준다. (system.out...)
		if(tmp==null) {System.out.println("error");}
		//만약 아니라면 로그인된 사용자의 정보를 세션에 저장해준다.
		javax.servlet.http.HttpSession session = request.getSession(true);//있으면 세션반환, 없으면 null
		session.setAttribute("loggedInUser", tmp);
		model.addAttribute("html_login_info", tmp);
		
		System.out.println("\n\n\tlogin_result Connected!\n\n");
		return "login_result";
	}
	
	// 세션체크용
	@RequestMapping("/CheckMySession")
	public String CheckMySession(Model model, HttpServletRequest request) {
		
		HttpSession session = request.getSession(true);//있으면 세션반환, 없으면 null
		gUserInfo loggedInUser = (gUserInfo) session.getAttribute("loggedInUser");
		model.addAttribute("SessionInfo", loggedInUser);
		return "CheckMySession";
	}
	
	@RequestMapping("/DestoryMySession")
	public String DestoryMySession(Model model, HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);//있으면 세션반환, 없으면 null
		if(session!=null) {
			session.invalidate();
			//Thread.sleep(1000);
			return "redirect:/homepage";
		}
		return "DestoryMySession";
	}

}
