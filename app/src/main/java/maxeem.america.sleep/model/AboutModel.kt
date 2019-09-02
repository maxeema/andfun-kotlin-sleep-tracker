package maxeem.america.sleep.model

import maxeem.america.sleep.R
import maxeem.america.sleep.app
import maxeem.america.sleep.ext.fromHtml
import maxeem.america.sleep.packageInfo

class AboutModel : BaseModel() {

    val author = app.getString(R.string.app_author)
    val version = app.packageInfo.versionName.substringBefore('-')
    val description = app.getString(R.string.app_description).fromHtml()

}
