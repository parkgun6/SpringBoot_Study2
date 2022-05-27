package org.geon.springbootstudy.board.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.geon.springbootstudy.board.entity.Board;
import org.geon.springbootstudy.board.entity.QBoard;
import org.geon.springbootstudy.board.entity.QMember;
import org.geon.springbootstudy.board.entity.QReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    public SearchBoardRepositoryImpl() {
        
        // QuerydslRepositorySupport는 생성자가 존재하므로 클래스 내에서 super()를 이용하여 호출해야한다.
        // 이때 도메인 클래스를 지정할때 null값을 넣을 수 있다.
        super(Board.class);
    }

    @Override
    public Board search1() {

        log.info("Search1.................");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);

//      jpqlQuery.select(board)
//              .where(board.bno.eq(5L));

        jpqlQuery.leftJoin(member)
                .on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply)
                .on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(
                                        board,
                                        member.email,
                                        reply.count()
                                        )
                                        .groupBy(board);
//        jpqlQuery.select(
//                board,
//                member.email,
//                reply.count()
//                )
//                .groupBy(board);

        log.info("---------------------------------");
        log.info(tuple);
//        log.info(jpqlQuery);
        log.info("---------------------------------");

        List<Tuple> result = tuple.fetch();
//        List<Board> result1 = jpqlQuery.fetch();

        log.info(result);
        log.info("---------------------------------");
//        log.info(result1);

        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {

        log.info("Search Page...........................");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);

        jpqlQuery.leftJoin(member)
                .on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply)
                .on(reply.board.eq(board));

        // select() 내에도 여러 객체를 가져오는 형태로 변경되었다.
        // 이렇게 정해진 엔티티 객체 단위가 아니라 각각의 데이터를 추출하는 경우에는
        // Tuple이라는 객체를 이용한다.

        // select b, w count(r) from Board b
        //      left join b.writer w
        //      left join Reply r on r.board = b
        JPQLQuery<Tuple> tuple = jpqlQuery.select(
                        board,
                        member.email,
                        reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = board.bno.gt(0L);

        booleanBuilder.and(expression);

        if (type != null) {
            String[] typeArr = type.split("");
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String t : typeArr) {
                switch (t) {
                    case "t":
                        conditionBuilder.or(board.title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(board.content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);

        Sort sort = pageable.getSort();

        // Pageable의 Sort 객체는 JPQLQuery의 orderBy()의 파라미터로 전달되어야 하지만,
        // JPQL에서는 Sort객체를 지원하지 않기 때문에 orderBy()의 경우 OrderSpecifier<T extends Comparable>을 파라미터로 처리해야한다.
        // springframework Sort는 내부적으로 여러 개의 Sort객체를 연결할수 있기 때문에 forEach를 이용해서 처리한다.
        // OrderSpecifier에는 정렬이 필요하므로 Sort 객체의 정렬 관련 정보를 Order타입으로 처리하고,
        // Sort 객체의 속성들은 PathBuilder라는 것을 이용해서 처리한다.
        // PathBuilder를 생성할 때 문자열로 된 이름은 JPQLQuery를 생성할 때 이용하는 변수명과 동일해야한다.
        // JPQLQuery를 이용해서 동적으로 검색조건을 처리하는것은 복잡하지만
        // 한번의 개발만으로 count쿼리도 같이 처리할수 있는 장점이 있다.
        // count를 얻는 방법은 fetchCount()를 이용하면 된다.
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ?
                    Order.ASC : Order.DESC;
            String prop = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");

            tuple.orderBy(new OrderSpecifier<>(direction, orderByExpression.get(prop)));
        });

        tuple.groupBy(board);

        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        log.info(result);

        long count = tuple.fetchCount();

        log.info("COUNT : " + count);

        return new PageImpl<Object[]>(
                result.stream().map(t -> t.toArray()).collect(Collectors.toList()),
                pageable,
                count
        );
    }

}
