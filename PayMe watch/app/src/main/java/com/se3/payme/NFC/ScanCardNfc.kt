package com.se3.payme.NFC

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.pro100svitlo.creditCardNfcReader.utils.CardNfcUtils
import com.se3.payme.NFC.extension.TextRecord
import com.se3.payme.NFC.extension.formattedCardNumber
import com.se3.payme.NFC.extension.longShowSnackBar
import com.se3.payme.R

class ScanCardNfc : AppCompatActivity(), MyCardNfcAsyncTask.MyCardNfcInterface {

    private var mNfcAdapter: NfcAdapter? = null
    private var mCardNfcUtils: CardNfcUtils? = null
    private var mCardNfcAsyncTask: MyCardNfcAsyncTask? = null
    private var mIntentFromCreate: Boolean = false
    private lateinit var mPendingIntent: PendingIntent

    //    private lateinit var mToolbar: Toolbar
    private lateinit var mNFCStatusTextView: TextView
    private lateinit var mCardNumber: TextView
    private lateinit var mCardHolderName: TextView
    private lateinit var mCardExpirationDate: TextView
    private lateinit var mCardType: ImageView
    private lateinit var mTurnNfcDialog: AlertDialog
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mCardContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_card_nfc)
        mNFCStatusTextView = findViewById(R.id.tv_result_nfc)
        mCardNumber = findViewById(R.id.tv_cardNumber)
        mCardHolderName = findViewById(R.id.tv_cardHolderName)
        mCardExpirationDate = findViewById(R.id.tv_cardExpirationDate)
        mCardType = findViewById(R.id.iv_cardType)
        mCardContainer = findViewById(R.id.ll_cardContainer)
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)

        createProgressbar()
        if (checkNFCEnable()) {
            mCardNfcUtils = CardNfcUtils(this)
            mIntentFromCreate = true
            readNFCInfo()
        } else {
            showTurnOnNfcDialog()
        }

    }

    private fun createProgressbar() {
        mProgressBar = ProgressBar(this)
        mProgressBar.isIndeterminate = true
    }


    override fun onStart() {
        super.onStart()
//        mPendingIntent = PendingIntent.getActivity(this, 0, Intent(this, javaClass), 0)
    }

    override fun onResume() {
        super.onResume()
        mIntentFromCreate = false
        if (mNfcAdapter != null) {
            mCardNfcUtils?.enableDispatch()
        }
//        mNfcAdapter?.enableForegroundDispatch(this, mPendingIntent, null, null)
    }


    override fun onPause() {
        super.onPause()
        if (mNfcAdapter != null) {
            mCardNfcUtils?.disableDispatch()
        }
    }


    private fun readNFCInfo() {
        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

//        readNFCTextInfo(intent)
        if (mNfcAdapter != null && checkNFCEnable()) {
            mCardNfcAsyncTask = MyCardNfcAsyncTask.Builder(this, intent, mIntentFromCreate).build()
        }

    }


    /**
     * this function is called to parse intent message to normal string
     * @param intent android intent conclude NdefMessage
     */
    private fun readNFCTextInfo(intent: Intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
                // Process the messages array.
                messages.forEach { message ->
                    message.records.forEach { record ->
                        mNFCStatusTextView.text = TextRecord.parse(record).text
                    }
                }
            }
        }
    }


    private fun showTurnOnNfcDialog() {
        mTurnNfcDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.ad_nfcTurnOn_title))
            .setMessage(getString(R.string.ad_nfcTurnOn_message))
            .setPositiveButton(
                getString(R.string.ad_nfcTurnOn_pos)
            ) { _, _ ->
                if (Build.VERSION.SDK_INT >= 16) {
                    startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
                } else {
                    startActivity(Intent(android.provider.Settings.ACTION_NFC_SETTINGS))
                }
            }.setNegativeButton(getString(R.string.ad_nfcTurnOn_neg)) { _, _ ->
                onBackPressed()
            }
            .create()
        mTurnNfcDialog.show()
    }

    private fun checkNFCEnable(): Boolean {
        return if (mNfcAdapter == null) {
            mNFCStatusTextView.text = getString(R.string.tv_noNfc)
            false
        } else {
            mNfcAdapter!!.isEnabled
        }
    }

    private fun parseCardType(cardType: String?) {
        when (cardType) {
            MyCardNfcAsyncTask.CARD_UNKNOWN -> longShowSnackBar(
                mNFCStatusTextView,
                getString(R.string.snack_unknown_bank_card)
            )
            MyCardNfcAsyncTask.CARD_VISA -> mCardType.setImageResource(R.drawable.ic_visa)
            MyCardNfcAsyncTask.CARD_MASTER_CARD -> mCardType.setImageResource(R.drawable.ic_mastercard)
        }
    }

    override fun startNfcReadCard() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun cardIsReadyToRead() {
        mNFCStatusTextView.visibility = View.GONE
        mProgressBar.visibility = View.GONE
        mCardContainer.visibility = View.VISIBLE
        val cardNumber = formattedCardNumber(mCardNfcAsyncTask?.cardNumber)
        val expirationDate = mCardNfcAsyncTask?.cardExpireDate
        val holderName = mCardNfcAsyncTask?.cardHolderFirstName + mCardNfcAsyncTask?.cardHolderLastName
        val cardType = mCardNfcAsyncTask?.cardType
        mCardNumber.text = cardNumber
        mCardExpirationDate.text = expirationDate
        mCardHolderName.text = holderName
    if (cardNumber?.get(0).toString()=="4"){

        mCardType.setImageResource(R.drawable.ic_visa)}
        else{

        mCardType.setImageResource(R.drawable.ic_mastercard)

        }

        parseCardType(cardType)
    }


    override fun finishNfcReadCard() {
        mCardNfcAsyncTask = null
    }

    override fun cardWithLockedNfc() {
        longShowSnackBar(mNFCStatusTextView, getString(R.string.snack_lockedNfcCard))
    }

    override fun doNotMoveCardSoFast() {
        longShowSnackBar(mNFCStatusTextView, getString(R.string.snack_doNotMoveCard))
    }

    override fun unknownEmvCard() {
        longShowSnackBar(mNFCStatusTextView, getString(R.string.snack_unknownEmv))
    }


}