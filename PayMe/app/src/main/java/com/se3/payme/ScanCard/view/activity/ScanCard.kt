package com.muryno.cardfinder.view.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cards.pay.paycardsrecognizer.sdk.Card
import cards.pay.paycardsrecognizer.sdk.ScanCardIntent
import cards.pay.paycardsrecognizer.sdk.ScanCardIntent.CancelReason
import com.muryno.cardfinder.view.base.BaseActivity
import com.muryno.cardfinder.viewmodel.MainViewModel
import com.muryno.cardfinder.utils.getCartLogo
import com.muryno.cardfinder.utils.getGreetingIcon
import com.muryno.cardfinder.utils.greetings
import com.muryno.cardfinder.utils.setCardNumber
import com.pro100svitlo.creditCardNfcReader.utils.CardNfcUtils
import com.se3.payme.Data.MyData
import com.se3.payme.NFC.MyCardNfcAsyncTask
import com.se3.payme.NFC.extension.TextRecord
import com.se3.payme.NFC.extension.formattedCardNumber
import com.se3.payme.NFC.extension.longShowSnackBar
import com.se3.payme.R
import com.se3.payme.ScanCard.MainApplication


class ScanCard : BaseActivity() , MyCardNfcAsyncTask.MyCardNfcInterface {
    /***** NFC Variavbles  start    **********/
    private var mNfcAdapter: NfcAdapter? = null
    private var mCardNfcUtils: CardNfcUtils? = null
    private var mCardNfcAsyncTask: MyCardNfcAsyncTask? = null
    private var mIntentFromCreate: Boolean = false
    private lateinit var mTurnNfcDialog: AlertDialog

    private lateinit var mPendingIntent: PendingIntent
    private lateinit var mNFCStatusTextView: TextView

    /***** NFC Variavbles  End    **********/


    /*****  Scanner Variable  start    **********/

    private lateinit var btn:Button
    private lateinit var greeting_:TextView
    private lateinit var tv_card_number:TextView
    private lateinit var frame_layout:FrameLayout
    private lateinit var edtCardNumber:TextView
    private lateinit var providerLogo:ImageView
    private lateinit var progress_bar: LinearLayout
    private lateinit var icon_img: ImageView
    private  var viewModel : MainViewModel?=null
    private lateinit var mCardExpirationDate: TextView

    val REQUEST_CODE_SCAN_CARD = 1
    /*****  Scanner Variable  end    **********/



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        mNFCStatusTextView = findViewById(R.id.mNFCStatusTextView)

        mCardExpirationDate = findViewById(R.id.tv_validity)
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)


        btn= findViewById(R.id.btn)
        greeting_ = findViewById(R.id.greeting_)
        tv_card_number = findViewById(R.id.tv_card_number)
        edtCardNumber = findViewById(R.id.edtCardNumber)
        progress_bar = findViewById(R.id.progress_bar)
        providerLogo =  findViewById(R.id.providerLogo)
        icon_img = findViewById(R.id.icon_img)
        frame_layout = findViewById(R.id.frame_layout)

        if (checkNFCEnable()) {
            mCardNfcUtils = CardNfcUtils(this)
            mIntentFromCreate = true
            readNFCInfo()
        } else {
            showTurnOnNfcDialog()
        }
        CardNumberListner()

        view()

    }

    /**************  Scannner Functions ***********/
    //to keep the oncreate page clean
    fun view(){

        //instantiate view model
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        btn.setOnClickListener {
            scanCard()
        }

        //handled in utils
        getGreetingIcon()?.let { icon_img.setImageResource(it) }

        //handled in utils
        greeting_.text = greetings()


        //
        viewModel?._error?.observe(this, Observer {
            toastError(it)
            progressDialog(false)
            Log.d("TAG",it.toString())

        })

        viewModel?._sucessfful?.observe(this, Observer {
            toastSuccess(it)
            progressDialog(false)
        })


        // to observe result from server and send through intent to display page
        viewModel?._card?.observe(this , Observer {
            if (it != null ){
//                val i = Intent(MainApplication.instance!!.applicationContext,CardDetailActivity::class.java)
                val i = Intent(this,CardDetailActivity::class.java)

                i.putExtra("data",it)
                startActivity(i)

            }

            progressDialog(false)

        })

    }



    //card number edit text listner
    private fun CardNumberListner() {

        edtCardNumber.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) { // Remove all spacing char


                // to append logo to card ui
                if(s.isNotEmpty()) {
                    providerLogo.visibility = View.VISIBLE
                    //handled in utils to set card logo
                    providerLogo.setImageResource(getCartLogo(s))
                }else{
                    providerLogo.visibility = View.GONE
                }




                //logic to space card number
                setCardNumber(s)
                if(s.isNotEmpty()){
                    tv_card_number.text =  s.toString()
                    MyData.bankCard = tv_card_number.text.toString()

                }else{
                    tv_card_number.text = getString(R.string.card_number)
                }



                //get card details from server when edit text completed
                postCardDetailsToServer(s)


            }

        })

    }





    //to call the ocr scan
    private fun scanCard() {
        val intent: Intent =  ScanCardIntent.Builder(this).build()
        startActivityForResult(intent, REQUEST_CODE_SCAN_CARD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SCAN_CARD) {
            if (resultCode == Activity.RESULT_OK) {
                val card: Card? = data?.getParcelableExtra(ScanCardIntent.RESULT_PAYCARDS_CARD)

                setCard(card)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                @CancelReason val reason: Int = data?.getIntExtra(
                    ScanCardIntent.RESULT_CANCEL_REASON,
                    ScanCardIntent.BACK_PRESSED
                )
                    ?: ScanCardIntent.BACK_PRESSED


            } else if (resultCode == ScanCardIntent.RESULT_CODE_ERROR) {
//                Log.i(cards.pay.sample.demo.CardDetailsActivity.TAG, "Scan failed")
            }
        }
    }

    //result from ocr and send to view model to post to the server
    private fun setCard(card : Card?){
        if(card!= null){
            //show user progress bar before posting to the server
            progressDialog(true)
            viewModel?.postData(card.cardNumber.toString())
        }
    }


    //post card details to server

    private fun postCardDetailsToServer(s: Editable){

        if(s.length == 19) {
            val k: String = s.toString().replace(" ", "")

            //show user progress bar before posting to the server
            progressDialog(true)
            viewModel?.postData(k)
        }

    }


    //shows progress dialog

    fun progressDialog(bol : Boolean){

        if(bol){
            frame_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.grey))
            progress_bar.visibility = View.VISIBLE
            btn.isEnabled = false
            edtCardNumber.isEnabled = false

        }
        else{
            frame_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            progress_bar.visibility = View.GONE
            btn.isEnabled = true
            edtCardNumber.isEnabled = true
            edtCardNumber.text =""
        }

    }


    /**************  Scannner Functions end ***********/




    /**************  NFC Functions Start ***********/
//    private fun checkNFCEnable(): Boolean {
//        return if (mNfcAdapter == null) {
//            mNFCStatusTextView.text = getString(R.string.tv_noNfc)
//            false
//        } else {
//            mNfcAdapter!!.isEnabled
//        }
//    }
//    private fun readNFCInfo() {
//        onNewIntent(intent)
//    }
//
//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//
////        readNFCTextInfo(intent)
//        if (mNfcAdapter != null && checkNFCEnable()) {
//            mCardNfcAsyncTask = MyCardNfcAsyncTask.Builder(this, intent, mIntentFromCreate).build()
//        }
//
//    }
//    private fun showTurnOnNfcDialog() {
//        mTurnNfcDialog = AlertDialog.Builder(this)
//            .setTitle(getString(R.string.ad_nfcTurnOn_title))
//            .setMessage(getString(R.string.ad_nfcTurnOn_message))
//            .setPositiveButton(
//                getString(R.string.ad_nfcTurnOn_pos)
//            ) { _, _ ->
//                if (Build.VERSION.SDK_INT >= 16) {
//                    startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
//                } else {
//                    startActivity(Intent(android.provider.Settings.ACTION_NFC_SETTINGS))
//                }
//            }.setNegativeButton(getString(R.string.ad_nfcTurnOn_neg)) { _, _ ->
//                onBackPressed()
//            }
//            .create()
//        mTurnNfcDialog.show()
//    }
//
//    override fun startNfcReadCard() {
//    }
//
//    override fun cardIsReadyToRead() {
//        mNFCStatusTextView.visibility = View.GONE
////        mProgressBar.visibility = View.GONE
////        mCardContainer.visibility = View.VISIBLE
//        val cardNumber = formattedCardNumber(mCardNfcAsyncTask?.cardNumber)
//        val expirationDate = mCardNfcAsyncTask?.cardExpireDate
//        val holderName = mCardNfcAsyncTask?.cardHolderFirstName + mCardNfcAsyncTask?.cardHolderLastName
//        val cardType = mCardNfcAsyncTask?.cardType
//        tv_card_number.text = cardNumber
//        mCardExpirationDate.text = expirationDate
////        mCardHolderName.text = holderName
//        if (cardNumber?.get(0).toString()=="4"){
//
//            providerLogo.setImageResource(R.drawable.ic_visa)}
//        else{
//
//            providerLogo.setImageResource(R.drawable.ic_mastercard)
//
//        }
//
//        parseCardType(cardType)    }
//
//    private fun parseCardType(cardType: String?) {
//        when (cardType) {
//            MyCardNfcAsyncTask.CARD_UNKNOWN -> longShowSnackBar(
//                mNFCStatusTextView,
//                getString(R.string.snack_unknown_bank_card)
//            )
//            MyCardNfcAsyncTask.CARD_VISA -> providerLogo.setImageResource(R.drawable.ic_visa)
//            MyCardNfcAsyncTask.CARD_MASTER_CARD -> providerLogo.setImageResource(R.drawable.ic_mastercard)
//        }
//    }
//
//
//    override fun finishNfcReadCard() {
//        mCardNfcAsyncTask = null
//    }
//
//    override fun cardWithLockedNfc() {
//        longShowSnackBar(mNFCStatusTextView, getString(R.string.snack_lockedNfcCard))
//    }
//
//    override fun doNotMoveCardSoFast() {
//        longShowSnackBar(mNFCStatusTextView, getString(R.string.snack_doNotMoveCard))
//    }
//
//    override fun unknownEmvCard() {
//        longShowSnackBar(mNFCStatusTextView, getString(R.string.snack_unknownEmv))
//    }
//
//

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
            MyCardNfcAsyncTask.CARD_VISA -> providerLogo.setImageResource(R.drawable.ic_visa)
            MyCardNfcAsyncTask.CARD_MASTER_CARD -> providerLogo.setImageResource(R.drawable.ic_mastercard)

        }
    }

    override fun startNfcReadCard() {
//        mProgressBar.visibility = View.VISIBLE
    }

    override fun cardIsReadyToRead() {
        mNFCStatusTextView.visibility = View.GONE
//        mProgressBar.visibility = View.GONE
//        mCardContainer.visibility = View.VISIBLE
        val cardNumber = formattedCardNumber(mCardNfcAsyncTask?.cardNumber)
        val expirationDate = mCardNfcAsyncTask?.cardExpireDate
        val holderName = mCardNfcAsyncTask?.cardHolderFirstName + mCardNfcAsyncTask?.cardHolderLastName
        val cardType = mCardNfcAsyncTask?.cardType
        var k: String = cardNumber.toString().replace("-", "")
        k = k.replace(" ", "")

        edtCardNumber.text = k
        mCardExpirationDate.text = expirationDate
//        mCardHolderName.text = holderName
        if (cardNumber?.get(0).toString()=="4"){

            providerLogo.setImageResource(R.drawable.ic_visa)}
        else{

            providerLogo.setImageResource(R.drawable.ic_mastercard)

        }

        viewModel?.postData(k)


//        parseCardType(cardType)
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
/**************  NFC Functions End ***********/



