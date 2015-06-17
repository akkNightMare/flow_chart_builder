/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Denis
 */
@Entity
@Table(name = "classes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clazz.findAll", query = "SELECT c FROM Clazz c"),
    @NamedQuery(name = "Clazz.findById", query = "SELECT c FROM Clazz c WHERE c.id = :id"),
    @NamedQuery(name = "Clazz.findByName", query = "SELECT c FROM Clazz c WHERE c.name = :name"),
    @NamedQuery(name = "Clazz.findBySource", query = "SELECT c FROM Clazz c WHERE c.source = :source"),
    @NamedQuery(name = "Clazz.findByCreatedDate", query = "SELECT c FROM Clazz c WHERE c.createdDate = :createdDate"),
    @NamedQuery(name = "Clazz.findByProject", query = "SELECT c FROM Clazz c WHERE c.projectId = :project")})
public class Clazz implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "source")
    private String source;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Basic(optional = false)
    @NotNull
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @JoinColumn(name = "project_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Project projectId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "classId", orphanRemoval = true)
    private Collection<Method> methods;

    public Clazz() {
    }

    @XmlTransient
    public Collection<Method> getMethods() {
        return methods;
    }

    public void setMethods(Collection<Method> methods) {
        this.methods = methods;
    }

    public Clazz(Integer id) {
        this.id = id;
    }

    public Clazz(Integer id, String name, String source, Date createdDate) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.createdDate = createdDate;
    }

    public Project getProjectId() {
        return projectId;
    }

    public void setProjectId(Project projectId) {
        this.projectId = projectId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clazz)) {
            return false;
        }
        Clazz other = (Clazz) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Class[ id=" + id + " ]";
    }

}
