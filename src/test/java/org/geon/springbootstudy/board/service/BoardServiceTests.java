package org.geon.springbootstudy.board.service;

import lombok.extern.log4j.Log4j2;
import org.geon.springbootstudy.board.dto.BoardDTO;
import org.geon.springbootstudy.board.dto.PageRequestDTO;
import org.geon.springbootstudy.board.dto.PageResultDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    BoardService boardService;

    @Test
    public void testReigster() {
        BoardDTO dto = BoardDTO.builder()
                .title("Test...")
                .content("Test Content....")
                // DB에 존재하는 Email이어야 한다.
                .writerEmail("user13@geon.com")
                .build();

        Long bno = boardService.register(dto);
    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);

        for (BoardDTO boardDTO : result.getDtoList()) {
            log.info(boardDTO);
        }
    }

    @Test
    public void testGet() {
        Long bno = 100L;

        BoardDTO boardDTO = boardService.get(bno);

        log.info(boardDTO);
    }

    @Test
    public void testRemove() {
        Long bno = 1L;

        boardService.removeWithReplies(bno);
    }

    @Test
    public void testModify() {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(100L)
                .title("Modify.....")
                .content("Modify......")
                .build();

        boardService.modify(boardDTO);
    }
}
