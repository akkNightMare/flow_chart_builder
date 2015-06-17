/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managed_beans;

import drawing.DraftsMan;
import drawing.GraphViz_1;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import session.ClassFacade;
import session.MethodFacade;
import entities.Clazz;
import entities.Method;
import entities.Project;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import parsing.JavaCodeParser;
import parsing.ParsedClass;
import util.JsfUtil;

/**
 *
 * @author Denis
 */
@ManagedBean(name = "classController")
@SessionScoped
public class ClassController implements Serializable {

    private static final Logger log = Logger.getLogger(ClassController.class.getName());

    @EJB
    private ClassFacade classEJB;

    @EJB
    private MethodFacade methodEJB;

    private Integer selectedProjectId;

    private List<Clazz> classes = null;

    private Clazz selected;

    public List<Clazz> getClasses() {
        if (classes == null) {
            log.info("ClassController: getClasses was invoked with ID[" + selectedProjectId + "]");
            return classEJB.findByProjectId(selectedProjectId);
        }
        return classes;
    }

    public Clazz getSelected() {
        return selected;
    }

    public void setSelected(Clazz selected) {
        this.selected = selected;
    }

    /**
     * Creates a new instance of ClassController
     */
    public ClassController() {
    }

    public void setProjectId() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("projectId");
        if (id == null) {
            JsfUtil.addErrorMessage("ClassController: selectedProject id null. Select any project.");
        }
        this.selectedProjectId = Integer.valueOf(id);
    }

    public Clazz prepareCreate() {
        return selected = new Clazz();
    }

    @PostConstruct
    public void init() {
        log.info("ClassControllerBean: bean was created");
    }

    private void initClass() {
        selected.setCreatedDate(new Date());
        selected.setProjectId(new Project(selectedProjectId));
    }

    public String addClass() {
        initClass();
        if (selected == null || "".equals(selected.getSource().trim())) {
            JsfUtil.addErrorMessage("Add Source Code of Class or upload Class from file");
        } else {
            try {
                JavaCodeParser jcp = new JavaCodeParser(selected.getSource());
                List<ParsedClass> parsedClasses = jcp.parse();
                log.info("JavaCodeParser.parse is complited!");
                DraftsMan draftsMan = new DraftsMan(parsedClasses.get(0));
                selected.setName(draftsMan.getClassName());

                if (classEJB.isExist(selected.getName(), selectedProjectId)) {
                    JsfUtil.addErrorMessage("The class already exist in the project. Enter another class name or choose another project.");
                    return "";
                }

                String graphForClassMembers = draftsMan.getGraphForClassMembers();

                byte[] image;
                File graphForClass = GraphViz_1.writeDotGraphToFile("D:\\tmp", draftsMan.getClassName(), "txt", graphForClassMembers);
                image = GraphViz_1.getImageStream(graphForClass, "svg");
                selected.setImage(image);
                log.log(Level.INFO, "Before create...\nselected (name) = {0}\nselected (date) = {1}\nselected (projectId) = {2}", new Object[]{selected.getName(), selected.getCreatedDate(), selected.getProjectId()});
                classEJB.create(selected);
                log.info("graphForClassMembers was written successfully!");

                Map<String, String> methods = draftsMan.buildGraphsForMethods();
                for (Map.Entry<String, String> entry : methods.entrySet()) {
                    String name = entry.getKey();
                    String graph = entry.getValue();
                    File result = GraphViz_1.writeDotGraphToFile("D:\\tmp", name, "txt", graph);
                    image = GraphViz_1.getImageStream(result, "svg");
                    Method method = new Method(name, image, new Clazz(selected.getId()));
                    methodEJB.create(method);
                }

            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, "Error during class parsing");
                return "";
            }
        }
        JsfUtil.addSuccessMessage("Class \"" + selected.getName() + "\" was added successful");
        selected = null;
        return "classeslist.xhtml?faces-redirect=true";
    }

    public void destroy() {
        classEJB.remove(selected);
        JsfUtil.addSuccessMessage("Project \"" + selected.getName() + "\" was deleted.");
        selected = null;
        classes = null;
    }

    public void loadFile(FileUploadEvent event) {
        try {
            InputStream in = event.getFile().getInputstream();
            selected.setSource(new Scanner(in, "UTF-8").useDelimiter("\\A").next());
            in.close();
            JsfUtil.addSuccessMessage("\"" + event.getFile().getFileName() + "\" is uploaded!");
        } catch (IOException ex) {
            log.info("ClassController: Error during loading file!");
        }
    }

}
