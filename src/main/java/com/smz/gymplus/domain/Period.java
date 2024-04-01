package com.smz.gymplus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Period.
 */
@Entity
@Table(name = "period")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Period implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "month_occurrence")
    private Integer monthOccurrence;

    @Column(name = "day_occurrence")
    private Integer dayOccurrence;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "periods")
    @JsonIgnoreProperties(value = { "ids", "periods" }, allowSetters = true)
    private Set<Plan> ids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Period id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMonthOccurrence() {
        return this.monthOccurrence;
    }

    public Period monthOccurrence(Integer monthOccurrence) {
        this.setMonthOccurrence(monthOccurrence);
        return this;
    }

    public void setMonthOccurrence(Integer monthOccurrence) {
        this.monthOccurrence = monthOccurrence;
    }

    public Integer getDayOccurrence() {
        return this.dayOccurrence;
    }

    public Period dayOccurrence(Integer dayOccurrence) {
        this.setDayOccurrence(dayOccurrence);
        return this;
    }

    public void setDayOccurrence(Integer dayOccurrence) {
        this.dayOccurrence = dayOccurrence;
    }

    public Set<Plan> getIds() {
        return this.ids;
    }

    public void setIds(Set<Plan> plans) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setPeriods(null));
        }
        if (plans != null) {
            plans.forEach(i -> i.setPeriods(this));
        }
        this.ids = plans;
    }

    public Period ids(Set<Plan> plans) {
        this.setIds(plans);
        return this;
    }

    public Period addId(Plan plan) {
        this.ids.add(plan);
        plan.setPeriods(this);
        return this;
    }

    public Period removeId(Plan plan) {
        this.ids.remove(plan);
        plan.setPeriods(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Period)) {
            return false;
        }
        return getId() != null && getId().equals(((Period) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Period{" +
            "id=" + getId() +
            ", monthOccurrence=" + getMonthOccurrence() +
            ", dayOccurrence=" + getDayOccurrence() +
            "}";
    }
}
