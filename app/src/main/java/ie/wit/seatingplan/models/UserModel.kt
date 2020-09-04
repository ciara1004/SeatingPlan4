package ie.wit.seatingplan.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class UserModel(var id: String? = " ",
                     var username: String = ""): Parcelable

{
    @Exclude
    fun toMap(): Map<String, Any?>
    {
        return mapOf(
            "id" to id,
            "Username" to username
        )
    }
}