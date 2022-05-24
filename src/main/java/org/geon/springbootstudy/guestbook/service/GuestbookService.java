package org.geon.springbootstudy.guestbook.service;

import org.geon.springbootstudy.guestbook.dto.GuestbookDTO;
import org.geon.springbootstudy.guestbook.dto.PageRequestDTO;
import org.geon.springbootstudy.guestbook.dto.PageResultDTO;
import org.geon.springbootstudy.guestbook.entity.Guestbook;

public interface GuestbookService {

    Long register(GuestbookDTO guestbookDTO);

    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);

    GuestbookDTO read(Long gno);

    void remove(Long gno);

    void modify(GuestbookDTO dto);

    default Guestbook dtoToEntity(GuestbookDTO dto){
        return Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        //날짜와 시간처리는 AuditingJPA로 알아서 처리하게 해놨기때문에 따로 처리하지않는다.
    }

    default GuestbookDTO entityToDto(Guestbook entity){

        GuestbookDTO dto  = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                //날짜 시간처리가 들어간다.
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();

        return dto;
    }
}
