package org.geon.springbootstudy.board.repository;

import org.geon.springbootstudy.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 한개의 Object 안에 Object[]로 나온다.
    // Board는 Member는 연관관계를 맺고있으므로 b.writer와 같은 형태로 사용한다.
    // 이처럼 내부에 있는 엔티티를 이용할 때는 'LEFT JOIN' 뒤에 'ON'을 이용하는 부분이 없다.
    @Query("select b,w from Board b " +
            "left join b.writer w where b.bno = :bno")
    Object getBoardWithWriter(@Param("bno") Long bno);

    @Query("select b, r from Board b " +
            "left join Reply r on r.board = b " +
            "where b.bno = :bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    @Query(value = "select b, w, count(r)" +
            "from Board b " +
            "left join b.writer w " +
            "left join Reply r on r.board = b " +
            "group by b",
            countQuery = "select count(b) from Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

    @Query("select b, w, count(r) " +
            "from Board b " +
            "left join b.writer w " +
            "left outer join Reply r on r.board = b " +
            "where b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);
}
