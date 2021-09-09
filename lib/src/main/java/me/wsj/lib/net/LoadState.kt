package me.wsj.lib.net

/**
 * 加载状态。
 */
sealed class LoadState {
    /**
     * (开始)加载中。
     */
    class Start(var tip: String = "正在加载中...") : LoadState()

//    /**
//     * 成功
//     */
//    class Success<T>(t: T) : HttpState()

    /**
     * 加载失败。
     */
    class Error(val msg: String) : LoadState()

    /**
     * 加载完成。
     */
    object Finish : LoadState()
}