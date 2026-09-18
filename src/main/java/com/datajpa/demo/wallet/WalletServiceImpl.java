package com.datajpa.demo.wallet;

import com.datajpa.demo.Transaction.Transaction;
import com.datajpa.demo.Transaction.TransactionRespository;
import com.datajpa.demo.Transaction.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.datajpa.demo.Transaction.Transaction.*;

@Service
public class WalletServiceImpl implements WalletService {

    private WalletRepository walletRepository;
    private TransactionRespository transactionRespository;
    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository, TransactionRespository transactionRespository) {
        this.walletRepository = walletRepository;
        this.transactionRespository = transactionRespository;
    }

    @Override
    public Wallet registerNewWalletUser(Wallet newWallet) {
       if (this.walletRepository.findByEmail(newWallet.getEmail()).isPresent()){
            throw new WalletException("Email already exists");
        }

        newWallet.setCreatedAt(LocalDateTime.now());
        return this.walletRepository.save(newWallet);
    }

    @Override
    public Wallet getUserWalletById(Integer walletID) {
        Optional<Wallet> foundWallet = Optional.of(this.walletRepository.findById(walletID).orElseThrow(() -> new WalletException("Wallet ID" + walletID + " Not Found")));
        if (foundWallet.isPresent()) {
            return foundWallet.get();
        }
        return null;
    }

    @Override
    public Wallet updateUserWallet(Integer ID) {
        Wallet wallet = this.walletRepository.findById(ID).orElseThrow(() -> new WalletException("Wallet ID"+ ID+" Not Found"));
        if (wallet != null) {
            wallet.setCreatedAt(LocalDateTime.now());
            wallet.setBalance(10000.0);
            return this.walletRepository.save(wallet);
        }
        return null;
    }

    @Override
    public Double addFundsToWalletByID(Integer fromID,Integer toID, Double balance) {
        Wallet foundWallettoID = walletRepository.findById(toID).orElseThrow(() -> new WalletException("Wallet ID " + toID + " Not Found"));
        Double oldbalnce = foundWallettoID.getBalance();
        foundWallettoID.setBalance((oldbalnce + balance));
        return this.walletRepository.save(foundWallettoID).getBalance();
    }

    @Override
    public Double withdrawFundsToWalletByID(Integer ID, Double amount) {
        Wallet foundWallet= this.walletRepository.findById(ID).orElse(null);
        if (foundWallet.getBalance()<amount) {
            throw new WalletException("Wallet Balance Not Enough:" + foundWallet.getBalance());
        }
        Double currentbalace = foundWallet.getBalance();
        foundWallet.setBalance(currentbalace - amount);
        this.walletRepository.save(foundWallet);
        return foundWallet.getBalance();
    }

    @Override
    public Boolean fundTransfer(Integer fromID, Integer toID, Double balance) {
        Wallet foundWallettoID = this.walletRepository.findById(toID).orElse(null);
        Wallet foundWalletfromID = this.walletRepository.findById(fromID).orElse(null);
        if (foundWallettoID != null && foundWalletfromID != null) {

        if (foundWalletfromID.getBalance()<balance) {
            throw new WalletException("Wallet Balance Not Enough:"+foundWalletfromID.getBalance());
        }
        Double oldbalancefromID = foundWalletfromID.getBalance();
        foundWalletfromID.setBalance((oldbalancefromID - balance));

        Transaction newDebitTransaction =  builder().trans_datetime(LocalDateTime.now()).
                    created_at(LocalDateTime.now()).trans_amount(balance).transactionType(TransactionType.DEBIT).
                    transaction_status("Success").build();
        newDebitTransaction= transactionRespository.save(newDebitTransaction);
        foundWalletfromID.getTransaction().add(newDebitTransaction);

        Double oldbalnce = foundWallettoID.getBalance();
        foundWallettoID.setBalance((oldbalnce + balance));

        Transaction newCreditTransaction = builder().trans_datetime(LocalDateTime.now()).
                created_at(LocalDateTime.now()).trans_amount(balance).transactionType(TransactionType.CREDIT).
                transaction_status("Success").build();
        newCreditTransaction= transactionRespository.save(newCreditTransaction);
        foundWallettoID.getTransaction().add(newCreditTransaction);

        this.walletRepository.save(foundWalletfromID);
        this.walletRepository.save(foundWallettoID);
        return true;
        }
        return false;
    }

    @Override
    public Boolean deactivateWalletByID(Integer ID) {
        Optional<Wallet> foundWallet = Optional.of(this.walletRepository.findById(ID).orElseThrow(() -> new WalletException("Wallet ID " + ID + " Not Found")));
        if  (foundWallet.isPresent()) {
            if(foundWallet.get().getActive()==true) {
                foundWallet.get().setActive(false);
                this.walletRepository.save(foundWallet.get());
            }
            else {
                throw new WalletException("Wallet is already Deactivate");
            }
            return true;
        }
        return null;
    }

    @Override
    public Boolean activateWalletByID(Integer ID) {
        Optional<Wallet> foundWallet = Optional.of(this.walletRepository.findById(ID).orElseThrow(() -> new WalletException("Wallet ID " + ID + " Not Found")));
        if (foundWallet.isPresent()) {
            if(foundWallet.get().getActive()==false) {
                foundWallet.get().setActive(true);
                this.walletRepository.save(foundWallet.get());
            }
            else {
                throw new WalletException("Wallet is already Activate");
            }
            return true;
        }
        return null;
    }

    @Override
    public List<Wallet> getAllWallet() {
        return this.walletRepository.findAll();
    }

}
