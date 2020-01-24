import crud.EntityManager;
import exception.AnnotationException;
import model.Auto;
import model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class tests the CRUD-methods of the application
 * 
 * @author Anna Severyna
 */
public class TestOrm {
    EntityManager entityManager;
    List<Auto> autoList = new ArrayList<>();
    List<User> userList = new ArrayList<>();
    public TestOrm() throws AnnotationException {
        entityManager = new EntityManager();
        createLists();

    }

    public void testSaveUser () throws SQLException, IllegalAccessException, NoSuchFieldException, InstantiationException {
        for (User user : userList
             ) {
            entityManager.save(user);
        }
    }

    public void testSaveAuto () throws SQLException, IllegalAccessException, NoSuchFieldException, InstantiationException {
        for (Auto auto : autoList
        ) {
            entityManager.save(auto);
        }
    }

    public void testDeleteById (){

    }

    private void createLists (){
        User user1 = new User("Mary", 20, "Casablanca");
        User user2 = new User("Jane", 24, "Paris");
        User user3 = new User("Patrick", 20, "Rome");
        User user4 = new User("Елена", 35, "Сызрань");
        User user5 = new User("Kate", 20, "Boston");
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        userList.add(user5);

        Auto auto1 = new Auto("Opel", "white", false, user2);
        Auto auto2 = new Auto("Ford", "black", true, null);
        Auto auto3 = new Auto("Ferrari", "green", false, null);
        Auto auto4 = new Auto("BMW", "red", true, user1);
        Auto auto5 = new Auto("Lexus", "blue", false, user3);

        autoList.add(auto1);
        autoList.add(auto2);
        autoList.add(auto3);
        autoList.add(auto4);
        autoList.add(auto5);
    }
}
