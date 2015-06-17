package entities;

import entities.Groups;
import entities.Project;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-06-03T04:46:38")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, String> firstName;
    public static volatile SingularAttribute<User, String> lastName;
    public static volatile SingularAttribute<User, String> password;
    public static volatile SingularAttribute<User, String> userName;
    public static volatile CollectionAttribute<User, Groups> groupsCollection;
    public static volatile CollectionAttribute<User, Project> projectCollection;
    public static volatile SingularAttribute<User, String> email;

}