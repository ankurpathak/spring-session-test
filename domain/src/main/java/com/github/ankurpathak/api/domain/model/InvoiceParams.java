package com.github.ankurpathak.api.domain.model;

import java.math.BigDecimal;

public class InvoiceParams {
    private Boolean previousDueCarryForward = Boolean.FALSE;
    private Boolean previousAdvanceCarryForward = Boolean.TRUE;
    private BigDecimal penalty = BigDecimal.ZERO;
    private Boolean penaltyAbsolute = Boolean.TRUE;

    public Boolean getPreviousDueCarryForward() {
        return previousDueCarryForward;
    }

    public void setPreviousDueCarryForward(Boolean previousDueCarryForward) {
        this.previousDueCarryForward = previousDueCarryForward;
    }

    public Boolean getPreviousAdvanceCarryForward() {
        return previousAdvanceCarryForward;
    }

    public void setPreviousAdvanceCarryForward(Boolean previousAdvanceCarryForward) {
        this.previousAdvanceCarryForward = previousAdvanceCarryForward;
    }

    public BigDecimal getPenalty() {
        return penalty;
    }

    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }

    public Boolean getPenaltyAbsolute() {
        return penaltyAbsolute;
    }

    public void setPenaltyAbsolute(Boolean penaltyAbsolute) {
        this.penaltyAbsolute = penaltyAbsolute;
    }

    public InvoiceParams previousDueCarryForward(Boolean previousDueCarryForward) {
        this.previousDueCarryForward = previousDueCarryForward;
        return this;
    }

    public InvoiceParams previousAdvanceCarryForward(Boolean previousAdvanceCarryForward) {
        this.previousAdvanceCarryForward = previousAdvanceCarryForward;
        return this;
    }

    public InvoiceParams penalty(BigDecimal penalty) {
        this.penalty = penalty;
        return this;
    }

    public InvoiceParams penaltyAbsolute(Boolean penaltyAbsolute) {
        this.penaltyAbsolute = penaltyAbsolute;
        return this;
    }
}
