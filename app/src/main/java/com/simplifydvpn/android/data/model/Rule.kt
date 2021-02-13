package com.simplifydvpn.android.data.model

import com.google.gson.annotations.SerializedName

data class Rule(
    var id: String?,
    val type: RuleType?,
    val category_id: String?,
    val category_name: String?,
    val domain: String?,
    val action: RuleAction?
) {
    val isBlocked: Boolean
        get() = !id.isNullOrEmpty()
}

enum class RuleType {

    @SerializedName("domain")
    DOMAIN,

    @SerializedName("category")
    CATEGORY
}

enum class RuleAction {

    @SerializedName("allow")
    ALLOW,

    @SerializedName("block")
    BLOCK,
}