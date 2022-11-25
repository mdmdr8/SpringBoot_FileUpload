package com.example.board.controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.board.entity.Board;
import com.example.board.service.BoardService;
import com.example.board.vo.PageInfo;

@RestController
public class BoardController {
	
	@Autowired
	BoardService boardService;


	@PostMapping("/writeboard")
//	파라미터에 들어온 setstate이고 string writer는 boardService.writeBoard를 거쳐서 엔터디의 속성으로 간다.
	public ResponseEntity<String> writeboard(
			@RequestParam("writer") String writer,
			@RequestParam("password") String password,
			@RequestParam("subject") String subject,
			@RequestParam("content") String content,
//	MultipartFile은 여러개 파일을 보내면 파일끼리 부분적으로 연결이 되어 있어서 보낼 떄 그 부분연결을 끊고 하나씩 보내주는 기능
//	required=false 는 값이 안들어왔을 때, false를 지정해줘서 오류가 안나게 한다.)
			@RequestParam(name="file", required=false) MultipartFile file) {
				
				ResponseEntity<String> res = null;
				try {
					String filename = null;
					if(file!=null && !file.isEmpty()) {
						String path = "D:\\22web_class\\spring_work_sts\\upload\\";
						filename=file.getOriginalFilename();
						File dFile = new File(path + filename);
						file.transferTo(dFile);
					}
				
					
//						null은 id 오토인덱스 붙여줘서 널로 넣고 값 넣으려고
				boardService.writeBoard(
						new Board(null, writer, password, subject, content, filename));
				
					res = new ResponseEntity<String>("게시글 저장 성공", HttpStatus.OK);
			} catch(Exception e) {
				e.printStackTrace();
				res = new ResponseEntity<String>("게시글 저장 실패", HttpStatus.BAD_REQUEST);
			}

			return res;}
	
//	두번째 방법
		@PostMapping("/writeboard2")
		public ResponseEntity<String> writeBoard2(@ModelAttribute Board board,
				@RequestParam(name="file", required = false) MultipartFile file) {
			ResponseEntity<String> res=null;
			try {
				boardService.writeBoard2(board, file);
				res =new ResponseEntity<String>("게시글 저장 성공", HttpStatus.OK);
			} catch(Exception e) {
//				e.printStackTrace(); 예외 상황을 console에 보여주는 역할
				e.printStackTrace();
				res = new ResponseEntity<String>("게시글 저장 실패", HttpStatus.BAD_REQUEST);
			}
			return res;
}
		@GetMapping("/boarddata/{id}")
//		@PathVariable는 {id}객체를 가져오는 어노테이션
		public ResponseEntity<Board> boarddata(@PathVariable Integer id) {
			ResponseEntity<Board> res=null;
			try {
				Board board = boardService.dataBoard(id);
				res = new ResponseEntity<Board>(board, HttpStatus.OK);
			} catch(Exception e) {
				e.printStackTrace();
				res = new ResponseEntity<Board>(HttpStatus.BAD_REQUEST);
			}
			return res;
}
		@GetMapping("/img/{filename}")
		public void imageView (@PathVariable String filename, HttpServletResponse response) {
			try {
				String path = "D:\\22web_class\\spring_work_sts\\upload\\";
				FileInputStream fis = new FileInputStream(path+filename);
				OutputStream out = response.getOutputStream();
				FileCopyUtils.copy(fis, out);
				out.flush();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		@GetMapping("/boardlist")
		public ResponseEntity<List<Board>> BoardList(){
			ResponseEntity<List<Board>> res=null;
			List<Board> boards = null;
			try {
				boards= boardService.BoardList();
				res= new ResponseEntity<List<Board>> (boards, HttpStatus.OK);

			} catch(Exception e){
				e.printStackTrace();
				res =new ResponseEntity<List<Board>>(boards, HttpStatus.BAD_REQUEST);
			}
			return res;
		}
//		@PathVariable : URL상에 경로의 일부를 파라미터로 사용하기 위해
		@PostMapping("/update/{id}")
		public ResponseEntity<String> updateBoard(@PathVariable Integer id, String subject, String content){
			System.out.println("여기 왔어");
			ResponseEntity<String> res=null;
			try {
				boardService.updateBoard(id, subject, content);
				res= new ResponseEntity<String> ("수정 완료", HttpStatus.OK);
			} catch(Exception e){
				e.printStackTrace();
				res =new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
			return res;
		}
		
		
//		여러개 묶어주는거 value post get 둘다 됨
		@GetMapping(value={"/boardpage/{page}", "/boardpage"}) //두개 가져올 수 있음 /boardpage가 없을 때 오류 발생 안시키고 첫번째 페이지 보여주려고 
		public ResponseEntity<Map<String, Object>> boardPage(@PathVariable(required = false) Integer page){
			if(page==null) page = 1;
			System.out.println(page);
			ResponseEntity<Map<String, Object>> res = null;
			try {
				PageInfo pageInfo = new PageInfo();
				pageInfo.setCurPage(page);
				pageInfo.setCurPage(page);
				List<Board> boards = boardService.boardPage(pageInfo);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("pageInfo", pageInfo);
				map.put("boards", boards);
				res = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
			} catch(Exception e){
				e.printStackTrace();
				res =new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
			}
			return res;
		}
}