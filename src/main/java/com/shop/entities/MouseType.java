/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shop.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author frodi
 */
@Entity
@Table(name = "mouse_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MouseType.findAll", query = "SELECT m FROM MouseType m ORDER BY m.name")
    , @NamedQuery(name = "MouseType.findById", query = "SELECT m FROM MouseType m WHERE m.id = :id")
    , @NamedQuery(name = "MouseType.findByName", query = "SELECT m FROM MouseType m WHERE m.name = :name")})
public class MouseType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "type")
    private Collection<Mouse> mouseCollection;
    @OneToMany(mappedBy = "type")
    private Collection<Keyboard> keyboardCollection;

    public MouseType() {
    }

    public MouseType(Integer id) {
        this.id = id;
    }

    public MouseType(Integer id, String name) {
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

    @XmlTransient
    public Collection<Mouse> getMouseCollection() {
        return mouseCollection;
    }

    public void setMouseCollection(Collection<Mouse> mouseCollection) {
        this.mouseCollection = mouseCollection;
    }

    @XmlTransient
    public Collection<Keyboard> getKeyboardCollection() {
        return keyboardCollection;
    }

    public void setKeyboardCollection(Collection<Keyboard> keyboardCollection) {
        this.keyboardCollection = keyboardCollection;
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
        if (!(object instanceof MouseType)) {
            return false;
        }
        MouseType other = (MouseType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
}
