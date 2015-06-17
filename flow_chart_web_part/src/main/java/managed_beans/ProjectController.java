/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managed_beans;

import entities.Project;
import entities.User;
import java.io.Serializable;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import session.ProjectFacade;
import session.UserFacade;
import util.JsfUtil;

/**
 *
 * @author Denis
 */
@ManagedBean(name = "projectController")
@SessionScoped
public class ProjectController implements Serializable {

    private static final Logger log = Logger.getLogger(ProjectController.class.getName());

    @EJB
    private UserFacade userEJB;

    @EJB
    private ProjectFacade projectEJB;

    private List<Project> projects = null;
    private Project selected;
    private User currentUser;

    public List<Project> getProjects() {
        if (projects == null) {
            log.info("Call Method getProjects() for userName: " + "{" + getСurrentUser() + "}");
            projects = projectEJB.findByUser(getСurrentUser());
        }
        return projects;
    }

    public void resetSelected() {
        selected = null;
    }

    private User getСurrentUser() {
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

    public Project getSelected() {
        return selected;
    }

    public void setSelected(Project selected) {
        this.selected = selected;
    }

    public Project prepareCreate() {
        log.info("ProjectController: called method {prepareCreate}");
        this.selected = new Project();
        return this.selected;
    }

    /**
     * Creates a new instance of CreateProjectManagedBean
     */
    public ProjectController() {
    }

    @PostConstruct
    public void init() {
        log.info("ProjectController: projectController bean was created!");
    }

    public void createProject() {
        log.info("Start create project - selected name  {" + selected.getName() + "}");

        if (projectEJB.isExist(selected.getName(), getСurrentUser())) {
            JsfUtil.addErrorMessage("Project with this name exist already. Enter another name!");
        } else {
            try {
                selected.setCreatedDate(new Date());
                selected.setUserName(getСurrentUser());
                projectEJB.create(selected);
                JsfUtil.addSuccessMessage("\"" + selected.getName() + "\" project was created!");

                if (!JsfUtil.isValidationFailed()) {
                    projects = null;
                    selected = null;
                }

            } catch (Exception ex) {
                JsfUtil.addErrorMessage("Error during creating project");
            }
        }
    }

    public void updateProject() {
        if (projectEJB.isExist(selected.getName(), getСurrentUser())) {
            JsfUtil.addErrorMessage("Project with this name exist already. Enter another name!");
        } else {
            try {
                selected.setCreatedDate(new Date());
                projectEJB.edit(selected);
                JsfUtil.addSuccessMessage("\"" + selected.getName() + "\" project was updated!");
            } catch (Exception ex) {
                JsfUtil.addErrorMessage("Error during updating project");
            }
        }
    }

    public void destroy() {
        projectEJB.remove(selected);
        JsfUtil.addSuccessMessage("Project \"" + selected.getName() + "\" was deleted.");
        selected = null;
        projects = null;
    }

}
