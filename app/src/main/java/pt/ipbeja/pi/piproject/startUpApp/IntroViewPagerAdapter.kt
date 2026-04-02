package pt.ipbeja.pi.piproject.startUpApp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import pt.ipbeja.pi.piproject.R

/**
 * Adapter for the IntroActivity ViewPager.
 */
class IntroViewPagerAdapter(
    private val mContext: Context,
    private val mListScreen: List<ScreenItem>
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutScreen = inflater.inflate(R.layout.layout_screen, null)

        val imgSlide = layoutScreen.findViewById<ImageView>(R.id.intro_img)
        val title = layoutScreen.findViewById<TextView>(R.id.intro_title)
        val description = layoutScreen.findViewById<TextView>(R.id.intro_description)

        val item = mListScreen[position]
        title.text = item.title
        description.text = item.description
        imgSlide.setImageResource(item.screenImg)

        container.addView(layoutScreen)

        return layoutScreen
    }

    override fun getCount(): Int = mListScreen.size

    override fun isViewFromObject(view: View, o: Any): Boolean = view === o

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}