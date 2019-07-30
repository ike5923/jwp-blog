package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.exception.ArticleException;
import techcourse.myblog.exception.UserException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.utils.converter.DtoConverter;
import techcourse.myblog.utils.page.PageRequest;

import java.util.List;

@Service
public class ArticleService {
    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);
    private static final String ID = "id";
    private static final int START_PAGE = 1;
    private static final int VIEW_ARTICLE_COUNT = 10;

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional(readOnly = true)
    public List<Article> findAll() {
        PageRequest pageRequest = new PageRequest(START_PAGE, VIEW_ARTICLE_COUNT, Sort.Direction.DESC, ID);
        Page<Article> page = articleRepository.findAll(pageRequest.of());
        log.info("page : {} ", page.getContent());
        return page.getContent();
    }

    @Transactional(readOnly = true)
    public Article findArticle(long articleId) {
        return getArticle(articleId);
    }

    private Article getArticle(long articleId) {
        return articleRepository.findById(articleId).orElseThrow(ArticleException::new);
    }

    @Transactional
    public Article save(ArticleRequestDto articleRequestDto, UserResponseDto userResponseDto) {
        Article article = DtoConverter.convert(articleRequestDto);
        User user = userRepository.findByEmail(userResponseDto.getEmail()).orElseThrow(UserException::new);
        article.setAuthor(user);
        articleRepository.save(article);
        return article;
    }

    @Transactional
    public Article update(long articleId, ArticleRequestDto articleRequestDto) {
        Article originArticle = findArticle(articleId);
        originArticle.update(DtoConverter.convert(articleRequestDto));
        return originArticle;
    }

    @Transactional
    public void delete(long articleId) {
        Article article = getArticle(articleId);
        commentRepository.deleteAllByArticle(article);
        articleRepository.deleteById(articleId);
    }

    @Transactional
    public List<Comment> getCommentsByArticleId(Long articleId) {
        Article article = getArticle(articleId);
        return commentRepository.findByArticle(article);
    }
}