package org.geon.springbootstudy.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
//exclude = ~를 제외한
//writer로 Member테이블을 조인하면서 에러가 발생한다. 또는 불필요한 쿼리를 날린다.
@ToString(exclude = "writer")
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String content;

    // (default) Eager Loading은 불필요한 join을 유발하여 성능저하를 일으킨다
    // Lazy Loading을 통해 불필요한 join을 유발하지 않는 대신 데이터베이스 연결이 끝난 시점에서 no Session 에러가 난다.
    // 그런 경우에 @Transactional을 이용하여 필요할 때 데이터베이스와의 연결을 생성한다
    // 연관관계에선 @ToString()을 조심해야한다. toString()을 호출하면 데이터베이스 연결을 필요로 한다.
    // 연관관계가 있는 엔티티 클래스의 경우 @ToString에 exclude속성을 사용하는것이 좋다
    // 기본적으로 Lazy Loading을 사용한다.
    @ManyToOne (fetch = FetchType.LAZY)
    private Member writer;

    //Entity는 완벽한 ReadOnly가 아니다.
    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content = content;
    }
}