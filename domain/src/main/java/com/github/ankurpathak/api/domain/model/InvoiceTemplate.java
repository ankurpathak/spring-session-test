package com.github.ankurpathak.api.domain.model;

import com.github.ankurpathak.api.constant.Model;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Document(collection = Model.InvoiceTemplate.INVOICE_TEMPLATE)
public class InvoiceTemplate extends BusinessExtendedDomain<String> {
    private LocalDate issueDate;
    private LocalDate dueDate;
    private List<CustomerId> customerIds;
    private BigDecimal grandTotal = BigDecimal.ZERO;
    private BigDecimal tax = BigDecimal.ZERO;
    private BigDecimal discount = BigDecimal.ZERO;
    private InvoiceParams invoiceParams;
    private List<InvoiceLineItem> invoiceLineItems;
    private Long recurringIntervalInDays = 0L;
    public InvoiceParams getInvoiceParams() {
        return invoiceParams;
    }

    public void setInvoiceParams(InvoiceParams invoiceParams) {
        this.invoiceParams = invoiceParams;
    }

    public List<CustomerId> getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(List<CustomerId> customerIds) {
        this.customerIds = customerIds;
    }


    public Long getRecurringIntervalInDays() {
        return recurringIntervalInDays;
    }

    public void setRecurringIntervalInDays(Long recurringIntervalInDays) {
        this.recurringIntervalInDays = recurringIntervalInDays;
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

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public InvoiceTemplate issueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
        return this;
    }

    public InvoiceTemplate dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public InvoiceTemplate grandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
        return this;
    }

    public InvoiceTemplate products(List<Product> products) {
        this.products = products;
        return this;
    }



    public InvoiceTemplate tax(BigDecimal tax) {
        this.tax = tax;
        return this;
    }

    public InvoiceTemplate discount(BigDecimal discount) {
        this.discount = discount;
        return this;
    }

    public InvoiceTemplate invoiceLineItems(List<InvoiceLineItem> invoiceLineItems) {
        this.invoiceLineItems = invoiceLineItems;
        return this;
    }

    public  static class InvoiceLineItem {
        public String name;
        public BigDecimal amount;
        public BigDecimal discount;
        public BigDecimal tax;
        public Boolean variable = Boolean.FALSE;

        public Boolean getVariable() {
            return variable;
        }

        public void setVariable(Boolean variable) {
            this.variable = variable;
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
