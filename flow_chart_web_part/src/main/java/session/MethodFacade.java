/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.Clazz;
import entities.Method;
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
public class MethodFacade extends AbstractFacade<Method> {

    @PersistenceContext(unitName = "com.mycompany_flow_chart_web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MethodFacade() {
        super(Method.class);
    }

    public List<Method> findByClassId(Integer classId) {
        if (classId == null) {
            return null;
        }
        Clazz searchedClass = new Clazz(classId);
        TypedQuery<Method> query = em.createNamedQuery("Method.findByClass", Method.class).setParameter("searchedClass", searchedClass);
        List<Method> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        return resultList;
    }

}
