package techcourse.myblog.domain;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import techcourse.myblog.dto.CommentRequestDto;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class Comment {
    private static final int MAX_CONTENTS_LENGTH = 200;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Length(max = MAX_CONTENTS_LENGTH)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commenterId", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private User commenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId")
    private Article article;

    @CreatedDate
    private Date date;

    public Comment() {
    }

    public Comment(String contents, User commenter, Article article) {
        this.contents = contents;
        this.commenter = commenter;
        this.article = article;
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.contents = commentRequestDto.getContents();
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public User getCommenter() {
        return commenter;
    }

    public Date getDate() {
        return date;
    }

    public Article getArticle() {
        return article;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) &&
                Objects.equals(contents, comment.contents) &&
                Objects.equals(commenter, comment.commenter) &&
                Objects.equals(article, comment.article) &&
                Objects.equals(date, comment.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contents, commenter, article, date);
    }
}
