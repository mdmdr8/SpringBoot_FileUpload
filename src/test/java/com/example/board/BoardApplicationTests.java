package com.example.board;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.board.entity.Board;
import com.example.board.repository.boardrepository;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class BoardApplicationTests {
	@Autowired
	boardrepository boardrepository;
	
	@Test
	void contextLoads() {
		PageRequest pageRequest = PageRequest.of(1, 3);
		Page<Board> pages=boardrepository.findAll(pageRequest);
		List<Board> boards = pages.getContent();
		pages.getTotalPages();
//		getContent 데이터만 가졍옴
		for(Board board :boards){
			System.out.println(board);
		}
	}

}





