package com.luys.library.callback

/**
 * @author luys
 * @describe 执行回调命令，用于ViewModel和XML之间的数据传递
 * @date 2020/3/9
 * @email samluys@foxmail.com
 */
class BindingCommand<T>(var execute: BindingAction? = null,
                        var consumer: BindingConsumer<T>? = null,
                        var canExecute0: BindingFunction<Boolean>? = null) {

    /**
     * 执行BindingAction命令
     */
    fun execute() {
        if (canExecute0()) {
            execute?.call()
        }
    }

    /**
     * 执行带泛型参数的命令
     *
     * @param parameter 泛型参数
     */
    fun execute(parameter: T) {
        if (canExecute0()) {
            consumer?.call(parameter)
        }
    }

    /**
     * 是否需要执行
     *
     * @return true则执行, 反之不执行
     */
    private fun canExecute0(): Boolean {
        return if (canExecute0 == null) {
            true
        } else canExecute0?.call() ?: false
    }
}