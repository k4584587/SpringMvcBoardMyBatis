package kojh.spring.board;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import kojh.db.beans.BoardBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BbsController
{
	@Autowired
	BoardService boardService;
			
	/*
	 Spring 3 이후부터는 Bean Validation에 대한 표준을 정의한 JSR-303 Spec.을 지원하고 있다. 
	 Validation은 선언적인 형태와 프로그램적인 형태로 구분할 수 있으며 Hibernate Validator와 같은 
	 JSR-303 Spec.을 구현한 구현체를 연계하여 처리된다.
	 
	 즉, Hibernate 추가해서 Validation 처리
	*/
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/show_write_form", method = RequestMethod.GET)
	public String show_write_form( Model model) 
	{
		logger.info("show_write_form called!!");
				
		// 객체를 전달해서 값을 얻어와야 함!!!		
		model.addAttribute("boardBeanObjToWrite", new BoardBean());
					
		return "writeBoard";		
	}
		
	//test---> OK!!
	/*
	@RequestMapping( value = "/DoWriteBoard" ,method = RequestMethod.POST)
	//public String PostWork( @ModelAttribute("boardBeanObjToWrite") BoardBean boardBeanObj, Model model) //OK!! 
	public String PostWork( BoardBean boardBeanObjToWrite, Model model) //OK Too!!
	{		
		logger.info("PostWork called!!");
		logger.info("memo=["+boardBeanObjToWrite.getMemo()+"]");
		return "PostWork";
	}
	*/
	
	
	@RequestMapping(value = "/DoWriteBoard", method = RequestMethod.POST)
	public String DoWriteBoard( BoardBean boardBeanObjToWrite, Model model) // 이것도 동작하고 위처럼 @ModelAttribute 사용해도 됨
	{
		logger.info("DoWriteBoard called!!");
		logger.info("memo=["+boardBeanObjToWrite.getMemo()+"]");
						
		//저장!!
		boardService.insertBoard(boardBeanObjToWrite);
				
		//Date date = new Date();
		//DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, new Locale("ko"));		
		//String formattedDate = dateFormat.format(date);		
		//model.addAttribute("serverTime", formattedDate );
		
		//목록을 조회후 저장 시킴.
		model.addAttribute("totalCnt", new Integer(boardService.getTotalCnt(null)) ); //Integer objects
		model.addAttribute("boardList", boardService.getList(1, null, 2) );
			
		return "home";		
	}
	
	//개별 목록 조회
	@RequestMapping(value = "/viewWork", method = RequestMethod.GET)
	public String viewWork	( 
								@RequestParam("memo_id") String memo_id,
								@RequestParam("current_page") String current_page,
								 Model model
							) 
	{
		logger.info("viewWork called!!");
		logger.info("memo_id=["+ memo_id+"] current_page=["+current_page+"]");
			
		model.addAttribute("memo_id", memo_id ); 
		model.addAttribute("current_page", current_page ); 
		model.addAttribute("boardData", boardService.getSpecificRow(memo_id) ); 
		
			
		return "viewMemo";		
	}
	
	// 특정 페이지에서 작업중 목록으로 나올경우, 이전 페이지 번호를 참조해서 해당 페이지 출력
	@RequestMapping(value = "/listSpecificPageWork", method = RequestMethod.GET)
	public String listSpecificPageWork	(								
								@RequestParam("current_page") String current_page,
								Model model
							) 
	{
		logger.info("listSpecificPageWork called!!");
		logger.info("current_page=["+current_page+"]");
			
		//model.addAttribute("memo_id", memo_id ); 
		//model.addAttribute("current_page", current_page ); 
		//model.addAttribute("boardData", boardService.getSpecificRow(memo_id) ); 
					
		return "listSpecificPage";		
	}
			
}
