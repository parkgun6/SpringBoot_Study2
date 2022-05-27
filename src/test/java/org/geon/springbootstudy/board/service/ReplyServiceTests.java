package org.geon.springbootstudy.board.service;

import lombok.extern.log4j.Log4j2;
import org.geon.springbootstudy.board.dto.ReplyDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Log4j2
public class ReplyServiceTests {

    @Autowired
    ReplyService replyService;

    @Test
    public void testGetList() {
        Long bno = 100L;

        List<ReplyDTO> replyDTOList = replyService.getList(bno);

        for (ReplyDTO replyDTO : replyDTOList) {
            log.info(replyDTO);
        }
    }
}
