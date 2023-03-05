package com.unimap.footprinttest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.unimap.footprinttest.domain.ProductionWaste} entity. This class is used
 * in {@link com.unimap.footprinttest.web.rest.ProductionWasteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /production-wastes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductionWasteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter material;

    private IntegerFilter quantity;

    private StringFilter transportType;

    private IntegerFilter electric;

    private IntegerFilter water;

    private StringFilter waste;

    private LongFilter departmentId;

    private Boolean distinct;

    public ProductionWasteCriteria() {}

    public ProductionWasteCriteria(ProductionWasteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.material = other.material == null ? null : other.material.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.transportType = other.transportType == null ? null : other.transportType.copy();
        this.electric = other.electric == null ? null : other.electric.copy();
        this.water = other.water == null ? null : other.water.copy();
        this.waste = other.waste == null ? null : other.waste.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductionWasteCriteria copy() {
        return new ProductionWasteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMaterial() {
        return material;
    }

    public StringFilter material() {
        if (material == null) {
            material = new StringFilter();
        }
        return material;
    }

    public void setMaterial(StringFilter material) {
        this.material = material;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            quantity = new IntegerFilter();
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public StringFilter getTransportType() {
        return transportType;
    }

    public StringFilter transportType() {
        if (transportType == null) {
            transportType = new StringFilter();
        }
        return transportType;
    }

    public void setTransportType(StringFilter transportType) {
        this.transportType = transportType;
    }

    public IntegerFilter getElectric() {
        return electric;
    }

    public IntegerFilter electric() {
        if (electric == null) {
            electric = new IntegerFilter();
        }
        return electric;
    }

    public void setElectric(IntegerFilter electric) {
        this.electric = electric;
    }

    public IntegerFilter getWater() {
        return water;
    }

    public IntegerFilter water() {
        if (water == null) {
            water = new IntegerFilter();
        }
        return water;
    }

    public void setWater(IntegerFilter water) {
        this.water = water;
    }

    public StringFilter getWaste() {
        return waste;
    }

    public StringFilter waste() {
        if (waste == null) {
            waste = new StringFilter();
        }
        return waste;
    }

    public void setWaste(StringFilter waste) {
        this.waste = waste;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public LongFilter departmentId() {
        if (departmentId == null) {
            departmentId = new LongFilter();
        }
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductionWasteCriteria that = (ProductionWasteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(material, that.material) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(transportType, that.transportType) &&
            Objects.equals(electric, that.electric) &&
            Objects.equals(water, that.water) &&
            Objects.equals(waste, that.waste) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, material, quantity, transportType, electric, water, waste, departmentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductionWasteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (material != null ? "material=" + material + ", " : "") +
            (quantity != null ? "quantity=" + quantity + ", " : "") +
            (transportType != null ? "transportType=" + transportType + ", " : "") +
            (electric != null ? "electric=" + electric + ", " : "") +
            (water != null ? "water=" + water + ", " : "") +
            (waste != null ? "waste=" + waste + ", " : "") +
            (departmentId != null ? "departmentId=" + departmentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
