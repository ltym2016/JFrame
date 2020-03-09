package com.luys.library.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.luys.library.R
import com.samluys.statusbar.StatusBarUtils
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import java.lang.ref.WeakReference

class ContainerActivity : RxAppCompatActivity() {

    private val FRAGMENT_TAG = "content_fragment_tag"
    protected lateinit var mFragment : WeakReference<Fragment>

    companion object{
        const val FRAGMENT = "fragment"
        const val BUNDLE = "BUNDLE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        StatusBarUtils.transparencyBar(this, false)
        val fm = supportFragmentManager
        var fragment = savedInstanceState?.let {
            fm.getFragment(it,FRAGMENT_TAG)
        }

        if (fragment == null) {
            fragment = initFromIntent(intent)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_content, fragment)
            .commitAllowingStateLoss()

        mFragment = WeakReference<Fragment>(fragment)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mFragment.get()?.let {
            supportFragmentManager.putFragment(outState, FRAGMENT_TAG, it)
        }
    }

    private fun initFromIntent(data:Intent) : Fragment {
        try {
            val fragmentName = data.getStringExtra(FRAGMENT)
            require(!(fragmentName == null || "" == fragmentName)) { "can not find page fragmentName" }

            val fragmentClass = Class.forName(fragmentName)
            val fragment : Fragment = fragmentClass.newInstance() as Fragment
            val args = data.getBundleExtra(BUNDLE)
            args?.let {
                fragment.arguments = args
            }

            return fragment
        } catch (e:Exception) {
            e.printStackTrace()
        }

        throw RuntimeException("fragment initialization failed!")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fl_content)
//        if (fragment is BaseFragen) {
//
//        }

        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mFragment.get() != null) {
            mFragment.clear()
        }
    }
}
