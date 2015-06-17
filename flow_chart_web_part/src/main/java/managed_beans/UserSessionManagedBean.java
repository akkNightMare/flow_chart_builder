/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managed_beans;

import entities.User;
import java.io.Serializable;
import java.security.Principal;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import session.UserFacade;
import util.JsfUtil;

/**
 *
 * @author Denis
 */
@ManagedBean(name = "userSession", eager = true)
@SessionScoped
public class UserSessionManagedBean implements Serializable {

    private static Logger log = Logger.getLogger(UserSessionManagedBean.class.getName());

    @EJB
    private UserFacade userEJB;

    private User currentUser;

    /**
     * Creates a new instance of UserManagedBean
     */
    public UserSessionManagedBean() {
    }

    public String logout() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            String userName = context.getUserPrincipal().getName();
            JsfUtil.addSuccessMessage("User " + userName + " has been logged out");
            context.invalidateSession();
            return "welcomepage";
        } catch (Exception ex) {
            return "";
        }
    }

    public boolean isLogged() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        Principal principal = context.getUserPrincipal();
        return principal != null;
    }

    public User getCurrentUser() {
        if (currentUser == null) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            Principal principal = context.getUserPrincipal();
            if (principal == null) {
                return null;
            }
            String userName = principal.getName();
            currentUser = userEJB.findByUserName(userName);
        }
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

}
