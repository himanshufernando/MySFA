package tkhub.project.sfa.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_info.*
import tkhub.project.sfa.R
import tkhub.project.sfa.services.perfrence.AppPrefs

class InfoDialog: DialogFragment()  {
    lateinit var dialogListener : InfoDialogListener

    fun setListener (listener : InfoDialogListener){
        dialogListener = listener
    }

    var appPrefs = AppPrefs

    companion object {
        private const val MESSAGE = "message"
        private const val CODE = "code"
        private const val TYPE = "type"
        fun newInstance(message: String = "",code:Int,type:Int): InfoDialog {
            val dialog = InfoDialog()
            val args = Bundle().apply {
                putString(MESSAGE, message)
                putInt(CODE,code)
                putInt(TYPE,type)
            }
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_info, container,false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val message = arguments?.getString(MESSAGE)
        val code = arguments?.getInt(CODE)
        val type = arguments?.getInt(TYPE)
        txt_dialog_message.text = message

        if(type == appPrefs.INFO_DIALOG_ERROR){
            anim_dialog_error.visibility = View.VISIBLE
            anim_dialog_success.visibility = View.GONE
        }else{
            anim_dialog_error.visibility = View.GONE
            anim_dialog_success.visibility = View.VISIBLE

        }

        txt_dialog_positive.setOnClickListener {
            dialogListener.onInfoDialogPositive(code!!)
            dismiss()
        }

    }


    interface InfoDialogListener {
        fun onInfoDialogPositive(code : Int)
    }
}