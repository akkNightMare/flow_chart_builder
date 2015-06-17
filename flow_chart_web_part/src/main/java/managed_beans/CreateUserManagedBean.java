/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managed_beans;

import entities.Groups;
import entities.User;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import session.GroupsFacade;
import session.UserFacade;
import util.JsfUtil;

/**
 *
 * @author Denis
 */
@ManagedBean(name = "createUser")
@RequestScoped
public class CreateUserManagedBean {

    @EJB
    private GroupsFacade groupEJB;

    @EJB
    private UserFacade userEJB;

    private final User user = new User();

    /**
     * Creates a new instance of CreateUserManagedBean
     */
    public CreateUserManagedBean() {
    }

    public User getUser() {
        return user;
    }

    public String createUser() {
        if (userEJB.isExist(user.getUserName())) {
            JsfUtil.addErrorMessage("User \"" + user.getUserName() + "\" already exists");
            return "";
        } else {
            try {
                Groups group = groupEJB.find(2);  //Find  "user" group
                if (group == null) {
                    group = new Groups(2, "user");
                    groupEJB.edit(group);
                } else {
                    group.getUserCollection().add(user);
                    groupEJB.edit(group);
                    JsfUtil.addSuccessMessage("Thanks for registering, " + user.getUserName() + "!");
                }
            } catch (Exception ex) {
                JsfUtil.addErrorMessage("Error during registering");
            }
        }

        return "login";
    }

}
