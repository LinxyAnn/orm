package reflection;


import exception.AnnotationException;
import metadata.MetaDataSchema;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Class starts the ORM checking annotations and then sends class fields to meta data
 *
 * @author Anna Severyna
 *
 * */

public class DataClassBuilder {
    private Set<Class<?>> checkedClass = new LinkedHashSet<>();
    private AnnotationService annotationService = new AnnotationService();
    private MetaDataService metaDataService = new MetaDataService();

    public DataClassBuilder() {
    }

    /**
     * Method adds annotated class to the set of checked on annotations classes
     *
     * @param   annoClass     annotated class
     */
    public void addAnnotationClass (Class<?> annoClass) {
        checkedClass.add(annoClass);
    }

    /**
     * Method builds configuration of checked on annotations classes for further handling of metadata
     *
     * @throws AnnotationException
     */
    public void bulidConfig () throws AnnotationException {
        annotationService.checkAllAnnotation(checkedClass);
        metaDataService.createMetaDataSchema(checkedClass);

/*        for (Class<?> checkedClass2: checkedClass
             ) {
            MetaDataSchema.printClassMap(checkedClass2);
        }*/
    }


}
