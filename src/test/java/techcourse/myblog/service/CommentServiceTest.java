package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.comment.CommentRequest;
import techcourse.myblog.dto.user.UserResponse;
import techcourse.myblog.exception.comment.CommentAuthenticationException;
import techcourse.myblog.repository.CommentRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentServiceTest {

    private static final String TITLE = "게시글 제목";
    private static final String CONTENTS = "게시글 내용";
    private static final String COVER_URL = "게시글 배경";

    private static final String NAME = "ike";
    private static final String PASSWORD = "@Password1234";
    private static final String EMAIL = "ike@ike.com";

    private static final String NAME_2 = "ikee";
    private static final String EMAIL_2 = "ike2@gmail.com";

    private static final String COMMENT_CONTENTS = "댓글";

    @Autowired
    private CommentService commentService;

    @MockBean(name = "commentRepository")
    private CommentRepository commentRepository;

    private Comment comment;
    private Article article;
    private User author;
    private CommentRequest commentRequest;
    private UserResponse userResponseDto;
    private UserResponse notCommenter;

    @BeforeEach
    void setUp() {
        author = new User(NAME, PASSWORD, EMAIL);
        article = new Article(TITLE, CONTENTS, COVER_URL, author);
        comment = new Comment(COMMENT_CONTENTS, author, article);
        commentRequest = new CommentRequest(COMMENT_CONTENTS);
        userResponseDto = new UserResponse(id, NAME, EMAIL);
        notCommenter = new UserResponse(id, NAME_2, EMAIL_2);
    }

    @Test
    void 댓글_작성자와_수정_하려는_사용자가_일치하는_경우() {
        //given
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        //then
        assertDoesNotThrow(() -> commentService.update(1L, commentRequest, userResponseDto));
    }

    @Test
    public void 작성자_외의_사용자가_댓글을_수정하려고_하는_경우_예외처리() {
        // given
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        // then
        assertThatThrownBy(() -> commentService.update(1L, commentRequest, notCommenter))
                .isInstanceOf(CommentAuthenticationException.class);
    }

    @Test
    public void 댓글_작성자와_삭제_하려는_사용자가_일치하는_경우() {
        // given
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        // then
        assertDoesNotThrow(() -> commentService.delete(1L, userResponseDto));
    }

    @Test
    public void 작성자_외의_사용자가_댓글을_삭제하려고_하는_경우_예외처리() {
        // given
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        // then
        assertThatThrownBy(() -> commentService.delete(1L, notCommenter))
                .isInstanceOf(CommentAuthenticationException.class);
    }
}
