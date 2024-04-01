package com.smz.gymplus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A PeriodicSubscription.
 */
@Entity
@Table(name = "periodic_subscription")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PeriodicSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "payment_status")
    private Boolean paymentStatus;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_periodic_subscription__id",
        joinColumns = @JoinColumn(name = "periodic_subscription_id"),
        inverseJoinColumns = @JoinColumn(name = "id_id")
    )
    private Set<User> ids = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ids", "periods" }, allowSetters = true)
    private Plan plans;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ids", "payments" }, allowSetters = true)
    private Invoice invoices;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PeriodicSubscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PeriodicSubscription name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public PeriodicSubscription description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public PeriodicSubscription status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getPaymentStatus() {
        return this.paymentStatus;
    }

    public PeriodicSubscription paymentStatus(Boolean paymentStatus) {
        this.setPaymentStatus(paymentStatus);
        return this;
    }

    public void setPaymentStatus(Boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Set<User> getIds() {
        return this.ids;
    }

    public void setIds(Set<User> users) {
        this.ids = users;
    }

    public PeriodicSubscription ids(Set<User> users) {
        this.setIds(users);
        return this;
    }

    public PeriodicSubscription addId(User user) {
        this.ids.add(user);
        return this;
    }

    public PeriodicSubscription removeId(User user) {
        this.ids.remove(user);
        return this;
    }

    public Plan getPlans() {
        return this.plans;
    }

    public void setPlans(Plan plan) {
        this.plans = plan;
    }

    public PeriodicSubscription plans(Plan plan) {
        this.setPlans(plan);
        return this;
    }

    public Invoice getInvoices() {
        return this.invoices;
    }

    public void setInvoices(Invoice invoice) {
        this.invoices = invoice;
    }

    public PeriodicSubscription invoices(Invoice invoice) {
        this.setInvoices(invoice);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeriodicSubscription)) {
            return false;
        }
        return getId() != null && getId().equals(((PeriodicSubscription) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeriodicSubscription{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            "}";
    }
}
