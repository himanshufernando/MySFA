package tkhub.project.sfa.services.listeners

import java.io.File

interface CustomerPhotoListener {
    fun onCustomerPhotoUrlResponse(file: File)
    fun onCustomerPhotoUrlResponseError(error: Throwable)
}