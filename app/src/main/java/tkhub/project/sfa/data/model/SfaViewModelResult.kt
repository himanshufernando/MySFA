package tkhub.project.sfa.data.model




sealed  class SfaViewModelResult <out T : Any>{
     class Success<out T : Any>(val data: T) : SfaViewModelResult<T>()
     sealed class ExceptionError(val exception: Exception) : SfaViewModelResult<Nothing>() {
          class ExError(exception: Exception) : ExceptionError(exception)
     }
     sealed class LogicalError(val exception: BaseApiModal) : SfaViewModelResult<Nothing>() {
          class LogError(exception: BaseApiModal) : LogicalError(exception)
     }
}