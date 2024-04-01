package com.smz.gymplus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;



    @Column(name = "price")
    private Float price;

    @Column(name = "available_stock_quantity")
    private Long availableStockQuantity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    private Set<ProductHistory> ids = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "products")
    @JsonIgnoreProperties(value = { "orderUnits", "products" }, allowSetters = true)
    private Set<OrderUnit> orderUnits = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "names" }, allowSetters = true)
    private Category category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return this.price;
    }

    public Product price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Long getAvailableStockQuantity() {
        return this.availableStockQuantity;
    }

    public Product availableStockQuantity(Long availableStockQuantity) {
        this.setAvailableStockQuantity(availableStockQuantity);
        return this;
    }

    public void setAvailableStockQuantity(Long availableStockQuantity) {
        this.availableStockQuantity = availableStockQuantity;
    }

    public Set<ProductHistory> getIds() {
        return this.ids;
    }

    public void setIds(Set<ProductHistory> productHistories) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setProduct(null));
        }
        if (productHistories != null) {
            productHistories.forEach(i -> i.setProduct(this));
        }
        this.ids = productHistories;
    }

    public Product ids(Set<ProductHistory> productHistories) {
        this.setIds(productHistories);
        return this;
    }

    public Product addId(ProductHistory productHistory) {
        this.ids.add(productHistory);
        productHistory.setProduct(this);
        return this;
    }

    public Product removeId(ProductHistory productHistory) {
        this.ids.remove(productHistory);
        productHistory.setProduct(null);
        return this;
    }

    public Set<OrderUnit> getOrderUnits() {
        return this.orderUnits;
    }

    public void setOrderUnits(Set<OrderUnit> orderUnits) {
        if (this.orderUnits != null) {
            this.orderUnits.forEach(i -> i.setProducts(null));
        }
        if (orderUnits != null) {
            orderUnits.forEach(i -> i.setProducts(this));
        }
        this.orderUnits = orderUnits;
    }

    public Product orderUnits(Set<OrderUnit> orderUnits) {
        this.setOrderUnits(orderUnits);
        return this;
    }

    public Product addOrderUnit(OrderUnit orderUnit) {
        this.orderUnits.add(orderUnit);
        orderUnit.setProducts(this);
        return this;
    }

    public Product removeOrderUnit(OrderUnit orderUnit) {
        this.orderUnits.remove(orderUnit);
        orderUnit.setProducts(null);
        return this;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product category(Category category) {
        this.setCategory(category);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", availableStockQuantity=" + getAvailableStockQuantity() +
            "}";
    }
}
