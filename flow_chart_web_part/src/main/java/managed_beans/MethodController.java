/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managed_beans;

import entities.Method;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import session.MethodFacade;

/**
 *
 * @author Denis
 */
@ManagedBean(name = "methodController")
@SessionScoped
public class MethodController implements Serializable {

    List<Method> methods;

    Integer imageId;

    @EJB
    MethodFacade methodFacade;

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public List<Method> getMethods() {
        if (methods == null) {
            ClassController classController = (ClassController) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("classController");
            if (classController == null) {
                return null;
            }
            Integer classId = classController.getSelected().getId();
            methods = methodFacade.findByClassId(classId);
        }
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    /**
     * Creates a new instance of MethodController
     */
    public MethodController() {
        methods = null;
    }

    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String id = context.getExternalContext().getRequestParameterMap().get("id");
            Method image = methodFacade.find(Integer.valueOf(id));
            return new DefaultStreamedContent(new ByteArrayInputStream(image.getImage()), "image/svg+xml");
        }
    }

    public void resetMethods(){
        methods = null;
    }

}
