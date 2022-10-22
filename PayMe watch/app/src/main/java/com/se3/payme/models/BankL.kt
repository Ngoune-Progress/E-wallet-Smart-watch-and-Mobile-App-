package com.se3.payme.models

import java.util.*

class BankL(var name:String) {

    object bankObect{

        private val bankName = listOf(
            "Access Bank Cameroon",
                    "Afriland First Bank",
                    "Atlantic Bank Cameroon",
                    "Banque International du Cameroun pour l'Epargne et le Crédit (BICEC)",
                    "Banque Camerounaise des Petites et Moyennes Entreprises (BC-PME SA)",
                    "BGFI Bank Cameroon",
                    "SCB Cameroun",
                    "Crédit Communautaire d'Afrique Bank (CCA Bank)",
                    "Citibank",
                    "Commercial Bank of Cameroon",
                    "Ecobank Cameroon - Acquired Oceanic Bank Cameroon",
                    "National Financial Credit Bank (NFCB)",
                    "Société Commerciale de Banque du Cameroun - (Formerly SCB Credit Agricole)",
                    "Wineex Bank Cameroon (WBC)",
                    "Societe Generale des Banques au Cameroun (SGBC)",
                    "Standard Chartered Bank",
                    "Union Bank of Cameroon (UBC)",
                    "United Bank for Africa (UBA)",
                    "Attijari Securities Central Africa (ASCA)"
        )

        var bankList: ArrayList<BankL>? = null
            get() {

                if (field != null)      // backing 'field' refers to 'cityList' property object
                    return field

                field = ArrayList()
                for (i in BankL.bankObect.bankName.indices) {

                    val name = BankL.bankObect.bankName[i]
                    val bank = BankL(name)
                    field!!.add(bank)
                }

                return field
            }

    }
}