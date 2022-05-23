package org.geon.springbootstudy.memo.repository;

import lombok.extern.log4j.Log4j2;
import org.geon.springbootstudy.memo.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemoRepositoryTests {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass() {
        log.info(memoRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies() {
        IntStream.range(1,100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample..." +i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect() {
        Long mno = 99L;
        Optional<Memo> result = memoRepository.findById(mno);

        if(result.isPresent()){
            Memo memo = result.get();
            log.info(memo);
        }
    }

    @Test
    public void testUpdate() {
        Memo memo = Memo.builder().mno(99L).memoText("Update Text").build();

        log.info(memoRepository.save(memo));
    }

    @Test
    public void testDelete() {
        Long mno = 98L;

        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefault() {
        Pageable pageable = PageRequest.of(0,10);

        Page<Memo> result = memoRepository.findAll(pageable);

        log.info(result);

        for (Memo memo : result.getContent()) {
            log.info(memo);
        }
    }

    @Test
    public void testSort() {
        Sort sort1 = Sort.by("mno").descending();

        Pageable pageable = PageRequest.of(0,10, sort1);

        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo -> {
            log.info(memo);
        });

        Sort sort2 = Sort.by("memoText").ascending();

        Sort sortAll = sort1.and(sort2);

        Pageable pageable1 = PageRequest.of(0,10,sortAll);
    }

    @Test
    public void testQueryMethods() {
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);

        for (Memo memo : list) {
            log.info(memo);
        }
    }

    @Test
    public void testQueryMethodWithPageable() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());

        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);

        result.get().forEach(memo -> log.info(memo));
    }

    @Commit
    @Transactional
    @Test
    public void testDeleteQueryMethods() {
        //각 하나의 로우마다 삭제해서 성능이 그렇게 좋지않다.
        memoRepository.deleteMemoByMnoLessThan(10L);
    }

    @Test
    public void testNativeQuery() {
        //쿼리가 복잡해지는 경우 native query 사용
        List<Object[]> result = memoRepository.getNativeResult();

        log.info(result);
    }
}
