package org.geon.springbootstudy.board.repository;

import lombok.extern.log4j.Log4j2;
import org.geon.springbootstudy.board.entity.Board;
import org.geon.springbootstudy.board.entity.Reply;
import org.geon.springbootstudy.board.repository.ReplyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {

    @Autowired
    ReplyRepository replyRepository;

    @Test
    public void insertReply() {
        IntStream.rangeClosed(1, 300).forEach(i -> {
            long bno = (long)(Math.random() * 100) + 1;

            Board board = Board.builder().bno(bno).build();

            Reply reply = Reply.builder()
                    .text("Text..." + i)
                    .board(board)
                    .replyer("guest")
                    .build();

            replyRepository.save(reply);
        });
    }

    @Test
    public void testReadReply1() {
        Optional<Reply> result = replyRepository.findById(1L);

        Reply reply = result.get();

        log.info(reply);
        log.info(reply.getBoard());
    }
}
