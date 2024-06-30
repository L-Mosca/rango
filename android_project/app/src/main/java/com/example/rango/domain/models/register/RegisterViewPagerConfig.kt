package com.example.rango.domain.models.register

import androidx.annotation.StringRes
import com.example.rango.R
import com.example.rango.screen.credential.CredentialAdapter

data class CredentialViewPagerConfig(
    @StringRes val title: Int,
    @StringRes val actionDescription: Int,
    @StringRes val action: Int,
)

fun Int.buildByPosition(): CredentialViewPagerConfig {
    return when (this) {
        CredentialAdapter.SIGN_IN_PAGE -> CredentialViewPagerConfig(
            title = R.string.login,
            actionDescription = R.string.dont_have_account,
            action = R.string.register
        )

        CredentialAdapter.SIGN_UP_PAGE -> CredentialViewPagerConfig(
            title = R.string.sign_up,
            actionDescription = R.string.have_account,
            action = R.string.enter
        )

        else -> CredentialViewPagerConfig(
            title = R.string.login,
            actionDescription = R.string.dont_have_account,
            action = R.string.register
        )
    }
}
