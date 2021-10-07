package tkhub.project.sfa.ui.fragment.splash

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tkhub.project.sfa.R
import tkhub.project.sfa.services.perfrence.AppPrefs
import tkhub.project.sfa.ui.activity.MainActivity
import kotlin.math.ceil


class SplashFragment : Fragment() {
    lateinit var mainActivity: MainActivity

    var appPrefs = AppPrefs


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)

    }

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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

        if(appPrefs.getUserPrefs().user_id == 0L){
            try {
                lifecycleScope.launch(context = Dispatchers.Main) {
                    delay(3000)
                    goToLogin()

                }
            }catch (e : Exception){
                e.printStackTrace()
               goToLogin()
            }

        }else{
            try {
                lifecycleScope.launch(context = Dispatchers.Main) {
                    delay(3000)
                    goToHome()

                }
            }catch (e : Exception){
                e.printStackTrace()
                 goToHome()
            }
        }


    }

    private fun goToLogin(){ NavHostFragment.findNavController(this).navigate(R.id.fragment_splash_to_login) }
    private fun goToHome(){ NavHostFragment.findNavController(this).navigate(R.id.fragment_splash_to_home) }

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
}