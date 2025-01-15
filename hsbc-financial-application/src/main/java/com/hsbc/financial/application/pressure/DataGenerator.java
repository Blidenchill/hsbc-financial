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
            "HU13628283364276023385599797",
            "DO37UQzH55396938148626586068",
            "GT43HyT1cPIemJ4X87F0h0VwErag",
            "SA8380hEDtZbw668N60938Qy",
            "EE430913942278241418",
            "GB39IMKJ87966995830495",
            "BG71JNSY052055zVgO10F1",
            "RS29098784841754421065",
            "KW56HUPOP3t85Q79sf1d0B6i3o18pp",
            "PT57393256805801202325767",
            "ME81451865441863304165",
            "TL258811401702535994848",
            "LU928766FIWsxy8b0HzN",
            "VG62TRFP4899873599450126",
            "IL498513381055467244183",
            "AZ67NONYj0MEP0dS1tO3mVkKYlwK",
            "XK323768065047958153",
            "UA3347704196fd8YvP864t6L01uZF",
            "MK03202mqJsF46U3x83",
            "RS45878473117139243210",
            "BE95921827202960",
            "AL1177290792S4CM2eL9xb0GG9gP",
            "NL46EHVC1429820960",
            "RS56956815841367829987",
            "MT42TXWL93474bN3R1E1X1j989MFkfA",
            "MU48RLKW2067699653856444287FWZ",
            "VG09AFRR3938467812787866",
            "ME34790091526615655563",
            "AT834848477262206983",
            "AD4276015109wGL91H2NYPI6",
            "QA52UTVQ467m67JuaAt6kk0YO2E8n",
            "ME09631786466960256617",
            "LT278782450613197666",
            "LV18ADHVb1193vm1u2wXc",
            "AE846877468609318885952",
            "ST50583559175266890152346",
            "IT22W6431810441VZd9f7LmthlS",
            "VG34EYYI3619337929018248",
            "TL111527828800238386366",
            "BG20WSPX030637EF8m9HXt",
            "DK0302544865515487",
            "VG64MWYG6939443524185611",
            "AZ37ZIUQ1fw248l8xGY782K8AD6Q",
            "UA91994179wSsjZ4i5o6i87K7Ky0P",
            "ST88192839604779330638193",
            "LT201186645247753698",
            "FI1212263314751082",
            "LI4535263g6696H9K297p",
            "BE04883860208976",
            "RO76MLKRQ2QEQyBZ429D8ZF9"
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
