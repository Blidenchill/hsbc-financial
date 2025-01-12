package com.hsbc.financial.application.pressure;

import com.github.javafaker.Faker;
import com.hsbc.financial.domain.account.entity.Account;
import com.hsbc.financial.domain.transaction.command.TransactionCommand;
import com.hsbc.financial.domain.transaction.entity.TransactionEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * DataGenerator
 *
 * @author zhaoyongping
 * @date 2025/1/12
 * @className DataGenerator
 **/
public class DataGenerator {

    private static final Faker faker = new Faker();

    public static final List<String> ACCOUNT_LIST = Arrays.asList(
            "LV59VHFW6q10tlN41qY66",
            "AZ27UINT3F90wI2iz4Lk1DD52G2N",
            "CY06466167871w29I2BpnwTL5ZPT",
            "AZ93UQEHz98pnNO52id0wTZCwF4C",
            "LV60QGRITqCrCUC24WCC7",
            "XK284576568997572433",
            "IE26OUBA21657711840350",
            "GB92QJXV67514991370510",
            "AL333639225956N8F7V0Vxb71Vpl",
            "AZ96AAST8sGMH8041apa9EE3J0ac",
            "CY1984742639b4DM9gbog11Ay604",
            "RS59142674080692300855",
            "RO49NHBRqiOdW4N6Dl3mvLK4",
            "NO8082649579689",
            "VG75QAQH6514738356823640",
            "MT09NWJO88391vChREFVnO27Si9T1L3",
            "GI57FRYCMx17wkTjN110NV8",
            "BA425975653306544606",
            "SE7092275042734538283140",
            "PT92791701278842442863279",
            "MC046212337846X822Z9lTwO796",
            "SA0273ID9AaiSX2Krw465mA1",
            "MR2502770230380508013358897",
            "RO70PAFC3JO1JDzu724OUNp7",
            "MT21AWIQ41593f5sq4s2Z0bHCS75c7G",
            "SI64037300589101787",
            "GE69IP0972977403909056",
            "IT76O6384264949WtMiyr24y6d4",
            "LV16HLTJ9B8wgHA1NIsst",
            "PS78ACPO29S385CqEK9MmeM0YS8AR",
            "BG42QFSP097854vG4fHa8M",
            "DK6023279187251085",
            "BA733407791269167168",
            "SK3658610372638592796490",
            "MD56gJ3IDL4O6H5cSuviN0Gh",
            "HR4887861715330118942",
            "LB466301Aq050T2v6msw2B5qE2V7",
            "BG51JQNF529968VFHPc62g",
            "BH73CYNH3Om9J1UqVI0L6Z",
            "GI51ZVPOTga3P2U6aCQvt4m",
            "SM97J52976369497dMNlB42Ih6m",
            "GE91VL9128489630903652",
            "LI4409235Cveddbei58e8",
            "DE89683090840171719632",
            "IE28WJRG70513532575854",
            "NO9179467989579",
            "BR8587903819919903491758778JV",
            "MU87UIOG5534165924430354698UUU",
            "LC03IYZQa1v0SwK8X7bxobI77X6bHqYP",
            "UA38347642v6I1kFeiq10qC0DvjVY"
    );

    public static List<TransactionEvent> generateTransactions(int count) {
        List<TransactionEvent> transactions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            TransactionEvent transaction = new TransactionEvent();
            transaction.setTransactionId(faker.idNumber().valid());
            transaction.setSourceAccountId(faker.finance().iban());
            transaction.setDestAccountId(faker.finance().iban());
            transaction.setAmount(BigDecimal.valueOf(faker.number().randomDouble(2, 10, 100)));
            transactions.add(transaction);
        }
        return transactions;
    }


    public static List<Account> generateAccounts(int count) {
        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Account account = new Account();
            account.setAccountId(faker.finance().iban());
            account.setBalance(BigDecimal.valueOf(faker.number().randomDouble(2, 5000, 20000)));
            account.setCreatedAt(new Timestamp(faker.date().past(365, TimeUnit.DAYS).getTime()));
            account.setUpdatedAt(new Timestamp(faker.date().past(365, TimeUnit.DAYS).getTime()));
            account.setIsDeleted(false);
            account.setAccountName(faker.letterify("???????"));
            accounts.add(account);
        }
        return accounts;
    }

    public static String getAccountId() {
        int i = faker.number().numberBetween(0, ACCOUNT_LIST.size() - 1);
        return ACCOUNT_LIST.get(i);

    }
    public static TransactionCommand generateTransactionCommand() {
        TransactionCommand transactionCommand = new TransactionCommand();
        transactionCommand.setSourceAccountId(getAccountId());
        transactionCommand.setDestAccountId(getAccountId());
        transactionCommand.setAmount(BigDecimal.valueOf(faker.number().randomDouble(2, 10, 100)));
        transactionCommand.setTransactionId(faker.idNumber().valid());
        transactionCommand.setTimestamp(new Timestamp(new java.util.Date().getTime()));
        return transactionCommand;

    }

}
