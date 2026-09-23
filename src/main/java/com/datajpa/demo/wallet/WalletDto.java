package com.datajpa.demo.wallet;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class WalletDto {
    @NotNull(message = "From wallet ID is required")
    private Integer fromid;

    private Integer toid;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    public WalletDto() {
    }

    public WalletDto(Integer fromid, Integer toid, Double amount) {
        this.fromid = fromid;
        this.toid = toid;
        this.amount = amount;
    }

    public Integer getFromid() {
        return fromid;
    }

    public void setFromid(Integer fromid) {
        this.fromid = fromid;
    }

    public Integer getToid() {
        return toid;
    }

    public void setToid(Integer toid) {
        this.toid = toid;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
