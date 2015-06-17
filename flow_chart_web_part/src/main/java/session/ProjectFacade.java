/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.Project;
import entities.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Denis
 */
@Stateless
public class ProjectFacade extends AbstractFacade<Project> {

    @PersistenceContext(unitName = "com.mycompany_flow_chart_web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProjectFacade() {
        super(Project.class);
    }

    public List<Project> findByProjectName(String projectName) {
        if (projectName == null) {
            return null;
        }
        TypedQuery<Project> query = em.createNamedQuery("Project.findByName", Project.class).setParameter("name", projectName);
        List<Project> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            return null;
        }
        return resultList;
    }

    public List<Project> findByUser(User user) {
        if (user == null) {
            return null;
        }
        TypedQuery<Project> query = em.createNamedQuery("Project.findByUser", Project.class).setParameter("user", user);
        List<Project> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        return resultList;
    }

    public boolean isExist(String projectName, User user) {
//        if (projectName == null || user == null) {
//            throw new NullPointerException();
//        }
        List<Project> projects = findByUser(user);
        if (projects == null) {
            return false;
        }
        for (Project project : projects) {
            if (project.getName().equals(projectName)) {
                return true;
            }
        }
        return false;
    }
}
