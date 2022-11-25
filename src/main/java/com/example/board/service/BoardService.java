package com.example.board.service;

import java.io.File;
import java.util.List;
import java.util.Optional;

//import org.hibernate.query.criteria.internal.predicate.IsEmptyPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.board.entity.Board;
import com.example.board.repository.boardrepository;
import com.example.board.vo.PageInfo;


@Service
public class BoardService {
	
	@Autowired
	boardrepository boardrepository;
	
	public void writeBoard(Board board) throws Exception{
		boardrepository.save(board);
	}
	
	public void writeBoard2(Board board, MultipartFile file) throws Exception{
		String filename = null;
//		파일을 첨부하지 않아도 String 변수를 받아서 뿌리고 오류 발생을 예방하기 위해 && IsEmpty해준거임
		if(file!=null && !file.isEmpty()) {
			String path = "D:\\22web_class\\spring_work_sts\\upload\\";
			filename = file.getOriginalFilename();
			File dFile = new File(path+filename);
//			파일 내용 복사
			file.transferTo(dFile);
		}
		board.setFilename(filename);
		boardrepository.save(board);

}
	public Board dataBoard(Integer id) throws Exception {
//		nullPoint오류가 안생기게 optional을 쓴다.
		Optional<Board> oboard = boardrepository.findById(id);
		if(oboard.isPresent()) return oboard.get();
		throw new Exception("글번호 오류");
	}
//최신글이 위로 올라오게
	public List<Board> BoardList() {
		return boardrepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
	}
	

	public void updateBoard(Integer id, String subject, String content) throws Exception{
		Optional<Board> oBoard = boardrepository.findById(id);
        if (oBoard.isEmpty()) {
            throw new Exception("글 조회 오류");
        }
        Board board1 = oBoard.get();
        board1.setSubject(subject);
        board1.setContent(content);
        boardrepository.save(board1);
	}
	public List<Board> boardPage(PageInfo pageInfo) throws Exception{
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, 5, Sort.by(Sort.Direction.DESC, "id"));
		
		Page<Board> pages = boardrepository.findAll(pageRequest);
		
		int maxPage = pages.getTotalPages();
		int startPage = pageInfo.getCurPage()/10*10+1; //start page를 1,11,21,31
		int endPage = startPage + 10 -1; //10, 20,30
		if(endPage>maxPage) endPage=maxPage;
		
		pageInfo.setAllPageInteger(maxPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		
		return pages.getContent();
	}
}
