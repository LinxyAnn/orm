package crud;

import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

/**
 *  Class contains auxiliary methods for create and update methods
 *
 * @author Anna Severyna
 *
 */
public class CRUDUtilities {

    /**
     *
     * @param linkedList    list of column names of database table
     * @return              string of column names of database table
     */
    public static String listColumnsToString(List<String> linkedList){
        Iterator<String> listIter = linkedList.iterator();
        StringJoiner sj = new StringJoiner(", ");
        while (listIter.hasNext()) {
            String st = listIter.next();
            sj.add(st);
        }
        return sj.toString().trim();
    }

    /**
     *
     * @param linkedList    list of column names values from database table
     * @return              string of column names values from database table
     */
    public static String listValuesToString(List <String> linkedList){
        Iterator<String> listIter = linkedList.iterator();
        StringJoiner sj = new StringJoiner(", ");
        while (listIter.hasNext()) {
            String st = listIter.next();
            sj.add("'" +  st + "'");
        }
        return sj.toString().trim();
    }
}
