package com.datajpa.demo.wallet;

import com.datajpa.demo.Transaction.Transaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Wallet name is required")
    @Size(min = 5, max = 25, message = "The size of name should be between 5-25")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Phone number is required")
    @Column(nullable = false)
    private Long phoneNumber;

    @Email(message = "Email should be valid")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password can't be blank")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password should be strong"
    )
    @Column(nullable = false)
    private String password;

    @Min(value = 500, message = "The minimum balance should be 500")
    @Column(nullable = false)
    private Double balance = 500.0;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private String city;

    @Column(nullable = false)
    private boolean isActive = true;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Transaction> transaction = new ArrayList<>();

    public Wallet(Integer id, String name, Long phoneNumber, String email, String password, Double balance,
                  LocalDateTime createdAt, String city, Boolean isActive, List<Transaction> transaction) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.createdAt = createdAt;
        this.city = city;
        this.isActive = Boolean.TRUE.equals(isActive);
        this.transaction = transaction == null ? new ArrayList<>() : transaction;
    }

    public Wallet() {
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.balance == null) {
            this.balance = 500.0;
        }
        if (this.transaction == null) {
            this.transaction = new ArrayList<>();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getActive() {
        return isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        this.isActive = Boolean.TRUE.equals(active);
    }

    public List<Transaction> getTransaction() {
        return transaction;
    }

    public void setTransaction(List<Transaction> transaction) {
        this.transaction = transaction == null ? new ArrayList<>() : transaction;
    }
}
