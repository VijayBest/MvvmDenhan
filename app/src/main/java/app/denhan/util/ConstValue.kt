package app.denhan.util

object ConstValue {
    const val loginStatusTrue = true
    const val loginStatusFalse = false

    const val openJobSelected="1"
    const val inProgressJobSelected="2"
    const val completeJobSelected="3"
    const val workCompletionImages = "1"
    const val beforeCompletionImages="0"
    const val billImages ="2"

    //Sub Task status to maintain the the job status and task status
    const val notStarted="0"//=> not started
    const val started="1" //=> in progress
    const val completed="2" //=> completed task


    // static folder name to stores  the compress image and for uploading the image to server use this folder
    const val temFolder="MyFolder/Images"
    const val deleteTempFolder="MyFolder"

}