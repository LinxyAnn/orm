package reflection;

import annotation.*;
import exception.AnnotationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * Class checks does the annotated class have the annotations
 *
 * @author Anna Severyna
 */

public class AnnotationService {


    /**
     * Method for general checking for all annotations to pass all classes to the configuration
     *
     * @param   listClassAnnotaition        list of annotations
     * @throws  AnnotationException
     */
    public void checkAllAnnotation(Set<Class<?>> listClassAnnotaition) throws AnnotationException {
        for (Class<?> classAnnotation : listClassAnnotaition
        ) {
            checkTableAnnotation(classAnnotation);
            checkField(classAnnotation);
        }
    }

    /**
     * Method checks if @TableSoft annotation exists
     *
     * @param   classTable   table name specified in annotated class
     * @see annotation.TableSoft
     */
    private void checkTableAnnotation(Class<?> classTable) throws AnnotationException {
        if (classTable.getAnnotation(TableSoft.class) == null) {
            throw new AnnotationException("Class '" + classTable.getSimpleName() + "' does not have annotation @TableSoft");
        }
    }

    /**
     * Method checks if field exists in specified class
     *
     * @param   classField      field name
     * @throws  AnnotationException
     */

    private void checkField(Class<?> classField) throws AnnotationException {
        Field[] fields = classField.getDeclaredFields();
        if (fields.length == 0) {
            throw new AnnotationException("Class '" + classField.getSimpleName() + "' has no fields");
        }
    }
}
