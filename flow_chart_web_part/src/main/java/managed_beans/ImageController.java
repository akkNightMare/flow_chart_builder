/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managed_beans;

import entities.Clazz;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import session.ClassFacade;
import session.MethodFacade;

/**
 *
 * @author Denis
 */
@ManagedBean(name = "imageController")
@ApplicationScoped
public class ImageController implements Serializable {

    private static final Logger log = Logger.getLogger(ImageController.class.getName());
    @EJB
    private ClassFacade classEJB;

    @EJB
    private MethodFacade methodEJB;

    /**
     * Creates a new instance of ImageController
     */
    public ImageController() {
    }

    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            log.info("ImageController: {Generate right URL was completed!}");
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String classId = context.getExternalContext().getRequestParameterMap().get("classId");
            Clazz clazz = classEJB.find(Integer.valueOf(classId));
            return new DefaultStreamedContent(new ByteArrayInputStream(clazz.getImage()), "image/svg+xml");
        }

    }

}
