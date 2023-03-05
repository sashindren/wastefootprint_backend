package com.unimap.footprinttest.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductionWaste.
 */
@Entity
@Table(name = "production_waste")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductionWaste implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "material")
    private String material;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "transport_type")
    private String transportType;

    @Column(name = "electric")
    private Integer electric;

    @Column(name = "water")
    private Integer water;

    @Column(name = "waste")
    private String waste;

    @ManyToOne
    private Departments department;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductionWaste id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaterial() {
        return this.material;
    }

    public ProductionWaste material(String material) {
        this.setMaterial(material);
        return this;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public ProductionWaste quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getTransportType() {
        return this.transportType;
    }

    public ProductionWaste transportType(String transportType) {
        this.setTransportType(transportType);
        return this;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public Integer getElectric() {
        return this.electric;
    }

    public ProductionWaste electric(Integer electric) {
        this.setElectric(electric);
        return this;
    }

    public void setElectric(Integer electric) {
        this.electric = electric;
    }

    public Integer getWater() {
        return this.water;
    }

    public ProductionWaste water(Integer water) {
        this.setWater(water);
        return this;
    }

    public void setWater(Integer water) {
        this.water = water;
    }

    public String getWaste() {
        return this.waste;
    }

    public ProductionWaste waste(String waste) {
        this.setWaste(waste);
        return this;
    }

    public void setWaste(String waste) {
        this.waste = waste;
    }

    public Departments getDepartment() {
        return this.department;
    }

    public void setDepartment(Departments departments) {
        this.department = departments;
    }

    public ProductionWaste department(Departments departments) {
        this.setDepartment(departments);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductionWaste)) {
            return false;
        }
        return id != null && id.equals(((ProductionWaste) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductionWaste{" +
            "id=" + getId() +
            ", material='" + getMaterial() + "'" +
            ", quantity=" + getQuantity() +
            ", transportType='" + getTransportType() + "'" +
            ", electric=" + getElectric() +
            ", water=" + getWater() +
            ", waste='" + getWaste() + "'" +
            "}";
    }
}
