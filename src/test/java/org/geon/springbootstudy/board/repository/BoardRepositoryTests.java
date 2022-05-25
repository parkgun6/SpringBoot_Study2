package org.geon.springbootstudy.board.repository;

import lombok.extern.log4j.Log4j2;
import org.geon.springbootstudy.board.entity.Board;
import org.geon.springbootstudy.board.entity.Member;
import org.geon.springbootstudy.board.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    BoardRepository boardRepository;

    @Test
    public void insertBoard() {
        IntStream.rangeClosed(0,100).forEach(i -> {
            int idx = (int)(Math.random() * 100) + 1;

            Member member = Member.builder()
                    .email("user" + idx + "@geon.com")
                    .build();

            Board board = Board.builder()
                    .title("Title..." + i)
                    .content("Content..." + i)
                    .writer(member)
                    .build();

            boardRepository.save(board);
        });
    }

    @Test
    @Transactional
    public void testsRead1() {

        // (default) Eager Loading은 불필요한 join을 유발하여 성능저하를 일으킨다
        // Lazy Loading을 통해 불필요한 join을 유발하지 않는 대신 데이터베이스 연결이 끝난 시점에서 no Session 에러가 난다.
        // 그런 경우에 @Transactional을 이용하여 필요할 때 데이터베이스와의 연결을 생성한다
        // 연관관계에선 @ToString()을 조심해야한다. toString()을 호출하면 데이터베이스 연결을 필요로 한다.
        // 연관관계가 있는 엔티티 클래스의 경우 @ToString에 exclude속성을 사용하는것이 좋다
        // 기본적으로 Lazy Loading을 사용한다.
        Optional<Board> result = boardRepository.findById(100L);

        Board board = result.get();

        log.info(board);
        log.info(board.getWriter());
    }

    @Test
    public void testReadWithWriter() {
        Object result = boardRepository.getBoardWithWriter(100L);

        Object[] arr = (Object[]) result;

        log.info("--------------------------------");
        log.info(Arrays.toString(arr));
    }

    @Test
    public void testWithReplyCount() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

        result.get().forEach(row -> {
            Object[] arr = (Object[])row;

            log.info(Arrays.toString(arr));
        });
    }

    @Test
    public void testRead2() {
        Object result = boardRepository.getBoardByBno(100L);

        Object[] arr = (Object[])result;

        log.info(Arrays.toString(arr));
    }
}
