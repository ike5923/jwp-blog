package techcourse.myblog.exception.comment;

public class CommentAuthenticationException extends RuntimeException {

    private static final String ACCESS_AUTHENTICATION_ERROR = "댓글에 권한이 없습니다.";

    public CommentAuthenticationException() {
        super(ACCESS_AUTHENTICATION_ERROR);
    }

    public CommentAuthenticationException(String message) {
        super(message);
    }

    public CommentAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentAuthenticationException(Throwable cause) {
        super(cause);
    }

    protected CommentAuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
