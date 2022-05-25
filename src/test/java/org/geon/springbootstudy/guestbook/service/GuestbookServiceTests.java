package org.geon.springbootstudy.guestbook.service;

import lombok.extern.log4j.Log4j2;
import org.geon.springbootstudy.guestbook.dto.GuestbookDTO;
import org.geon.springbootstudy.guestbook.dto.PageRequestDTO;
import org.geon.springbootstudy.guestbook.dto.PageResultDTO;
import org.geon.springbootstudy.guestbook.entity.Guestbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class GuestbookServiceTests {

    @Autowired
    GuestbookService guestbookService;

    @Test
    public void testRegister() {

        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
                .title("Sample Title....")
                .content("Sample Content.....")
                .writer("user0")
                .build();

        log.info(guestbookService.register(guestbookDTO));
    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = guestbookService.getList(pageRequestDTO);

        log.info("PREV: " + resultDTO.isPrev());
        log.info("NEXT: " + resultDTO.isNext());
        log.info("TOTAL: " + resultDTO.getTotalPage());
        log.info("-----------------------------------------");

        for (GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {
            log.info(guestbookDTO);
        }

        log.info("-----------------------------------------");
        resultDTO.getPageList().forEach(i -> log.info(i));
    }

    @Test
    public void testSearch() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tc")
                .keyword("Sample")
                .build();

        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = guestbookService.getList(pageRequestDTO);


        log.info("PREV: " + resultDTO.isPrev());
        log.info("NEXT: " + resultDTO.isNext());
        log.info("TOTAL: " + resultDTO.getTotalPage());
        log.info("-----------------------------------------");

        for (GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {
            log.info(guestbookDTO);
        }

        log.info("-----------------------------------------");
        resultDTO.getPageList().forEach(i -> log.info(i));
    }
}
