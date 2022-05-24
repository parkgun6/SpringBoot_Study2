package org.geon.springbootstudy.guestbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.geon.springbootstudy.guestbook.dto.GuestbookDTO;
import org.geon.springbootstudy.guestbook.dto.PageRequestDTO;
import org.geon.springbootstudy.guestbook.dto.PageResultDTO;
import org.geon.springbootstudy.guestbook.entity.Guestbook;
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

        Page<Guestbook> result = guestbookRepository.findAll(pageable);

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
}
