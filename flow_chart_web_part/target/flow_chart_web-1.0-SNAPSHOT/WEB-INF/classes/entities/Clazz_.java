package entities;

import entities.Method;
import entities.Project;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-06-03T04:46:38")
@StaticMetamodel(Clazz.class)
public class Clazz_ { 

    public static volatile SingularAttribute<Clazz, byte[]> image;
    public static volatile SingularAttribute<Clazz, Date> createdDate;
    public static volatile CollectionAttribute<Clazz, Method> methods;
    public static volatile SingularAttribute<Clazz, String> name;
    public static volatile SingularAttribute<Clazz, Integer> id;
    public static volatile SingularAttribute<Clazz, String> source;
    public static volatile SingularAttribute<Clazz, Project> projectId;

}