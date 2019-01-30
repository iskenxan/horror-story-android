package samatov.space.spookies.view_model.fragments.posts_viewpager

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.fragment_posts_list.*
import samatov.space.spookies.R
import samatov.space.spookies.model.MyPreferenceManager
import samatov.space.spookies.model.api.beans.IdPostRef
import samatov.space.spookies.model.api.beans.PostRef
import samatov.space.spookies.model.enums.POST_TYPE
import samatov.space.spookies.model.utils.Serializer
import samatov.space.spookies.model.utils.SerializerK
import samatov.space.spookies.view_model.activities.my_profile.MyProfileActivity
import samatov.space.spookies.view_model.dialogs.favorite.FavoriteDialogHandler
import samatov.space.spookies.view_model.fragments.BaseFragment


class PostsListFragment : BaseFragment() {

    companion object {


        fun newInstance(posts: Map<String, PostRef>, type: POST_TYPE): PostsListFragment {
            val fragment = PostsListFragment()

            val jsonStr = Serializer.toString(posts)
            val bundle = Bundle()
            bundle.putString("posts", jsonStr)
            bundle.putString("post_type", type.toString())
            fragment.arguments = bundle

            return fragment
        }
    }


    private var mPosts: Map<String, PostRef>? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var mAdapter: PostsListAdapter? = null
    private var mActivity: MyProfileActivity? = null
    private var mPostsType: POST_TYPE? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_posts_list, container, false)
        ButterKnife.bind(this, view)

        mActivity = activity as MyProfileActivity?

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extractPostsArgument()
        setupViews()
    }


    private fun extractPostsArgument() {
        val postsStr = arguments!!.getString("posts")
        val postType = arguments!!.getString("post_type")
        if (postsStr != null)
            mPosts = SerializerK.fromJson(postsStr)
        if (postType != null)
            mPostsType = POST_TYPE.valueOf(postType)
    }


    private fun setupViews() {
        val list = getFormattedPostList(mPosts)
        if (list.size <= 0) {
            postListFragmentRecyclerView!!.visibility = View.GONE
            postListEmptyListContainer!!.visibility = View.VISIBLE
        } else
            setupRecyclerView(list)
    }


    private fun setupRecyclerView(list: List<IdPostRef>) {
        mLayoutManager = LinearLayoutManager(context)
        postListFragmentRecyclerView!!.layoutManager = mLayoutManager
        mAdapter = PostsListAdapter(list, itemClickedListener, true, mPostsType)
        postListFragmentRecyclerView!!.adapter = mAdapter
    }


    private val itemClickedListener: (String, ClickItemType) -> Unit
        get() = { postId, clickedType ->
            when (clickedType) {
                ClickItemType.READ_POST -> onReadPostClicked(postId)
                ClickItemType.FAVORITE -> onViewLikesClicked(postId)
                ClickItemType.COMMENT -> onViewCommentsClicked(postId)
            }
        }


    private fun onReadPostClicked(postId: String) {
        MyPreferenceManager.saveObjectAsJson(context, MyPreferenceManager.CURRENT_POST_TYPE, mPostsType)
        MyPreferenceManager.saveString(context, MyPreferenceManager.CURRENT_POST_ID, postId)
        mActivity!!.startEditPostActivity()
    }


    private fun onViewLikesClicked(postId: String) {
        val post = getPost(postId)
        val dialogHandler = FavoriteDialogHandler(mActivity!!, post.favorite)
        dialogHandler.showDialog()
    }


    private fun onViewCommentsClicked(postId: String) {
        val post = getPost(postId)
        mActivity!!.startViewCommentFragment(post)
    }


    private fun getPost(postId: String): IdPostRef {

        return getIdPostRef(postId, mPosts)
    }


    override fun onResume() {
        super.onResume()
        extractPostsArgument()
        setupViews()
    }
}
