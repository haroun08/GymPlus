package com.smz.gymplus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Plan.
 */
@Entity
@Table(name = "plan")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Plan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Float price;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "plans")
    @JsonIgnoreProperties(value = { "ids", "plans", "invoices" }, allowSetters = true)
    private Set<PeriodicSubscription> ids = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ids" }, allowSetters = true)
    private Period periods;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Plan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Plan name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return this.price;
    }

    public Plan price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Set<PeriodicSubscription> getIds() {
        return this.ids;
    }

    public void setIds(Set<PeriodicSubscription> periodicSubscriptions) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setPlans(null));
        }
        if (periodicSubscriptions != null) {
            periodicSubscriptions.forEach(i -> i.setPlans(this));
        }
        this.ids = periodicSubscriptions;
    }

    public Plan ids(Set<PeriodicSubscription> periodicSubscriptions) {
        this.setIds(periodicSubscriptions);
        return this;
    }

    public Plan addId(PeriodicSubscription periodicSubscription) {
        this.ids.add(periodicSubscription);
        periodicSubscription.setPlans(this);
        return this;
    }

    public Plan removeId(PeriodicSubscription periodicSubscription) {
        this.ids.remove(periodicSubscription);
        periodicSubscription.setPlans(null);
        return this;
    }

    public Period getPeriods() {
        return this.periods;
    }

    public void setPeriods(Period period) {
        this.periods = period;
    }

    public Plan periods(Period period) {
        this.setPeriods(period);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plan)) {
            return false;
        }
        return getId() != null && getId().equals(((Plan) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Plan{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
