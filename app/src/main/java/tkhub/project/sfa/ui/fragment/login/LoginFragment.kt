package tkhub.project.sfa.ui.fragment.login


import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.github.pwittchen.rxbiometric.library.RxBiometric
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationError
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationFail
import com.github.pwittchen.rxbiometric.library.throwable.BiometricNotSupported
import com.github.pwittchen.rxbiometric.library.validation.RxPreconditions
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_login.view.*
import tkhub.project.sfa.R
import tkhub.project.sfa.data.model.SfaViewModelResult
import tkhub.project.sfa.databinding.FragmentLoginBinding
import tkhub.project.sfa.services.perfrence.AppPrefs
import tkhub.project.sfa.ui.activity.MainActivity
import tkhub.project.sfa.ui.dialog.InfoDialog
import tkhub.project.sfa.viewmodels.user.UserViewModel


class LoginFragment : Fragment(), View.OnClickListener,  InfoDialog.InfoDialogListener {
    lateinit var mainActivity: MainActivity
    lateinit var binding: FragmentLoginBinding
    private val viewmodel: UserViewModel by viewModels { UserViewModel.LiveDataVMFactory }

    var appPrefs = AppPrefs
    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.userViewModel = viewmodel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.root.cl_signin.setOnClickListener(this)
        binding.root.img_fingerprint.setOnClickListener(this)



    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        mainActivity = (activity as MainActivity)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onResume() {
        super.onResume()

        trasperat(mainActivity)
        mainActivity.lockDrawer()
        mainActivity.getMenus()
    }

    override fun onStop() {
        super.onStop()
        viewmodel.getUserLoginResponse.removeObservers(viewLifecycleOwner)
        viewmodel.pushTokenUpdateResponse.removeObservers(viewLifecycleOwner)
    }

    override fun onPause() {
        super.onPause()
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onClick(v: View) {

        when (v.id) {
            R.id.cl_signin -> {
                if (!viewmodel.getUserLoginResponse.hasObservers()) {
                    getUserLoginResponseObserver()
                }
                viewmodel.userLogin()
            }

            R.id.img_fingerprint -> {
                if(appPrefs.getUserPrefs().user_id == 0L){
                    showInfoDialog("You need to sing in to use fingerprint authentication !",0,appPrefs.INFO_DIALOG_ERROR)
                }else{
                    fingerPrint()
                }

            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun fingerPrint(){
        disposable =
            RxPreconditions
                .hasBiometricSupport(requireContext())
                .flatMapCompletable {
                    if (!it) Completable.error(BiometricNotSupported())
                    else
                        RxBiometric
                            .title("Sing In")
                            .description("User your fingerprint to sing in")
                            .negativeButtonText("cancel")
                            .negativeButtonListener(DialogInterface.OnClickListener { _, _ ->
                                showMessage("cancel")
                            })
                            .executor(ActivityCompat.getMainExecutor(requireContext()))
                            .build()
                            .authenticate(mainActivity)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onComplete = { showMessage("authenticated!") },
                    onError = {
                        when (it) {
                            is AuthenticationError -> showMessage("error: ${it.errorCode} ${it.errorMessage}")
                            is AuthenticationFail -> showMessage("fail")
                            else -> {
                                showMessage("You don't have permissions to fingerprint authentication !!")
                            }
                        }
                    }
                )

    }

    private fun showMessage(message: String) {
        Toast
            .makeText(
                requireContext(),
                message,
                Toast.LENGTH_SHORT
            )
            .show()
    }

    private fun getUserLoginResponseObserver() {
        viewmodel.getUserLoginResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is SfaViewModelResult.Success -> {
                    if (!viewmodel.pushTokenUpdateResponse.hasObservers()) {
                        pushTokenUpdateResponseObserver()
                    }
                    viewmodel.pushTokenUpdate(it.data.data.user_id)

                    NavHostFragment.findNavController(this).navigate(R.id.fragment_login_to_home)
                }
                is SfaViewModelResult.ExceptionError -> {
                    showInfoDialog(it.exception.message.toString(),0,appPrefs.INFO_DIALOG_ERROR)
                }
                is SfaViewModelResult.LogicalError -> {
                    showInfoDialog(it.exception.message,0,appPrefs.INFO_DIALOG_ERROR)
                }

            }
        })
    }


    private fun pushTokenUpdateResponseObserver() {
        viewmodel.pushTokenUpdateResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is SfaViewModelResult.Success -> {

                }
                is SfaViewModelResult.ExceptionError -> {

                }
                is SfaViewModelResult.LogicalError -> {

                }

            }
        })
    }

    private fun trasperat(activity: Activity) {
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            activity.window.statusBarColor = Color.WHITE
            activity.window.navigationBarColor = Color.BLACK
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = requireActivity().window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    private fun showInfoDialog(message: String, code: Int, type: Int) {
        val dialogInfoFragment = InfoDialog.newInstance(message, code,type)
        dialogInfoFragment.setListener(this)
        dialogInfoFragment.show(activity?.supportFragmentManager!!, "")
    }

    override fun onInfoDialogPositive(code: Int) {

    }


}