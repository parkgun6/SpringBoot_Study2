package org.geon.springbootstudy.board.service;

import org.geon.springbootstudy.board.dto.BoardDTO;
import org.geon.springbootstudy.board.dto.PageRequestDTO;
import org.geon.springbootstudy.board.dto.PageResultDTO;
import org.geon.springbootstudy.board.entity.Board;
import org.geon.springbootstudy.board.entity.Member;

public interface BoardService {

    Long register(BoardDTO dto);

    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    BoardDTO get(Long bno);

    void removeWithReplies(Long bno);

    void modify(BoardDTO boardDTO);

    default Board dtoTOEntity(BoardDTO dto){
        Member writer = Member.builder().email(dto.getWriterEmail()).build();

        Board board = Board.builder()
                .title(dto.getTitle())
                .writer(writer)
                .content(dto.getContent())
                .build();
        return board;
    }

    default BoardDTO entityToDTO(Board board, Member member, Long replyCount){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue())
                .build();

        return boardDTO;
    }
}
