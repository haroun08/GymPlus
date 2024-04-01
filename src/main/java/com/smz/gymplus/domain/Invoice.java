package com.smz.gymplus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "invoice_date")
    private Instant invoiceDate;

    @Column(name = "pay_amount")
    private Float payAmount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "invoices")
    @JsonIgnoreProperties(value = { "ids", "plans", "invoices" }, allowSetters = true)
    private Set<PeriodicSubscription> ids = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "invoice")
    @JsonIgnoreProperties(value = { "invoice", "order" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Invoice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getInvoiceDate() {
        return this.invoiceDate;
    }

    public Invoice invoiceDate(Instant invoiceDate) {
        this.setInvoiceDate(invoiceDate);
        return this;
    }

    public void setInvoiceDate(Instant invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Float getPayAmount() {
        return this.payAmount;
    }

    public Invoice payAmount(Float payAmount) {
        this.setPayAmount(payAmount);
        return this;
    }

    public void setPayAmount(Float payAmount) {
        this.payAmount = payAmount;
    }

    public Set<PeriodicSubscription> getIds() {
        return this.ids;
    }

    public void setIds(Set<PeriodicSubscription> periodicSubscriptions) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setInvoices(null));
        }
        if (periodicSubscriptions != null) {
            periodicSubscriptions.forEach(i -> i.setInvoices(this));
        }
        this.ids = periodicSubscriptions;
    }

    public Invoice ids(Set<PeriodicSubscription> periodicSubscriptions) {
        this.setIds(periodicSubscriptions);
        return this;
    }

    public Invoice addId(PeriodicSubscription periodicSubscription) {
        this.ids.add(periodicSubscription);
        periodicSubscription.setInvoices(this);
        return this;
    }

    public Invoice removeId(PeriodicSubscription periodicSubscription) {
        this.ids.remove(periodicSubscription);
        periodicSubscription.setInvoices(null);
        return this;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setInvoice(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setInvoice(this));
        }
        this.payments = payments;
    }

    public Invoice payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Invoice addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setInvoice(this);
        return this;
    }

    public Invoice removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setInvoice(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return getId() != null && getId().equals(((Invoice) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", payAmount=" + getPayAmount() +
            "}";
    }
}
