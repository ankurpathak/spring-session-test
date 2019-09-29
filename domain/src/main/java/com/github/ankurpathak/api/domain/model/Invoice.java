package com.github.ankurpathak.api.domain.model;

import com.github.ankurpathak.api.constant.Model;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
@Document(collection = Model.Invoice.INVOICE)
public class Invoice extends BusinessExtendedDomain<String> {
    private String previous;
    private String next;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private CustomerId customerId;
    private BigDecimal grandTotal = BigDecimal.ZERO;
    private BigDecimal amountPaid = BigDecimal.ZERO;
    private BigDecimal tax = BigDecimal.ZERO;
    private BigDecimal discount = BigDecimal.ZERO;
    private BigDecimal penalty = BigDecimal.ZERO;
    private BigDecimal previousDue = BigDecimal.ZERO;
    private BigDecimal previousAdvance = BigDecimal.ZERO;
    private LocalDate lastPaidOn;
    private InvoiceParams invoiceParams;
    private List<InvoiceLineItem> invoiceLineItems;
    private Set<String> urls;

    public void setUrls(Set<String> urls) {
        this.urls = urls;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public List<InvoiceLineItem> getInvoiceLineItems() {
        return invoiceLineItems;
    }

    public void setInvoiceLineItems(List<InvoiceLineItem> invoiceLineItems) {
        this.invoiceLineItems = invoiceLineItems;
    }


    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getPenalty() {
        return penalty;
    }

    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }

    private List<Product> products;

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public void setCustomerId(CustomerId customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public Invoice previous(String previous) {
        this.previous = previous;
        return this;
    }

    public Invoice next(String next) {
        this.next = next;
        return this;
    }

    public Invoice issueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
        return this;
    }

    public Invoice dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public Invoice customerId(CustomerId customerId) {
        this.customerId = customerId;
        return this;
    }

    public Invoice grandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
        return this;
    }

    public Invoice amountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
        return this;
    }



    public Invoice products(List<Product> products) {
        this.products = products;
        return this;
    }

    public Invoice urls(Set<String> urls) {
        this.urls = urls;
        return this;
    }

    public Invoice tax(BigDecimal tax) {
        this.tax = tax;
        return this;
    }

    public Invoice penalty(BigDecimal penalty) {
        this.penalty = penalty;
        return this;
    }



    public Invoice discount(BigDecimal discount) {
        this.discount = discount;
        return this;
    }

    public Invoice invoiceLineItems(List<InvoiceLineItem> invoiceLineItems) {
        this.invoiceLineItems = invoiceLineItems;
        return this;
    }

    public  static class InvoiceLineItem {
        public String name;
        public BigDecimal amount;
        public BigDecimal discount;
        public BigDecimal tax;
        public Product product;
        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getDiscount() {
            return discount;
        }

        public void setDiscount(BigDecimal discount) {
            this.discount = discount;
        }

        public BigDecimal getTax() {
            return tax;
        }

        public void setTax(BigDecimal tax) {
            this.tax = tax;
        }

        public InvoiceLineItem name(String name) {
            this.name = name;
            return this;
        }

        public InvoiceLineItem amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public InvoiceLineItem discount(BigDecimal discount) {
            this.discount = discount;
            return this;
        }

        public InvoiceLineItem tax(BigDecimal tax) {
            this.tax = tax;
            return this;
        }
    }

}
