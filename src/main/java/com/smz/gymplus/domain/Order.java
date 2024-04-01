package com.smz.gymplus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "order_date")
    private Instant orderDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderUnits")
    @JsonIgnoreProperties(value = { "orderUnits", "products" }, allowSetters = true)
    private Set<OrderUnit> ids = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @JsonIgnoreProperties(value = { "invoice", "order" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Order id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Order name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public Order orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Set<OrderUnit> getIds() {
        return this.ids;
    }

    public void setIds(Set<OrderUnit> orderUnits) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setOrderUnits(null));
        }
        if (orderUnits != null) {
            orderUnits.forEach(i -> i.setOrderUnits(this));
        }
        this.ids = orderUnits;
    }

    public Order ids(Set<OrderUnit> orderUnits) {
        this.setIds(orderUnits);
        return this;
    }

    public Order addId(OrderUnit orderUnit) {
        this.ids.add(orderUnit);
        orderUnit.setOrderUnits(this);
        return this;
    }

    public Order removeId(OrderUnit orderUnit) {
        this.ids.remove(orderUnit);
        orderUnit.setOrderUnits(null);
        return this;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setOrder(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setOrder(this));
        }
        this.payments = payments;
    }

    public Order payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Order addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setOrder(this);
        return this;
    }

    public Order removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setOrder(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return getId() != null && getId().equals(((Order) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            "}";
    }
}
