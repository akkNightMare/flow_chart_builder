package entities;

import entities.Clazz;
import entities.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-06-03T04:46:38")
@StaticMetamodel(Project.class)
public class Project_ { 

    public static volatile SingularAttribute<Project, Date> createdDate;
    public static volatile CollectionAttribute<Project, Clazz> classCollection;
    public static volatile SingularAttribute<Project, String> name;
    public static volatile SingularAttribute<Project, Integer> id;
    public static volatile SingularAttribute<Project, User> userName;

}