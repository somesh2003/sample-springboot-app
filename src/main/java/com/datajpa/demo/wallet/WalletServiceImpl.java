package com.datajpa.demo.wallet;

import com.datajpa.demo.Transaction.Transaction;
import com.datajpa.demo.Transaction.TransactionRespository;
import com.datajpa.demo.Transaction.TransactionType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRespository transactionRespository;

    public WalletServiceImpl(WalletRepository walletRepository, TransactionRespository transactionRespository) {
        this.walletRepository = walletRepository;
        this.transactionRespository = transactionRespository;
    }

    @Override
    public Wallet registerNewWalletUser(Wallet newWallet) {
        if (newWallet == null) {
            throw new WalletException("Wallet details are required");
        }
        if (newWallet.getEmail() != null && walletRepository.findByEmail(newWallet.getEmail()).isPresent()) {
            throw new WalletException("Email already exists");
        }
        newWallet.setCreatedAt(LocalDateTime.now());
        return walletRepository.save(newWallet);
    }

    @Override
    public Wallet getUserWalletById(Integer walletID) {
        return walletRepository.findById(walletID)
                .orElseThrow(() -> new WalletException("Wallet ID " + walletID + " Not Found"));
    }

    @Override
    public Wallet updateUserWallet(Integer id) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new WalletException("Wallet ID " + id + " Not Found"));
        wallet.setCreatedAt(LocalDateTime.now());
        wallet.setBalance(wallet.getBalance() == null ? 10000.0 : wallet.getBalance());
        return walletRepository.save(wallet);
    }

    @Override
    public Double addFundsToWalletByID(Integer fromID, Integer toID, Double balance) {
        Wallet wallet = walletRepository.findById(toID)
                .orElseThrow(() -> new WalletException("Wallet ID " + toID + " Not Found"));
        if (balance == null || balance <= 0) {
            throw new WalletException("Transfer amount must be greater than zero");
        }
        wallet.setBalance((wallet.getBalance() == null ? 0.0 : wallet.getBalance()) + balance);
        return walletRepository.save(wallet).getBalance();
    }

    @Override
    public Double withdrawFundsToWalletByID(Integer ID, Double amount) {
        Wallet wallet = walletRepository.findById(ID)
                .orElseThrow(() -> new WalletException("Wallet ID " + ID + " Not Found"));
        if (amount == null || amount <= 0) {
            throw new WalletException("Withdrawal amount must be greater than zero");
        }
        if (wallet.getBalance() == null || wallet.getBalance() < amount) {
            throw new WalletException("Wallet Balance Not Enough:" + (wallet.getBalance() == null ? 0.0 : wallet.getBalance()));
        }
        wallet.setBalance(wallet.getBalance() - amount);
        walletRepository.save(wallet);
        return wallet.getBalance();
    }

    @Override
    public Boolean fundTransfer(Integer fromID, Integer toID, Double balance) {
        if (fromID.equals(toID)) {
            throw new WalletException("Wallet source and destination cannot be the same");
        }
        if (balance == null || balance <= 0) {
            throw new WalletException("Transfer amount must be greater than zero");
        }

        Wallet destinationWallet = walletRepository.findById(toID)
                .orElseThrow(() -> new WalletException("Wallet ID " + toID + " Not Found"));
        Wallet sourceWallet = walletRepository.findById(fromID)
                .orElseThrow(() -> new WalletException("Wallet ID " + fromID + " Not Found"));

        if (sourceWallet.getBalance() == null || sourceWallet.getBalance() < balance) {
            throw new WalletException("Wallet Balance Not Enough:" + (sourceWallet.getBalance() == null ? 0.0 : sourceWallet.getBalance()));
        }

        sourceWallet.setBalance(sourceWallet.getBalance() - balance);
        Transaction debitTransaction = Transaction.builder()
                .trans_datetime(LocalDateTime.now())
                .created_at(LocalDateTime.now())
                .trans_amount(balance)
                .transactionType(TransactionType.DEBIT)
                .transaction_status("Success")
                .build();
        transactionRespository.save(debitTransaction);
        sourceWallet.getTransaction().add(debitTransaction);

        destinationWallet.setBalance((destinationWallet.getBalance() == null ? 0.0 : destinationWallet.getBalance()) + balance);
        Transaction creditTransaction = Transaction.builder()
                .trans_datetime(LocalDateTime.now())
                .created_at(LocalDateTime.now())
                .trans_amount(balance)
                .transactionType(TransactionType.CREDIT)
                .transaction_status("Success")
                .build();
        transactionRespository.save(creditTransaction);
        destinationWallet.getTransaction().add(creditTransaction);

        walletRepository.save(sourceWallet);
        walletRepository.save(destinationWallet);
        return true;
    }

    @Override
    public Boolean deactivateWalletByID(Integer ID) {
        Wallet wallet = walletRepository.findById(ID)
                .orElseThrow(() -> new WalletException("Wallet ID " + ID + " Not Found"));
        if (wallet.getActive() != null && wallet.getActive()) {
            wallet.setActive(false);
            walletRepository.save(wallet);
            return true;
        }
        throw new WalletException("Wallet is already Deactivate");
    }

    @Override
    public Boolean activateWalletByID(Integer ID) {
        Wallet wallet = walletRepository.findById(ID)
                .orElseThrow(() -> new WalletException("Wallet ID " + ID + " Not Found"));
        if (wallet.getActive() != null && !wallet.getActive()) {
            wallet.setActive(true);
            walletRepository.save(wallet);
            return true;
        }
        throw new WalletException("Wallet is already Activate");
    }

    @Override
    public List<Wallet> getAllWallet() {
        return walletRepository.findAll();
    }
}
