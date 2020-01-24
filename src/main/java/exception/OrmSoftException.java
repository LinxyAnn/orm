package exception;

import java.sql.SQLException;

/**
 * ORM operations error on the client side
 *
 * @author Anna Severyna
 */

public class OrmSoftException extends SQLException{
    public OrmSoftException(String message) {
        super(message);
    }
}
