package samatov.space.spookies.view_model.fragments.notification

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_notification.*
import samatov.space.spookies.R
import samatov.space.spookies.model.MyPreferenceManager
import samatov.space.spookies.model.api.beans.Post
import samatov.space.spookies.model.api.beans.PostRef
import samatov.space.spookies.model.api.beans.User
import samatov.space.spookies.model.api.beans.notification.NotificationActivity
import samatov.space.spookies.model.api.beans.notification.NotificationsFeed
import samatov.space.spookies.model.utils.Formatter
import samatov.space.spookies.view_model.activities.BaseToolbarActivity
import samatov.space.spookies.view_model.activities.FeedActivity
import samatov.space.spookies.view_model.dialogs.favorite.FavoriteDialogHandler


class NotificationFragment : Fragment() {

    companion object {
        fun newInstance(): NotificationFragment {

            return NotificationFragment()
        }
    }

    var mActivity: BaseToolbarActivity? = null
    private var mFeed: NotificationsFeed? = null
    private var mAdapter: NotificationListAdapter? = null
    private var mCurrentPost: Post? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mActivity = activity as BaseToolbarActivity
        mActivity?.setMainToolbarTitle("Notifications")
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mActivity?.getNotificationFeed(true, true) { result, _ ->
            mFeed = result as NotificationsFeed
            var formattedList = formatNotificationActivityList()
            formattedList = formattedList.sortedWith(compareByDescending { it.timestamp })
            setupRecyclerView(formattedList)
            if (formattedList.isEmpty())
                notificationFragmentEmptyContainer.visibility = View.VISIBLE
        }
        super.onViewCreated(view, savedInstanceState)
    }


    private fun setupRecyclerView(formattedList: List<NotificationActivity>) {
        mAdapter = NotificationListAdapter(formattedList, getOnNotificationClickListener())
        notificationFragmentRecyclerView.layoutManager = LinearLayoutManager(context)
        notificationFragmentRecyclerView.adapter = mAdapter
    }


    private fun getOnNotificationClickListener(): (NotificationActivity) -> Unit {
        return {
            when {
                it.verb == "like" -> fetchPostAndShowLikes(it)
                it.verb == "comment" -> fetchPostAndStartReadFragment(it)
                it.verb == "follow" -> fetchUserAndStartViewProfileActivity(it)
            }
        }
    }


    private fun fetchPostAndShowLikes(activity: NotificationActivity) {
        if (mCurrentPost != null && activity.activityObject == mCurrentPost?.id) {
            displayLikesDialog(mCurrentPost)
            return
        }
        mActivity?.fetchMyPublished(activity.activityObject) { result, _ ->
            val post: Post? = result as Post
            mCurrentPost = post
            displayLikesDialog(mCurrentPost)
        }
    }


    private fun displayLikesDialog(post: Post?) {
        val postRef: PostRef = Formatter.constructRefFromPost(post)
        val dialogHandler = FavoriteDialogHandler(mActivity!!, postRef.favorite, postRef.title)
        dialogHandler.showDialog()
    }



    private fun fetchPostAndStartReadFragment(activity: NotificationActivity) {
        mActivity?.let {
            val currentUser = MyPreferenceManager
                    .getObject(it, MyPreferenceManager.CURRENT_USER, User::class.java)
            val ref = PostRef(activity.activityObject!!, currentUser.username)
            if (it is FeedActivity)
                it.startReadCommentsFragment(ref)
            else
                it.fetchPostAndStartReadCommentFragment(ref, it.mPlaceholder)
        }
    }


    private fun fetchUserAndStartViewProfileActivity(activity: NotificationActivity) {
        mActivity?.let {
            it.getUserAndStartViewProfileActivity(activity.actor, false)
        }
    }


    private fun formatNotificationActivityList(): List<NotificationActivity> {
        val list: MutableList<NotificationActivity> = ArrayList()

        mFeed?.results?.forEach { group ->
            group.activities.forEach {
                list.add(it)
            }
        }

        return list
    }

}
