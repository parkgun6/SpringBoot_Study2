package org.geon.springbootstudy.board.repository;

import org.geon.springbootstudy.board.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
