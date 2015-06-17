/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Denis
 */
@Entity
@Table(name = "methods")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Method.findAll", query = "SELECT m FROM Method m"),
    @NamedQuery(name = "Method.findById", query = "SELECT m FROM Method m WHERE m.id = :id"),
    @NamedQuery(name = "Method.findByName", query = "SELECT m FROM Method m WHERE m.name = :name"),
    @NamedQuery(name = "Method.findByClass", query = "SELECT m FROM Method m WHERE m.classId = :searchedClass")})
public class Method implements Serializable {

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

    @Lob
    @Column(name = "image")
    private byte[] image;

    @JoinColumn(name = "class_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Clazz classId;

    public Method(String name, byte[] image, Clazz classId) {
        this.name = name;
        this.image = image;
        this.classId = classId;
    }

    public Method() {
    }

    public Method(Integer id) {
        this.id = id;
    }

    public Method(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Clazz getClassId() {
        return classId;
    }

    public void setClassId(Clazz classId) {
        this.classId = classId;
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
        if (!(object instanceof Method)) {
            return false;
        }
        Method other = (Method) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Method[ id=" + id + " ]";
    }

}
