package com.datajpa.demo.wallet;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/wallet")
@CrossOrigin
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping
    public String hello() {
        return "hello";
    }

    @PostMapping("/register")
    public Wallet registerNewWallet(@Valid @RequestBody Wallet newWallet) {
        return walletService.registerNewWalletUser(newWallet);
    }

    @GetMapping("/{walletID}")
    public Wallet getWalletByID(@PathVariable("walletID") Integer walletID) {
        return walletService.getUserWalletById(walletID);
    }

    @PatchMapping("/update")
    public Wallet updateUserWallet(@Valid @RequestBody WalletDto walletDto) {
        return walletService.updateUserWallet(walletDto.getFromid());
    }

    @PatchMapping("/addFund")
    public Double addFundsToWalletByID(@Valid @RequestBody WalletDto walletDto) {
        return walletService.addFundsToWalletByID(walletDto.getFromid(), walletDto.getToid(), walletDto.getAmount());
    }

    @PatchMapping("/withdraw")
    public Double withdrawFundsToWalletByID(@Valid @RequestBody WalletDto walletDto) {
        return walletService.withdrawFundsToWalletByID(walletDto.getFromid(), walletDto.getAmount());
    }

    @PatchMapping("/transferFund")
    public Boolean fundTransfer(@Valid @RequestBody WalletDto walletDto) {
        return walletService.fundTransfer(walletDto.getFromid(), walletDto.getToid(), walletDto.getAmount());
    }

    @PostMapping("/deactivate/{walletID}")
    public Boolean deactivateWalletByID(@PathVariable("walletID") Integer walletID) {
        return walletService.deactivateWalletByID(walletID);
    }

    @PostMapping("/activate/{walletID}")
    public Boolean activateWalletByID(@PathVariable("walletID") Integer walletID) {
        return walletService.activateWalletByID(walletID);
    }

    @GetMapping("/allwallets")
    public List<Wallet> getAllWallets() {
        return walletService.getAllWallet();
    }
}

