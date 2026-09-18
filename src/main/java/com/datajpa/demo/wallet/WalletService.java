package com.datajpa.demo.wallet;

import java.util.List;

public interface WalletService {

    Wallet registerNewWalletUser(Wallet newWallet);
    Wallet getUserWalletById(Integer walletId);
    Wallet updateUserWallet(Integer ID);

    Double addFundsToWalletByID(Integer fromID,Integer toID,Double balance);
    Double withdrawFundsToWalletByID(Integer ID,Double amount);

    Boolean fundTransfer(Integer fromID,Integer toID, Double balance);
    Boolean deactivateWalletByID(Integer ID);
    Boolean activateWalletByID(Integer ID);
    List<Wallet> getAllWallet();

}
