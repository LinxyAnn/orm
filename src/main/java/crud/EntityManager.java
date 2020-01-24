package crud;

import exception.AnnotationException;
import mapping.InstanceBuilder;
import model.Auto;
import model.User;
import reflection.DataClassBuilder;

import java.sql.SQLException;

/**
 * Class manages the CRUD-methods
 *
 * @author Anna Severyna
 *
 */
public class EntityManager {

    public EntityManager() throws AnnotationException {
        DataClassBuilder dataClassBuilder = new DataClassBuilder();
        dataClassBuilder.addAnnotationClass(User.class);
        dataClassBuilder.addAnnotationClass(Auto.class);
        dataClassBuilder.bulidConfig();
    }

    /**
	 * Method removes object of specified class data from database by id
	 *
     * @param classobj  object class
	 * @param id        object id
	 * 
     * @throws IllegalAccessException
     * @throws SQLException
     * @throws InstantiationException
	 */
    public void deleteById (Class<?> classobj, Integer id) throws IllegalAccessException, SQLException, InstantiationException {
        CRUDOperations crudOperations = new CRUDOperations();
        crudOperations.deleteById(classobj, id);
    }

    /**
	 * Method removes object data from database 
	 *
     * @param object    object
	 * @param id        object id
	 * 
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws InstantiationException
	 */
    public void save(Object object) throws SQLException, IllegalAccessException, NoSuchFieldException, InstantiationException {
        UpdateData updateData = new UpdateData();
        updateData.updateRecord(object);
    }

    /**
	 * Method loads object data from database 
	 *
     * @param classobj      object class
	 * @param columnName    object field column name in database table
     * @param value         object field column name value in database table
	 * 
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
	 */
    public Object loadByValue (Class<?> classobj, String columnName, String value) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        CreateData createData = new CreateData();
        Object object = createData.createInstanceByValue(classobj, columnName, value);;
        InstanceBuilder.joinGet = null;
        return object;
    }

    /**
	 * Method loads object data from database 
	 *
     * @param classobj      object class
	 * @param value         object id in database table
	 * 
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
	 */
    public Object loadById (Class<?> classobj, int value) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        CreateData createData = new CreateData();
        Object object = createData.createInstanceFromRecordById(classobj, value);
        InstanceBuilder.joinGet = null;
        return object;
    }
}
