package exception;

import java.sql.SQLException;

/**
 * Annotation exception class
 *
 * @author Anna Severyna
 *
 * */

public class AnnotationException extends SQLException{
    public AnnotationException(String message) {
        super(message);
    }
}
