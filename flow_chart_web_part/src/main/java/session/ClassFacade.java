/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.Clazz;
import entities.Project;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author Denis
 */
@Stateless
public class ClassFacade extends AbstractFacade<Clazz> {

    @PersistenceContext(unitName = "com.mycompany_flow_chart_web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClassFacade() {
        super(Clazz.class);
    }

    public List<Clazz> findByProjectId(Integer projectId) {
        if (projectId == null) {
            return null;
        }
        Project searchProject = new Project(projectId);
        TypedQuery<Clazz> query = em.createNamedQuery("Clazz.findByProject", Clazz.class).setParameter("project", searchProject);
        List<Clazz> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        return resultList;
    }

    public boolean isExist(String className, Integer projectId) {
        Project project = new Project(projectId);
        Query query = em.createQuery("SELECT c FROM Clazz c WHERE c.projectId = :project and c.name = :name", Clazz.class);
        query.setParameter("project", project);
        query.setParameter("name", className);
        List<Clazz> resultList = query.getResultList();
        return !resultList.isEmpty();
    }

}
