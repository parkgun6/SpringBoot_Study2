package org.geon.springbootstudy.guestbook.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.geon.springbootstudy.guestbook.dto.GuestbookDTO;
import org.geon.springbootstudy.guestbook.dto.PageRequestDTO;
import org.geon.springbootstudy.guestbook.dto.PageResultDTO;
import org.geon.springbootstudy.guestbook.entity.Guestbook;
import org.geon.springbootstudy.guestbook.entity.QGuestbook;
import org.geon.springbootstudy.guestbook.repository.GuestbookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService{

    private final GuestbookRepository guestbookRepository;

    @Override
    public Long register(GuestbookDTO guestbookDTO) {

        Guestbook entity = dtoToEntity(guestbookDTO);

        log.info(entity);

        guestbookRepository.save(entity);

        return entity.getGno();
    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder booleanBuilder = getSearch(requestDTO);

        Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder, pageable);

        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public GuestbookDTO read(Long gno) {
        Optional<Guestbook> result = guestbookRepository.findById(gno);

        return result.isPresent() ? entityToDto(result.get()) : null;
    }

    @Override
    public void remove(Long gno) {
        guestbookRepository.deleteById(gno);
    }

    @Override
    public void modify(GuestbookDTO dto) {

        Optional<Guestbook> result = guestbookRepository.findById(dto.getGno());

        if(result.isPresent()) {
            Guestbook entity = result.get();

            entity.changeContent(dto.getContent());
            entity.changeTitle(dto.getTitle());

            guestbookRepository.save(entity);
        }
    }

    public BooleanBuilder getSearch(PageRequestDTO requestDTO) {
        String type = requestDTO.getType();

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = requestDTO.getKeyword();

        // where gno > 0
        BooleanExpression expression = qGuestbook.gno.gt(0L);

        booleanBuilder.and(expression);

        // trim() = 공백제거
        if (type == null || type.trim().length() == 0) {
            return booleanBuilder;
        }

        BooleanBuilder conditionBuilder = new BooleanBuilder();

        // contains = 포함여부 확인
        if (type.contains("t")) {
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }
        if (type.contains("c")) {
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }
        if (type.contains("w")) {
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }

        // 조건 통합
        booleanBuilder.and(conditionBuilder);

        // 검색 조건이 있을 때 conditionBuilder 반환, 없다면 gno > 0 만 반환
        return booleanBuilder;
    }
}
